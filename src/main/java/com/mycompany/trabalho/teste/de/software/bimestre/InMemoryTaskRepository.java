package com.mycompany.trabalho.teste.de.software.bimestre;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class InMemoryTaskRepository implements TaskRepository {
    private final ConcurrentHashMap<String, Tarefa> database = new ConcurrentHashMap<>();
    private final AtomicLong idCounter = new AtomicLong(1);

    @Override
    public Tarefa salvar(Tarefa tarefa) {
        if (tarefa.getId() == null || tarefa.getId().isEmpty()) {
            String newId = String.valueOf(idCounter.getAndIncrement());
            tarefa.setId(newId);
        }
        database.put(tarefa.getId(), tarefa);
        return tarefa;
    }

    @Override
    public List<Tarefa> findAll() {
        return new ArrayList<>(database.values());
    }

    @Override
    public Optional<Tarefa> findById(String id) {
        return Optional.ofNullable(database.get(id));
    }

    @Override
    public void deleteById(String id) {
        database.remove(id);
    }

    @Override
    public boolean existsById(String id) {
        return database.containsKey(id);
    }
}