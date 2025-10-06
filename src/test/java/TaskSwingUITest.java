import org.assertj.swing.core.BasicRobot;
import org.assertj.swing.core.Robot;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.mycompany.trabalho.teste.de.software.bimestre.TaskSwingUI;

import java.awt.*;

public class TaskSwingUITest {

    private FrameFixture window;

    @Before
    public void setUp() {
        TaskSwingUI frame = GuiActionRunner.execute(() -> new TaskSwingUI());
        Robot robot = BasicRobot.robotWithCurrentAwtHierarchy();
        robot.settings().delayBetweenEvents(100);
        window = new FrameFixture(robot, frame);
        window.show();
        window.resizeTo(new Dimension(600, 500));
    }

    @After
    public void tearDown() {
        if (window != null) {
            window.cleanUp();
        }
    }

    @Test
    public void testAdicionarTarefaComTituloEDescricaoDeveAparecerNaLista() {
        window.textBox("titleField").enterText("test1");
        window.textBox("descriptionArea").enterText("description1");
        window.comboBox("priorityComboBox").selectItem("ALTA");
        window.button("addButton").click();
        window.list("taskList").requireItemCount(1);
    }

    @Test
    public void testRemoverItemDaLista() {
        window.textBox("titleField").enterText("test2");
        window.textBox("descriptionArea").enterText("description2");
        window.button("addButton").click();
        window.list("taskList").selectItem(0);
        window.button("deleteButton").click();
        window.optionPane().yesButton().click();
        window.list("taskList").requireItemCount(0);
    }

    @Test
    public void TesteValidacaoNaoPermitirCadastroDeTarefaSemTitulo() {
        window.textBox("titleField").enterText("");
        window.textBox("descriptionArea").enterText("description3");
        window.button("addButton").click();
        window.optionPane().requireMessage("Título não pode ser vazio");
        window.optionPane().okButton().click();
    }

    @Test
    public void testComboBoxDePrioridadeFuncionaCorretamente() {
        window.comboBox("priorityComboBox").requireItemCount(3);
        window.comboBox("priorityComboBox").selectItem("BAIXA");
        window.comboBox("priorityComboBox").selectItem("MEDIA");
        window.comboBox("priorityComboBox").selectItem("ALTA");
    }
}