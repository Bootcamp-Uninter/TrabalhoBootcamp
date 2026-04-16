package br.uninter.medalerta.service;

import br.uninter.medalerta.model.*;
import br.uninter.medalerta.repository.MedicamentoRepository;
import br.uninter.medalerta.repository.UsuarioMedicamentoRepository;
import br.uninter.medalerta.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioMedicamentoServiceTest {

    @Mock
    private UsuarioMedicamentoRepository repository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private MedicamentoRepository medicamentoRepository;

    @InjectMocks
    private UsuarioMedicamentoService service;

    private Usuario usuario;
    private Medicamento medicamento;

    private Integer idUsuario;
    private Integer idMedicamento;

    private LocalTime horarioUso;
    private String frequenciaUso;
    private String dosagem;
    private LocalDateTime dataHorarioAlerta;
    private StatusAlerta statusAlerta;
    private LocalDateTime dataHorarioConsumo;
    private ConfirmacaoConsumo confirmacaoConsumo;

    @BeforeEach
    void setup() {
        idUsuario = 1;
        idMedicamento = 2;

        usuario = new Usuario();
        usuario.setIdUsuario(idUsuario);

        medicamento = new Medicamento();
        medicamento.setIdMedicamento(idMedicamento);

        horarioUso = LocalTime.of(8, 0, 0);
        frequenciaUso = "diário";
        dosagem = "1 comprimido";
        dataHorarioAlerta = LocalDateTime.of(2026, 4, 16, 8, 0, 0);
        statusAlerta = StatusAlerta.EMITIDO;
        dataHorarioConsumo = LocalDateTime.of(2026, 4, 16, 8, 5, 0);
        confirmacaoConsumo = ConfirmacaoConsumo.SIM;
    }

    @Nested
    @DisplayName("vincular")
    class Vincular {

        @Test
        void deveSalvarVinculoQuandoUsuarioEMedicamentoExistiremEAindaNaoHouverVinculo() {
            when(usuarioRepository.findById(idUsuario)).thenReturn(Optional.of(usuario));
            when(medicamentoRepository.findById(idMedicamento)).thenReturn(Optional.of(medicamento));
            when(repository.existsById(new UsuarioMedicamentoId(idUsuario, idMedicamento))).thenReturn(false);
            when(repository.save(any(UsuarioMedicamento.class))).thenAnswer(inv -> inv.getArgument(0));

            UsuarioMedicamento salvo = service.vincular(
                    idUsuario,
                    idMedicamento,
                    horarioUso,
                    frequenciaUso,
                    dosagem,
                    dataHorarioAlerta,
                    statusAlerta,
                    dataHorarioConsumo,
                    confirmacaoConsumo
            );

            assertNotNull(salvo);
            assertNotNull(salvo.getId());
            assertEquals(idUsuario, salvo.getId().getIdUsuario());
            assertEquals(idMedicamento, salvo.getId().getIdMedicamento());
            assertSame(usuario, salvo.getUsuario());
            assertSame(medicamento, salvo.getMedicamento());
            assertEquals(horarioUso, salvo.getHorarioUso());
            assertEquals(frequenciaUso, salvo.getFrequenciaUso());
            assertEquals(dosagem, salvo.getDosagem());
            assertEquals(dataHorarioAlerta, salvo.getDataHorarioAlerta());
            assertEquals(statusAlerta, salvo.getStatusAlerta());
            assertEquals(dataHorarioConsumo, salvo.getDataHorarioConsumo());
            assertEquals(confirmacaoConsumo, salvo.getConfirmacaoConsumo());

            verify(usuarioRepository).findById(idUsuario);
            verify(medicamentoRepository).findById(idMedicamento);
            verify(repository).existsById(new UsuarioMedicamentoId(idUsuario, idMedicamento));

            ArgumentCaptor<UsuarioMedicamento> captor = ArgumentCaptor.forClass(UsuarioMedicamento.class);
            verify(repository).save(captor.capture());
            UsuarioMedicamento entidadeSalva = captor.getValue();
            assertEquals(idUsuario, entidadeSalva.getId().getIdUsuario());
            assertEquals(idMedicamento, entidadeSalva.getId().getIdMedicamento());

            verifyNoMoreInteractions(repository, usuarioRepository, medicamentoRepository);
        }

        @Test
        void deveLancarExcecaoQuandoUsuarioNaoExistir() {
            when(usuarioRepository.findById(idUsuario)).thenReturn(Optional.empty());

            RuntimeException ex = assertThrows(RuntimeException.class, () -> service.vincular(
                    idUsuario,
                    idMedicamento,
                    horarioUso,
                    frequenciaUso,
                    dosagem,
                    dataHorarioAlerta,
                    statusAlerta,
                    dataHorarioConsumo,
                    confirmacaoConsumo
            ));

            assertEquals("Usuário não encontrado: " + idUsuario, ex.getMessage());

            verify(usuarioRepository).findById(idUsuario);
            verifyNoMoreInteractions(usuarioRepository);
            verifyNoInteractions(medicamentoRepository, repository);
        }

        @Test
        void deveLancarExcecaoQuandoMedicamentoNaoExistir() {
            when(usuarioRepository.findById(idUsuario)).thenReturn(Optional.of(usuario));
            when(medicamentoRepository.findById(idMedicamento)).thenReturn(Optional.empty());

            RuntimeException ex = assertThrows(RuntimeException.class, () -> service.vincular(
                    idUsuario,
                    idMedicamento,
                    horarioUso,
                    frequenciaUso,
                    dosagem,
                    dataHorarioAlerta,
                    statusAlerta,
                    dataHorarioConsumo,
                    confirmacaoConsumo
            ));

            assertEquals("Medicamento não encontrado: " + idMedicamento, ex.getMessage());

            verify(usuarioRepository).findById(idUsuario);
            verify(medicamentoRepository).findById(idMedicamento);
            verifyNoMoreInteractions(usuarioRepository, medicamentoRepository);
            verifyNoInteractions(repository);
        }

        @Test
        void deveLancarExcecaoQuandoVinculoJaExistir() {
            when(usuarioRepository.findById(idUsuario)).thenReturn(Optional.of(usuario));
            when(medicamentoRepository.findById(idMedicamento)).thenReturn(Optional.of(medicamento));
            when(repository.existsById(new UsuarioMedicamentoId(idUsuario, idMedicamento))).thenReturn(true);

            RuntimeException ex = assertThrows(RuntimeException.class, () -> service.vincular(
                    idUsuario,
                    idMedicamento,
                    horarioUso,
                    frequenciaUso,
                    dosagem,
                    dataHorarioAlerta,
                    statusAlerta,
                    dataHorarioConsumo,
                    confirmacaoConsumo
            ));

            assertEquals("Esse vínculo usuário-Medicamento já existe.", ex.getMessage());

            verify(usuarioRepository).findById(idUsuario);
            verify(medicamentoRepository).findById(idMedicamento);
            verify(repository).existsById(new UsuarioMedicamentoId(idUsuario, idMedicamento));
            verifyNoMoreInteractions(usuarioRepository, medicamentoRepository, repository);
            verify(repository, never()).save(any());
        }
    }

    @Nested
    @DisplayName("listarTodos")
    class ListarTodos {

        @Test
        void deveRetornarListaQuandoRepositoryRetornarItens() {
            UsuarioMedicamento um = new UsuarioMedicamento();
            when(repository.findAll()).thenReturn(List.of(um));

            List<UsuarioMedicamento> resultado = service.listarTodos();

            assertEquals(1, resultado.size());
            assertSame(um, resultado.get(0));
            verify(repository).findAll();
            verifyNoMoreInteractions(repository);
            verifyNoInteractions(usuarioRepository, medicamentoRepository);
        }
    }

    @Nested
    @DisplayName("listarPorUsuario")
    class ListarPorUsuario {

        @Test
        void deveChamarFindByUsuarioIdQuandoIdUsuarioInformado() {
            UsuarioMedicamento um = new UsuarioMedicamento();
            when(repository.findByUsuario_IdUsuario(idUsuario)).thenReturn(List.of(um));

            List<UsuarioMedicamento> resultado = service.listarPorUsuario(idUsuario);

            assertEquals(1, resultado.size());
            verify(repository).findByUsuario_IdUsuario(idUsuario);
            verifyNoMoreInteractions(repository);
            verifyNoInteractions(usuarioRepository, medicamentoRepository);
        }
    }

    @Nested
    @DisplayName("buscarPorId")
    class BuscarPorId {

        @Test
        void deveRetornarVinculoQuandoExistir() {
            UsuarioMedicamentoId id = new UsuarioMedicamentoId(idUsuario, idMedicamento);
            UsuarioMedicamento existente = new UsuarioMedicamento();
            existente.setId(id);

            when(repository.findById(id)).thenReturn(Optional.of(existente));

            UsuarioMedicamento resultado = service.buscarPorId(idUsuario, idMedicamento);

            assertSame(existente, resultado);
            verify(repository).findById(id);
            verifyNoMoreInteractions(repository);
            verifyNoInteractions(usuarioRepository, medicamentoRepository);
        }

        @Test
        void deveLancarExcecaoQuandoNaoExistir() {
            UsuarioMedicamentoId id = new UsuarioMedicamentoId(idUsuario, idMedicamento);
            when(repository.findById(id)).thenReturn(Optional.empty());

            RuntimeException ex = assertThrows(RuntimeException.class,
                    () -> service.buscarPorId(idUsuario, idMedicamento));

            assertEquals("Vínculo não encontrado.", ex.getMessage());

            verify(repository).findById(id);
            verifyNoMoreInteractions(repository);
            verifyNoInteractions(usuarioRepository, medicamentoRepository);
        }
    }

    @Nested
    @DisplayName("atualizar")
    class Atualizar {

        @Test
        void deveAtualizarCamposESalvarQuandoVinculoExistir() {
            UsuarioMedicamentoId id = new UsuarioMedicamentoId(idUsuario, idMedicamento);
            UsuarioMedicamento existente = new UsuarioMedicamento();
            existente.setId(id);

            when(repository.findById(id)).thenReturn(Optional.of(existente));
            when(repository.save(any(UsuarioMedicamento.class))).thenAnswer(inv -> inv.getArgument(0));

            UsuarioMedicamento atualizado = service.atualizar(
                    idUsuario,
                    idMedicamento,
                    horarioUso,
                    frequenciaUso,
                    dosagem,
                    dataHorarioAlerta,
                    statusAlerta,
                    dataHorarioConsumo,
                    confirmacaoConsumo
            );

            assertNotNull(atualizado);
            assertEquals(horarioUso, existente.getHorarioUso());
            assertEquals(frequenciaUso, existente.getFrequenciaUso());
            assertEquals(dosagem, existente.getDosagem());
            assertEquals(dataHorarioAlerta, existente.getDataHorarioAlerta());
            assertEquals(statusAlerta, existente.getStatusAlerta());
            assertEquals(dataHorarioConsumo, existente.getDataHorarioConsumo());
            assertEquals(confirmacaoConsumo, existente.getConfirmacaoConsumo());

            verify(repository).findById(id);
            verify(repository).save(existente);
            verifyNoMoreInteractions(repository);
            verifyNoInteractions(usuarioRepository, medicamentoRepository);
        }

        @Test
        void deveLancarExcecaoQuandoVinculoNaoExistir() {
            UsuarioMedicamentoId id = new UsuarioMedicamentoId(idUsuario, idMedicamento);
            when(repository.findById(id)).thenReturn(Optional.empty());

            assertThrows(RuntimeException.class, () -> service.atualizar(
                    idUsuario,
                    idMedicamento,
                    horarioUso,
                    frequenciaUso,
                    dosagem,
                    dataHorarioAlerta,
                    statusAlerta,
                    dataHorarioConsumo,
                    confirmacaoConsumo
            ));

            verify(repository).findById(id);
            verify(repository, never()).save(any());
            verifyNoMoreInteractions(repository);
            verifyNoInteractions(usuarioRepository, medicamentoRepository);
        }
    }

    @Nested
    @DisplayName("remover")
    class Remover {

        @Test
        void deveChamarDeleteByIdQuandoVinculoExistir() {
            UsuarioMedicamentoId id = new UsuarioMedicamentoId(idUsuario, idMedicamento);
            when(repository.existsById(id)).thenReturn(true);

            service.remover(idUsuario, idMedicamento);

            verify(repository).existsById(id);
            verify(repository).deleteById(id);
            verifyNoMoreInteractions(repository);
            verifyNoInteractions(usuarioRepository, medicamentoRepository);
        }

        @Test
        void deveLancarExcecaoQuandoVinculoNaoExistir() {
            UsuarioMedicamentoId id = new UsuarioMedicamentoId(idUsuario, idMedicamento);
            when(repository.existsById(id)).thenReturn(false);

            RuntimeException ex = assertThrows(RuntimeException.class,
                    () -> service.remover(idUsuario, idMedicamento));

            assertEquals("Vínculo não encontrado.", ex.getMessage());

            verify(repository).existsById(id);
            verify(repository, never()).deleteById(any());
            verifyNoMoreInteractions(repository);
            verifyNoInteractions(usuarioRepository, medicamentoRepository);
        }
    }
}