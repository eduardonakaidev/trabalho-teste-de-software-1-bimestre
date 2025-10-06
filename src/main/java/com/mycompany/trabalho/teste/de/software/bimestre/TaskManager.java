package com.mycompany.trabalho.teste.de.software.bimestre;

import java.util.List;
import java.util.Optional;

public class TaskManager {
    private final TaskRepository repository;

    public TaskManager(TaskRepository repository) {
        this.repository = repository;
    }

    public Tarefa cadastrarTarefa(String titulo, String descricao, Tarefa.Prioridade prioridade) {
        if (titulo == null || titulo.trim().isEmpty()) {
            throw new IllegalArgumentException("Título não pode ser vazio");
        }
        
        Tarefa novaTarefa = new Tarefa();
        novaTarefa.setTitulo(titulo.trim());
        novaTarefa.setDescricao(descricao != null ? descricao.trim() : "");
        novaTarefa.setPrioridade(prioridade);
        novaTarefa.setStatus(Tarefa.Status.PENDENTE);
        
        return repository.salvar(novaTarefa);
    }

    public List<Tarefa> listarTarefas() {
        return repository.findAll();
    }

    public void atualizarStatus(String id, Tarefa.Status novoStatus) {
        Optional<Tarefa> tarefaOpt = repository.findById(id);
        if (tarefaOpt.isPresent()) {
            Tarefa tarefa = tarefaOpt.get();
            tarefa.setStatus(novoStatus);
            repository.salvar(tarefa);
        } else {
            throw new IllegalArgumentException("Tarefa não encontrada com ID: " + id);
        }
    }

    public void excluirTarefa(String id) {
        if (!repository.existsById(id)) {
            throw new IllegalArgumentException("Tarefa não encontrada com ID: " + id);
        }
        repository.deleteById(id);
    }

    public Optional<Tarefa> buscarPorId(String id) {
        return repository.findById(id);
    }
}