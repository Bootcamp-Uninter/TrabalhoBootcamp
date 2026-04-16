package br.uninter.medalerta.service;

import br.uninter.medalerta.model.Medicamento;
import br.uninter.medalerta.repository.MedicamentoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MedicamentoService {

    private final MedicamentoRepository repository;

    public MedicamentoService(MedicamentoRepository repository) {
        this.repository = repository;
    }

    public Medicamento salvar(Medicamento Medicamento) {
        return repository.save(Medicamento);
    }

    public List<Medicamento> listarTodos() {
        return repository.findAll();
    }

    public Medicamento buscarPorId(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Medicamento não encontrado: " + id));
    }

    public Medicamento atualizar(Integer id, Medicamento novoMedicamento) {
        Medicamento existente = buscarPorId(id);
        
        if (novoMedicamento.getNomeComercial() != null && !novoMedicamento.getNomeComercial().isEmpty()) {
            existente.setNomeComercial(novoMedicamento.getNomeComercial());
        }
        if (novoMedicamento.getNomeGenerico() != null && !novoMedicamento.getNomeGenerico().isEmpty()) {
            existente.setNomeGenerico(novoMedicamento.getNomeGenerico());
        }
        if (novoMedicamento.getQuantidade() != null) {
            existente.setQuantidade(novoMedicamento.getQuantidade());
        }
        if (novoMedicamento.getFormaUso() != null && !novoMedicamento.getFormaUso().isEmpty()) {
            existente.setFormaUso(novoMedicamento.getFormaUso());
        }
        if (novoMedicamento.getObservacao() != null && !novoMedicamento.getObservacao().isEmpty()) {
            existente.setObservacao(novoMedicamento.getObservacao());
        }

        return repository.save(existente);
    }

    public void deletar(Integer id) {
        Medicamento Medicamento = buscarPorId(id);
        repository.delete(Medicamento);
    }
}