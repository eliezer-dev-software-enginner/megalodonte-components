package megalodonte.components;

import javafx.scene.AccessibleRole;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

class ClickableTest extends ApplicationTest {
    private StackPane root;

    @Override public void start(Stage stage) {
        root = new StackPane();
        stage.setScene(new Scene(root, 240, 120));
        stage.show();
    }

    @Test void activatesWithKeyboardWhenEnabled() {
        var activations = new AtomicInteger();
        var clickable = new Clickable(new Text("Open")).onClick(activations::incrementAndGet);
        var node = clickable.getJavaFxNode();
        interact(() -> {
            root.getChildren().add(node);
            assertTrue(node.isFocusTraversable());
            assertEquals(AccessibleRole.BUTTON, node.getAccessibleRole());
            node.fireEvent(key(KeyCode.ENTER));
            node.fireEvent(key(KeyCode.SPACE));
        });
        assertEquals(2, activations.get());
    }

    private static KeyEvent key(KeyCode code) {
        return new KeyEvent(KeyEvent.KEY_PRESSED, "", "", code, false, false, false, false);
    }
}
