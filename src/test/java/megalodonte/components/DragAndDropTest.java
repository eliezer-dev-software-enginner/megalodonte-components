package megalodonte.components;

import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

import static org.junit.jupiter.api.Assertions.*;

class DragAndDropTest extends ApplicationTest {
    @Override public void start(Stage stage) {
        stage.setScene(new Scene(new StackPane(), 240, 120));
        stage.show();
    }

    @Test void wrapsFrameworkComponentsWithoutExposingNativeNodesToConsumers() {
        interact(() -> {
            var draggable = new Draggable("new:TEXT", new Text("Texto"));
            var dropZone = new DropZone(new Text("Página")).onDrop(value -> {});
            assertNotNull(draggable.getJavaFxNode().getOnDragDetected());
            assertNotNull(dropZone.getJavaFxNode().getOnDragOver());
            assertNotNull(dropZone.getJavaFxNode().getOnDragDropped());
        });
    }

    @Test void rejectsBlankPayload() {
        interact(() -> assertThrows(IllegalArgumentException.class,
                () -> new Draggable(" ", new Text("Inválido"))));
    }
}
