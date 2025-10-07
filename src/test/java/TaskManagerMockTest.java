
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.mycompany.trabalho.teste.de.software.bimestre.TaskManager;
import com.mycompany.trabalho.teste.de.software.bimestre.TaskRepository;
import com.mycompany.trabalho.teste.de.software.bimestre.Tarefa;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskManagerMockTest {

    @Mock
    private TaskRepository repository;

    @InjectMocks
    private TaskManager taskManager;

    @Test
    void testCRUD() {
        Tarefa tarefaMock = new Tarefa("1", "Estudar Java", "Estudar para prova",
                Tarefa.Prioridade.ALTA, Tarefa.Status.PENDENTE);

        when(repository.salvar(any(Tarefa.class))).thenReturn(tarefaMock);

        Tarefa tarefaCriada = taskManager.cadastrarTarefa("Estudar Java", "Estudar para prova",
                Tarefa.Prioridade.ALTA);

        assertNotNull(tarefaCriada);
        verify(repository, times(1)).salvar(any(Tarefa.class));

        List<Tarefa> tarefasMock = Arrays.asList(
                new Tarefa("1", "Tarefa 1", "Desc 1", Tarefa.Prioridade.ALTA, Tarefa.Status.PENDENTE),
                new Tarefa("2", "Tarefa 2", "Desc 2", Tarefa.Prioridade.MEDIA, Tarefa.Status.CONCLUIDA)
        );

        when(repository.findAll()).thenReturn(tarefasMock);

        List<Tarefa> tarefas = taskManager.listarTarefas();

        assertEquals(2, tarefas.size());
        verify(repository, times(1)).findAll();

        when(repository.findById("1")).thenReturn(Optional.of(tarefaMock));

        Optional<Tarefa> tarefaEncontrada = taskManager.buscarPorId("1");

        assertTrue(tarefaEncontrada.isPresent());
        assertEquals("1", tarefaEncontrada.get().getId());
        verify(repository, times(1)).findById("1");

        // CORREÇÃO: Configurar o mock novamente para a próxima operação
        when(repository.findById("1")).thenReturn(Optional.of(tarefaMock));
        when(repository.salvar(any(Tarefa.class))).thenReturn(tarefaMock);

        taskManager.atualizarStatus("1", Tarefa.Status.CONCLUIDA);

        // CORREÇÃO: Agora verificamos 2 chamadas totais (1 da busca + 1 da atualização)
        verify(repository, times(2)).findById("1");
        verify(repository, times(2)).salvar(any(Tarefa.class));

        when(repository.existsById("1")).thenReturn(true);
        doNothing().when(repository).deleteById("1");

        taskManager.excluirTarefa("1");

        verify(repository, times(1)).existsById("1");
        verify(repository, times(1)).deleteById("1");
    }

    @Test
    void testErrosEExcecoes() {
        assertThrows(IllegalArgumentException.class, () -> {
            taskManager.cadastrarTarefa("", "Descrição", Tarefa.Prioridade.ALTA);
        });

        when(repository.findById("999")).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> {
            taskManager.atualizarStatus("999", Tarefa.Status.CONCLUIDA);
        });

        when(repository.existsById("999")).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> {
            taskManager.excluirTarefa("999");
        });

        when(repository.salvar(any(Tarefa.class)))
                .thenThrow(new RuntimeException("Erro de banco de dados"));

        assertThrows(RuntimeException.class, () -> {
            taskManager.cadastrarTarefa("Tarefa Teste", "Descrição", Tarefa.Prioridade.MEDIA);
        });
        when(repository.findById("999")).thenReturn(Optional.empty());

        Optional<Tarefa> tarefa = taskManager.buscarPorId("999");
        assertTrue(tarefa.isEmpty());
    }

    @Test
    void testChamadasCorretasAosMetodosDoRepositorio() {
        Tarefa tarefaMock = new Tarefa("1", "Tarefa Teste", "Descrição",
                Tarefa.Prioridade.ALTA, Tarefa.Status.PENDENTE);

        when(repository.salvar(any(Tarefa.class))).thenReturn(tarefaMock);
        taskManager.cadastrarTarefa("Tarefa Teste", "Descrição", Tarefa.Prioridade.ALTA);
        verify(repository).salvar(any(Tarefa.class));

        when(repository.findAll()).thenReturn(Arrays.asList(tarefaMock));
        taskManager.listarTarefas();
        verify(repository).findAll();
        when(repository.findById("1")).thenReturn(Optional.of(tarefaMock));
        taskManager.buscarPorId("1");
        verify(repository).findById("1");

        when(repository.existsById("1")).thenReturn(true);
        doNothing().when(repository).deleteById("1");
        taskManager.excluirTarefa("1");
        verify(repository).existsById("1");
        verify(repository).deleteById("1");
    }

    @Test
    void testValidacoesDeNegocio() {
        assertThrows(IllegalArgumentException.class, () -> {
            taskManager.cadastrarTarefa(null, "Descrição", Tarefa.Prioridade.ALTA);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            taskManager.cadastrarTarefa("   ", "Descrição", Tarefa.Prioridade.ALTA);
        });

        Tarefa tarefaMock = new Tarefa("1", "Título", "", Tarefa.Prioridade.ALTA, Tarefa.Status.PENDENTE);
        when(repository.salvar(any(Tarefa.class))).thenReturn(tarefaMock);

        Tarefa tarefa = taskManager.cadastrarTarefa("Título", null, Tarefa.Prioridade.ALTA);
        assertNotNull(tarefa);
        verify(repository).salvar(any(Tarefa.class));
    }
}
