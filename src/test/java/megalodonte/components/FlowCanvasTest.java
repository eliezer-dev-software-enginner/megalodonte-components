package megalodonte.components;

import javafx.scene.Scene;
import javafx.stage.Stage;
import megalodonte.base.theme.ThemeManager;
import megalodonte.theme.DefaultTheme;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class FlowCanvasTest extends ApplicationTest {
    private FlowCanvas canvas;

    @Override public void start(Stage stage) {
        ThemeManager.setTheme(new DefaultTheme());
        canvas = new FlowCanvas();
        canvas.show(List.of(
                new FlowCanvas.Node("first", "First", "COMMAND", 20, 20),
                new FlowCanvas.Node("second", "Second", "TEXT", 340, 20)));
        stage.setScene(new Scene((javafx.scene.layout.Pane) canvas.getJavaFxNode(), 800, 500));
        stage.show();
    }

    @Test void clickingAnotherActionSelectsItWithoutMovingIt() {
        AtomicReference<String> selected = new AtomicReference<>();
        AtomicReference<FlowCanvas.Move> move = new AtomicReference<>();
        interact(() -> canvas.onSelect(selected::set).onMove(move::set));

        clickOn("#flow-second");

        assertEquals("second", selected.get());
        assertNull(move.get());
    }
}
