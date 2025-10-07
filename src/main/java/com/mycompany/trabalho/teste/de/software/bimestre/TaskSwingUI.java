package com.mycompany.trabalho.teste.de.software.bimestre;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class TaskSwingUI extends JFrame {
    private final TaskManager taskManager;
    private DefaultListModel<String> listModel;
    private JList<String> taskList;
    private JTextField titleField;
    private JTextArea descriptionArea;
    private JComboBox<String> priorityComboBox;

    public TaskSwingUI() {
        this.taskManager = new TaskManager(new InMemoryTaskRepository());
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Gerenciador de Tarefas");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        initializeComponents();
        
        add(createInputPanel(), BorderLayout.NORTH);
        add(createListPanel(), BorderLayout.CENTER);
        add(createButtonPanel(), BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null);
        setSize(600, 500);
    }

    private void initializeComponents() {
        titleField = new JTextField(20);
        titleField.setName("titleField");
        
        descriptionArea = new JTextArea(3, 20);
        descriptionArea.setLineWrap(true);
        descriptionArea.setName("descriptionArea");
        
        priorityComboBox = new JComboBox<>(new String[]{"BAIXA", "MEDIA", "ALTA"});
        priorityComboBox.setName("priorityComboBox");
        
        listModel = new DefaultListModel<>();
        taskList = new JList<>(listModel);
        taskList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        taskList.setName("taskList");
    }

    private JPanel createInputPanel() {
        JPanel panel = new JPanel(new GridLayout(0, 1, 5, 5));
        panel.setBorder(BorderFactory.createTitledBorder("Nova Tarefa"));

        panel.add(new JLabel("Título: *"));
        panel.add(titleField);

        panel.add(new JLabel("Descrição:"));
        JScrollPane scrollPane = new JScrollPane(descriptionArea);
        panel.add(scrollPane);

        panel.add(new JLabel("Prioridade:"));
        panel.add(priorityComboBox);

        return panel;
    }

    private JPanel createListPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Tarefas Cadastradas"));

        JScrollPane listScrollPane = new JScrollPane(taskList);
        panel.add(listScrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout());

        JButton addButton = new JButton("Adicionar Tarefa");
        addButton.setName("addButton");
        addButton.addActionListener(this::adicionarTarefa);

        JButton deleteButton = new JButton("Excluir Tarefa");
        deleteButton.setName("deleteButton");
        deleteButton.addActionListener(this::excluirTarefa);

        panel.add(addButton);
        panel.add(deleteButton);

        return panel;
    }

    private void adicionarTarefa(ActionEvent e) {
        try {
            String titulo = titleField.getText().trim();
            String descricao = descriptionArea.getText().trim();
            String prioridadeStr = (String) priorityComboBox.getSelectedItem();

            if (titulo.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Título não pode ser vazio", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Tarefa.Prioridade prioridade = Tarefa.Prioridade.valueOf(prioridadeStr);
            
            Tarefa tarefa = taskManager.cadastrarTarefa(titulo, descricao, prioridade);
            listModel.addElement(tarefa.getId() + " - " + tarefa.getTitulo() + " - " + 
                               tarefa.getDescricao() + " (" + prioridadeStr + ") - " + tarefa.getStatus());
            
            titleField.setText("");
            descriptionArea.setText("");
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void excluirTarefa(ActionEvent e) {
        int selectedIndex = taskList.getSelectedIndex();
        if (selectedIndex == -1) {
            JOptionPane.showMessageDialog(this, "Selecione uma tarefa para excluir!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, 
            "Deseja realmente excluir a tarefa selecionada?",
            "Confirmar Exclusão", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                String selectedValue = listModel.get(selectedIndex);
                String taskId = selectedValue.split(" - ")[0];
                
                taskManager.excluirTarefa(taskId);
                listModel.remove(selectedIndex);
                
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro ao excluir: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getLookAndFeel());
            } catch (Exception e) {
                e.printStackTrace();
            }
            new TaskSwingUI().setVisible(true);
        });
    }
}