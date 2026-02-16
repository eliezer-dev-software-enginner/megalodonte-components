package megalodonte.props;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.VBox;

public class ColumnProps extends LayoutProps<ColumnProps> {
    private enum Alignment {CENTER_HORIZONTALLY, CENTER_VERTICALLY}
    private Alignment alignment;

    public ColumnProps centerHorizontally() {
        alignment = Alignment.CENTER_HORIZONTALLY;
        return this;
    }

    public ColumnProps centerVertically() {
        alignment = Alignment.CENTER_VERTICALLY;
        return this;
    }

    @Override
    public void apply(Node node) {
        if (node instanceof VBox vBox) {
            applyBaseLayout(node);

            if (spacingUnits > 0) {
                vBox.setSpacing(spacingUnits);
            }

            if (alignment != null) {
                if (alignment.equals(Alignment.CENTER_HORIZONTALLY)) {
                    vBox.setAlignment(Pos.TOP_CENTER);
                }
                if (alignment.equals(Alignment.CENTER_VERTICALLY)) {
                    vBox.setAlignment(Pos.CENTER);
                }
            }
        }
    }
}
