package megalodonte.components;

import javafx.scene.input.ClipboardContent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.StackPane;
import megalodonte.base.components.Component;

import java.util.Objects;

/** Wraps a framework component as a typed drag source without exposing JavaFX to applications. */
public final class Draggable extends Component {
    public Draggable(String payload, Component content) {
        super(new StackPane(Objects.requireNonNull(content, "content").getJavaFxNode()));
        String value = Objects.requireNonNull(payload, "payload");
        if (value.isBlank()) throw new IllegalArgumentException("payload must not be blank");
        node.setOnDragDetected(event -> {
            var board = node.startDragAndDrop(TransferMode.COPY_OR_MOVE);
            var clipboard = new ClipboardContent();
            clipboard.put(DragPayload.FORMAT, value);
            board.setContent(clipboard);
            event.consume();
        });
    }
}
