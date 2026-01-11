package megalodonte.styles;

import javafx.scene.Node;
import javafx.scene.layout.HBox;
import megalodonte.props.RowProps;
import megalodonte.theme.Theme;

public class RowStyler extends BaseStyler<RowProps, RowStyler> {

    @Override
    protected void applyTheme(Node node, RowProps props, Theme theme) {
        if (!(node instanceof HBox)) return;
        
        // Apply all common theme-aware styling
        applyBackgroundStyling(node, theme);
        applyBorderStyling(node, theme);
    }
}
