package megalodonte.components;

import javafx.application.Platform;
import javafx.event.Event;
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

class DiagramCanvasEdgeTest {
    @Test void clickingConnectionSelectsItsIdentifier() throws Exception {
        assumeTrue(System.getenv("DISPLAY") != null);
        var started = new CountDownLatch(1);
        try { Platform.startup(started::countDown); } catch (IllegalStateException alreadyStarted) { started.countDown(); }
        assertTrue(started.await(10, TimeUnit.SECONDS));
        var check = new FutureTask<Void>(() -> {
            ThemeManager.setTheme(new DefaultTheme());
            var canvas = new DiagramCanvas(); var selected = new AtomicReference<String>();
            canvas.onEdgeSelect(selected::set);
            canvas.show(List.of(new DiagramCanvas.Node("a", "A", "ITEM", 20, 50),
                    new DiagramCanvas.Node("b", "B", "JAVA", 330, 50)),
                    List.of(new DiagramCanvas.Edge("link", "a", "b", "IF")));
            var hit = ((Pane) canvas.getJavaFxNode()).getChildren().stream()
                    .filter(node -> "diagram-edge-link".equals(node.getId())).findFirst().orElseThrow();
            Event.fireEvent(hit, new MouseEvent(MouseEvent.MOUSE_CLICKED, 0, 0, 0, 0,
                    MouseButton.PRIMARY, 1, false, false, false, false,
                    false, false, false, false, false, true, null));
            assertEquals("link", selected.get());
            return null;
        });
        Platform.runLater(check); check.get(10, TimeUnit.SECONDS);
    }
}
