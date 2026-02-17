package megalodonte.styles;

import javafx.scene.Node;
import javafx.scene.layout.VBox;
import megalodonte.props.ListProps;
import megalodonte.theme.Theme;
import megalodonte.utils.Utils;

import static megalodonte.styles.util.StyleUtils.getFinalBackgroundColor;

public class ListStyler<T> extends BaseStyler<ListProps<T>, ListStyler<T>> {

    private String borderColor;
    private int borderWidth = 1;
    private int borderRadius = 8;

    public ListStyler<T> borderColor(String borderColor) {
        this.borderColor = borderColor;
        return this;
    }

    public ListStyler<T> borderWidth(int borderWidth) {
        this.borderWidth = borderWidth;
        return this;
    }

    public ListStyler<T> borderRadius(int borderRadius) {
        this.borderRadius = borderRadius;
        return this;
    }

    @Override
    protected void applyTheme(Node node, ListProps<T> props, Theme theme) {
        if (!(node instanceof VBox vbox)) return;

        // Apply container styling
        applyContainerStyling(vbox, theme);
    }

    protected void applyContainerStyling(VBox vbox, Theme theme) {
        String finalBgColor = getFinalBackgroundColor(theme, borderColor);
        String finalBorderColor = borderColor != null ? borderColor : theme.colors().border();
        int finalBorderRadius = borderRadius > 0 ? borderRadius : theme.radius().md();

        Utils.updateBackgroundColor(vbox, finalBgColor);
        Utils.updateBorderWidth(vbox, borderWidth > 0 ? borderWidth : theme.border().width());
        Utils.updateBorderColor(vbox, finalBorderColor);
        Utils.updateBorderRadius(vbox, finalBorderRadius);
    }
}