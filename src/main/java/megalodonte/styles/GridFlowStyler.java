package megalodonte.styles;

import javafx.scene.Node;
import javafx.scene.layout.TilePane;
import megalodonte.props.GridFlowProps;
import megalodonte.theme.Theme;

public class GridFlowStyler extends BaseStyler<GridFlowProps, GridFlowStyler> {

    @Override
    protected void applyTheme(Node node, GridFlowProps props, Theme theme) {
        if (!(node instanceof TilePane)) return;
        
        // Apply background styling
        applyBackgroundStyling(node, theme);
    }
}
