package megalodonte.styles;

import javafx.scene.Node;
import javafx.scene.layout.VBox;
import megalodonte.props.ColumnProps;
import megalodonte.theme.Theme;

public class ColumnStyler extends BaseStyler<ColumnProps, ColumnStyler> {

    @Override
    protected void applyTheme(Node node, ColumnProps props, Theme theme) {
        if (!(node instanceof VBox)) return;
    }
}
