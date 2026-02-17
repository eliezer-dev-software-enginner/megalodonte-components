package megalodonte.styles;

import javafx.scene.Node;
import javafx.scene.layout.HBox;
import megalodonte.props.RowProps;
import megalodonte.theme.Theme;

import static megalodonte.styles.util.StyleUtils.applyBackgroundStyling;
import static megalodonte.styles.util.StyleUtils.applyBorderStyling;

public class RowStyler extends BaseStyler<RowProps, RowStyler> {
    protected String bgColor;

    public RowStyler bgColor(String bgColor) {
        this.bgColor = bgColor;
        return  this;
    }

    @Override
    protected void applyTheme(Node node, RowProps props, Theme theme) {
        if (!(node instanceof HBox)) return;

        // Apply all common theme-aware styling
        applyBackgroundStyling(node, theme, bgColor);
    }
}
