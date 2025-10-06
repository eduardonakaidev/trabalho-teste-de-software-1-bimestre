package com.mycompany.trabalho.teste.de.software.bimestre;

public class Tarefa {
    private String id;
    private String titulo;
    private String descricao;
    private Prioridade prioridade;
    private Status status;

    public enum Prioridade {
        BAIXA, MEDIA, ALTA
    }

    public enum Status {
        PENDENTE, CONCLUIDA
    }

    public Tarefa(String id, String titulo, String descricao, Prioridade prioridade, Status status) {
        this.id = id;
        this.titulo = titulo;
        this.descricao = descricao;
        this.prioridade = prioridade;
        this.status = status;
    }

    public Tarefa() {}
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { 
        if (titulo == null || titulo.trim().isEmpty()) {
            throw new IllegalArgumentException("Título não pode ser vazio");
        }
        this.titulo = titulo; 
    }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public Prioridade getPrioridade() { return prioridade; }
    public void setPrioridade(Prioridade prioridade) { this.prioridade = prioridade; }

    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }

    @Override
    public String toString() {
        return String.format("%s - %s (%s) [%s]", id, titulo, prioridade, status);
    }
}