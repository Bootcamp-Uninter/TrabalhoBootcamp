package br.uninter.medalerta.service;

import br.uninter.medalerta.model.Usuario;
import br.uninter.medalerta.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository repository;

    @InjectMocks
    private UsuarioService service;

    private Usuario existente;

    @BeforeEach
    void setup() {
        existente = new Usuario();
        existente.setIdUsuario(10);
        existente.setNome("Ana");
        existente.setTelefone("11999990000");
        existente.setEmail("ana@email.com");
        existente.setEnderecoRua("Rua A");
        existente.setEnderecoNumero(123);
        existente.setEnderecoComplemento("Ap 10");
        existente.setEnderecoBairro("Centro");
        existente.setEnderecoCEP("01000-000");
        existente.setEnderecoCidade("São Paulo");
        existente.setEnderecoEstado("SP");
    }

    @Nested
    @DisplayName("salvar")
    class Salvar {

        @Test
        void deveChamarSaveERetornarOResultadoQuandoUsuarioValido() {
            Usuario novo = new Usuario();
            novo.setNome("Bruno");

            when(repository.save(novo)).thenReturn(novo);

            Usuario salvo = service.salvar(novo);

            assertSame(novo, salvo);
            verify(repository).save(novo);
            verifyNoMoreInteractions(repository);
        }
    }

    @Nested
    @DisplayName("listarTodos")
    class ListarTodos {

        @Test
        void deveRetornarListaQuandoRepositoryRetornarItens() {
            when(repository.findAll()).thenReturn(List.of(existente));

            List<Usuario> resultado = service.listarTodos();

            assertEquals(1, resultado.size());
            assertSame(existente, resultado.get(0));
            verify(repository).findAll();
            verifyNoMoreInteractions(repository);
        }

        @Test
        void deveRetornarListaVaziaQuandoRepositoryNaoRetornarItens() {
            when(repository.findAll()).thenReturn(List.of());

            List<Usuario> resultado = service.listarTodos();

            assertNotNull(resultado);
            assertTrue(resultado.isEmpty());
            verify(repository).findAll();
            verifyNoMoreInteractions(repository);
        }
    }

    @Nested
    @DisplayName("buscarPorId")
    class BuscarPorId {

        @Test
        void deveRetornarUsuarioQuandoIdExistir() {
            when(repository.findById(10)).thenReturn(Optional.of(existente));

            Usuario resultado = service.buscarPorId(10);

            assertSame(existente, resultado);
            verify(repository).findById(10);
            verifyNoMoreInteractions(repository);
        }

        @Test
        void deveLancarExcecaoQuandoIdNaoExistir() {
            when(repository.findById(99)).thenReturn(Optional.empty());

            RuntimeException ex = assertThrows(RuntimeException.class, () -> service.buscarPorId(99));
            assertEquals("Usuário não encontrado: 99", ex.getMessage());

            verify(repository).findById(99);
            verifyNoMoreInteractions(repository);
        }
    }

    @Nested
    @DisplayName("atualizar")
    class Atualizar {

        @Test
        void deveCopiarCamposESalvarQuandoIdExistir() {
            when(repository.findById(10)).thenReturn(Optional.of(existente));
            when(repository.save(any(Usuario.class))).thenAnswer(inv -> inv.getArgument(0));

            Usuario novo = new Usuario();
            novo.setNome("Carla");
            novo.setTelefone("11888887777");
            novo.setEmail("carla@email.com");
            novo.setEnderecoRua("Rua B");
            novo.setEnderecoNumero(999);
            novo.setEnderecoComplemento(null);
            novo.setEnderecoBairro("Bairro Novo");
            novo.setEnderecoCEP("02000-000");
            novo.setEnderecoCidade("Curitiba");
            novo.setEnderecoEstado("PR");

            Usuario atualizado = service.atualizar(10, novo);

            assertNotNull(atualizado);

            assertEquals("Carla", existente.getNome());
            assertEquals("11888887777", existente.getTelefone());
            assertEquals("carla@email.com", existente.getEmail());
            assertEquals("Rua B", existente.getEnderecoRua());
            assertEquals(999, existente.getEnderecoNumero());
            assertNull(existente.getEnderecoComplemento());
            assertEquals("Bairro Novo", existente.getEnderecoBairro());
            assertEquals("02000-000", existente.getEnderecoCEP());
            assertEquals("Curitiba", existente.getEnderecoCidade());
            assertEquals("PR", existente.getEnderecoEstado());

            verify(repository).findById(10);
            verify(repository).save(existente);
            verifyNoMoreInteractions(repository);
        }

        @Test
        void deveLancarExcecaoQuandoIdNaoExistir() {
            when(repository.findById(123)).thenReturn(Optional.empty());

            assertThrows(RuntimeException.class, () -> service.atualizar(123, new Usuario()));

            verify(repository).findById(123);
            verify(repository, never()).save(any());
            verifyNoMoreInteractions(repository);
        }
    }

    @Nested
    @DisplayName("deletar")
    class Deletar {

        @Test
        void deveChamarDeleteQuandoIdExistir() {
            when(repository.findById(10)).thenReturn(Optional.of(existente));

            service.deletar(10);

            verify(repository).findById(10);
            verify(repository).delete(existente);
            verifyNoMoreInteractions(repository);
        }

        @Test
        void deveLancarExcecaoQuandoIdNaoExistir() {
            when(repository.findById(123)).thenReturn(Optional.empty());

            assertThrows(RuntimeException.class, () -> service.deletar(123));

            verify(repository).findById(123);
            verify(repository, never()).delete(any());
            verifyNoMoreInteractions(repository);
        }
    }
}