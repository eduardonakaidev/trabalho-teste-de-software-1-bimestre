package com.mycompany.trabalho.teste.de.software.bimestre;

import java.util.List;
import java.util.Optional;

public interface TaskRepository {
    Tarefa salvar(Tarefa tarefa);
    List<Tarefa> findAll();
    Optional<Tarefa> findById(String id);
    void deleteById(String id);
    boolean existsById(String id);
}