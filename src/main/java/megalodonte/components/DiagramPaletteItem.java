package megalodonte.components;

import javafx.scene.control.Button;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.TransferMode;
import megalodonte.base.components.Component;
import megalodonte.props.ButtonProps;

/** A themed component palette entry that can be dropped on a DiagramCanvas. */
public final class DiagramPaletteItem extends Component {
    public DiagramPaletteItem(String label, String type) {
        super(new Button(label), new ButtonProps());
        Button button = (Button) node;
        button.setMaxWidth(Double.MAX_VALUE);
        button.setOnDragDetected(event -> {
            var board = button.startDragAndDrop(TransferMode.COPY);
            var content = new ClipboardContent();
            content.put(DiagramCanvas.COMPONENT_TYPE, type);
            board.setContent(content);
            event.consume();
        });
    }
}
