package megalodonte.components;

import javafx.scene.input.TransferMode;
import javafx.scene.layout.StackPane;
import megalodonte.base.components.Component;

import java.util.Objects;
import java.util.function.Consumer;

/** Receives string payloads emitted by {@link Draggable}. */
public final class DropZone extends Component {
    private Consumer<String> callback = ignored -> {};

    public DropZone(Component content) {
        super(new StackPane(Objects.requireNonNull(content, "content").getJavaFxNode()));
        node.setOnDragOver(event -> {
            if (event.getGestureSource() != node && event.getDragboard().hasContent(DragPayload.FORMAT)) {
                event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
            }
            event.consume();
        });
        node.setOnDragDropped(event -> {
            Object payload = event.getDragboard().getContent(DragPayload.FORMAT);
            boolean accepted = payload instanceof String value && !value.isBlank();
            if (accepted) callback.accept((String) payload);
            event.setDropCompleted(accepted);
            event.consume();
        });
    }

    public DropZone onDrop(Consumer<String> callback) {
        this.callback = Objects.requireNonNull(callback, "callback");
        return this;
    }
}
