package megalodonte.props;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import megalodonte.theme.Theme;

import static megalodonte.styles.util.StyleUtils.applyBackgroundStyling;

public class RowProps extends LayoutProps<RowProps> {
    protected String bgColor;

    public RowProps bgColor(String bgColor) {
        this.bgColor = bgColor;
        return this;
    }
    @Override
    protected void applyTheme(Node node, Props props, Theme theme) {
        if (node instanceof HBox hBox) {
            applyBaseLayout(node);

            if (spacingUnits > 0) {
                hBox.setSpacing(spacingUnits);
            }

            if (alignment == Alignment.BOTTOM_VERTICALLY) {
                hBox.setAlignment(Pos.BOTTOM_LEFT);
            }
            if (alignment == Alignment.CENTER_HORIZONTALLY) {
                hBox.setAlignment(Pos.CENTER);
            }
            if (alignment == Alignment.CENTER_VERTICALLY) {
                hBox.setAlignment(Pos.CENTER_LEFT);
            }

            // Apply all common theme-aware styling
            applyBackgroundStyling(node, theme, bgColor);
        }
    }

    private enum Alignment {BOTTOM_VERTICALLY, CENTER_HORIZONTALLY, CENTER_VERTICALLY}
    private Alignment alignment;

    public RowProps bottomVertically() {
        alignment = Alignment.BOTTOM_VERTICALLY;
        return this;
    }

    public RowProps centerHorizontally() {
        alignment = Alignment.CENTER_HORIZONTALLY;
        return this;
    }

    public RowProps centerVertically() {
        alignment = Alignment.CENTER_VERTICALLY;
        return this;
    }
}
