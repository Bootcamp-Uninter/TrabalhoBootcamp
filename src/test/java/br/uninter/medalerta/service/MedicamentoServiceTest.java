package br.uninter.medalerta.service;

import br.uninter.medalerta.model.Medicamento;
import br.uninter.medalerta.model.QuantidadeTipo;
import br.uninter.medalerta.repository.MedicamentoRepository;
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
class MedicamentoServiceTest {

    @Mock
    private MedicamentoRepository repository;

    @InjectMocks
    private MedicamentoService service;

    private Medicamento existente;

    @BeforeEach
    void setup() {
        existente = new Medicamento();
        existente.setIdMedicamento(10);
        existente.setNomeComercial("Dipirona");
        existente.setNomeGenerico("Metamizol");
        existente.setQuantidade(QuantidadeTipo.UNIDADE);
        existente.setFormaUso("Oral");
        existente.setObservacao("Original");
    }

    @Nested
    @DisplayName("salvar")
    class Salvar {

        @Test
        void deveChamarSaveERetornarOResultadoQuandoMedicamentoValido() {
            Medicamento novo = new Medicamento();
            novo.setNomeComercial("Paracetamol");

            when(repository.save(novo)).thenReturn(novo);

            Medicamento salvo = service.salvar(novo);

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

            List<Medicamento> resultado = service.listarTodos();

            assertEquals(1, resultado.size());
            assertSame(existente, resultado.get(0));
            verify(repository).findAll();
            verifyNoMoreInteractions(repository);
        }

        @Test
        void deveRetornarListaVaziaQuandoRepositoryNaoRetornarItens() {
            when(repository.findAll()).thenReturn(List.of());

            List<Medicamento> resultado = service.listarTodos();

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
        void deveRetornarMedicamentoQuandoIdExistir() {
            when(repository.findById(10)).thenReturn(Optional.of(existente));

            Medicamento resultado = service.buscarPorId(10);

            assertSame(existente, resultado);
            verify(repository).findById(10);
            verifyNoMoreInteractions(repository);
        }

        @Test
        void deveLancarExcecaoQuandoIdNaoExistir() {
            when(repository.findById(99)).thenReturn(Optional.empty());

            RuntimeException ex = assertThrows(RuntimeException.class, () -> service.buscarPorId(99));
            assertEquals("Medicamento não encontrado: 99", ex.getMessage());

            verify(repository).findById(99);
            verifyNoMoreInteractions(repository);
        }
    }

    @Nested
    @DisplayName("atualizar")
    class Atualizar {

        @Test
        void deveAtualizarCamposESalvarQuandoCamposForemPreenchidos() {
            when(repository.findById(10)).thenReturn(Optional.of(existente));
            when(repository.save(any(Medicamento.class))).thenAnswer(inv -> inv.getArgument(0));

            Medicamento novo = new Medicamento();
            novo.setNomeComercial("Ibuprofeno");
            novo.setNomeGenerico("Ibuprofeno");
            novo.setQuantidade(QuantidadeTipo.ML);
            novo.setFormaUso("Tópico");
            novo.setObservacao("Nova obs");

            Medicamento atualizado = service.atualizar(10, novo);

            assertEquals("Ibuprofeno", atualizado.getNomeComercial());
            assertEquals("Ibuprofeno", atualizado.getNomeGenerico());
            assertEquals(QuantidadeTipo.ML, atualizado.getQuantidade());
            assertEquals("Tópico", atualizado.getFormaUso());
            assertEquals("Nova obs", atualizado.getObservacao());

            verify(repository).findById(10);
            verify(repository).save(existente);
            verifyNoMoreInteractions(repository);
        }

        @Test
        void naoDeveSobrescreverStringsQuandoVieremNulasOuVazias() {
            when(repository.findById(10)).thenReturn(Optional.of(existente));
            when(repository.save(any(Medicamento.class))).thenAnswer(inv -> inv.getArgument(0));

            Medicamento novo = new Medicamento();
            novo.setNomeComercial("");
            novo.setNomeGenerico(null);
            novo.setFormaUso("");
            novo.setObservacao(null);
            novo.setQuantidade(null);

            Medicamento atualizado = service.atualizar(10, novo);

            assertEquals("Dipirona", atualizado.getNomeComercial());
            assertEquals("Metamizol", atualizado.getNomeGenerico());
            assertEquals("Oral", atualizado.getFormaUso());
            assertEquals("Original", atualizado.getObservacao());
            assertEquals(QuantidadeTipo.UNIDADE, atualizado.getQuantidade());

            verify(repository).findById(10);
            verify(repository).save(existente);
            verifyNoMoreInteractions(repository);
        }

        @Test
        void deveLancarExcecaoQuandoIdNaoExistir() {
            when(repository.findById(123)).thenReturn(Optional.empty());

            assertThrows(RuntimeException.class, () -> service.atualizar(123, new Medicamento()));

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