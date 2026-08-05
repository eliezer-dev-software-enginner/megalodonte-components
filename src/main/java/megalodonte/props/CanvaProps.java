package megalodonte.props;

import javafx.scene.Node;
import javafx.scene.layout.Pane;
import megalodonte.base.theme.ThemeInterface;

import static megalodonte.styles.util.StyleUtils.applyBackgroundStyling;
import static megalodonte.styles.util.StyleUtils.applyBorderStyling;

/**
 * Props for {@link megalodonte.components.layout_components.Canva}. Shares the
 * common layout knobs (width/height, padding, onClick) with every other
 * {@link LayoutProps} subclass, plus background/border color. {@code spacingOf(...)},
 * while inherited, has no effect here — a {@link Pane} positions children
 * absolutely (via {@code layoutX}/{@code layoutY}), it doesn't arrange them in a
 * line the way {@code VBox}/{@code HBox} do, so there's no "gap between items"
 * to apply.
 */
public class CanvaProps extends LayoutProps<CanvaProps> {
    protected String bgColor;
    protected String borderColor;
    protected int borderWidth;
    protected int borderRadius;

    public CanvaProps bgColor(String bgColor) {
        this.bgColor = bgColor;
        return this;
    }

    public CanvaProps borderColor(String borderColor) {
        this.borderColor = borderColor;
        return this;
    }

    public CanvaProps borderWidth(int borderWidth) {
        this.borderWidth = borderWidth;
        return this;
    }

    public CanvaProps borderRadius(int borderRadius) {
        this.borderRadius = borderRadius;
        return this;
    }

    @Override
    protected void applyTheme(Node node, Props props, ThemeInterface theme) {
        if (!(node instanceof Pane)) return;

        applyBaseLayout(node);

        if (bgColor != null) {
            applyBackgroundStyling(node, theme, bgColor);
        }

        if (borderColor != null || borderWidth > 0) {
            applyBorderStyling(node, theme, borderColor, borderWidth, borderRadius);
        }
    }
}
