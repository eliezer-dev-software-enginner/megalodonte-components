package megalodonte.components;

import javafx.application.Platform;
import javafx.event.Event;
import javafx.scene.Scene;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import megalodonte.base.theme.ThemeManager;
import megalodonte.theme.DefaultTheme;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

class DiagramCanvasConnectionTest {
    @Test void outputCanConnectOnlyToInputAndHandlesRespectNodeRoles() throws Exception {
        assumeTrue(System.getenv("DISPLAY") != null);
        var started = new CountDownLatch(1);
        try { Platform.startup(started::countDown); } catch (IllegalStateException alreadyStarted) { started.countDown(); }
        assertTrue(started.await(10, TimeUnit.SECONDS));
        var check = new FutureTask<Void>(() -> {
            ThemeManager.setTheme(new DefaultTheme());
            var canvas = new DiagramCanvas(); var result = new AtomicReference<DiagramCanvas.Connection>();
            canvas.onConnect(result::set);
            canvas.show(List.of(new DiagramCanvas.Node("start", "Start", "INÍCIO", 20, 50, false, true),
                    new DiagramCanvas.Node("java", "Java", "JAVA", 300, 50, true, true),
                    new DiagramCanvas.Node("end", "End", "RESPOSTA", 570, 50, true, false)), List.of());
            var surface = (Pane) canvas.getJavaFxNode();
            new Scene(surface, 800, 300);
            assertNull(surface.lookup("#diagram-input-start"));
            assertNull(surface.lookup("#diagram-output-end"));
            assertNotNull(surface.lookup("#diagram-input-java"));
            var output = surface.lookup("#diagram-output-start");
            Event.fireEvent(output, mouse(MouseEvent.MOUSE_PRESSED, 170, 90, true));
            Event.fireEvent(output, mouse(MouseEvent.MOUSE_DRAGGED, 300, 90, true));
            Event.fireEvent(output, mouse(MouseEvent.MOUSE_RELEASED, 300, 90, false));
            assertEquals(new DiagramCanvas.Connection("start", "java"), result.get());
            result.set(null);
            Event.fireEvent(output, mouse(MouseEvent.MOUSE_PRESSED, 170, 90, true));
            Event.fireEvent(output, mouse(MouseEvent.MOUSE_RELEASED, 450, 90, false));
            assertNull(result.get(), "Uma saída não pode ser ligada a outra saída");
            var input = surface.lookup("#diagram-input-java");
            Event.fireEvent(input, mouse(MouseEvent.MOUSE_PRESSED, 300, 90, true));
            Event.fireEvent(input, mouse(MouseEvent.MOUSE_RELEASED, 570, 90, false));
            assertNull(result.get(), "Uma entrada não inicia conexões");
            return null;
        });
        Platform.runLater(check); check.get(10, TimeUnit.SECONDS);
    }

    private static MouseEvent mouse(javafx.event.EventType<MouseEvent> type, double x, double y, boolean down) {
        return new MouseEvent(type, x, y, x, y, MouseButton.PRIMARY, 1, false, false, false, false,
                down, false, false, false, false, true, null);
    }
}
