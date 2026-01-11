package megalodonte.styles;

import javafx.scene.Node;
import javafx.scene.layout.VBox;
import megalodonte.props.ListProps;
import megalodonte.theme.Theme;
import megalodonte.utils.Utils;

public class ListStyler<T> extends BaseStyler<ListProps<T>, ListStyler<T>> {

    private String headerBgColor;
    private String headerTextColor;
    private String rowHoverBgColor;
    private String borderColor;
    private String searchBgColor;
    private String emptyTextColor;
    private int borderWidth = 1;
    private int borderRadius = 8;

    public ListStyler<T> headerBgColor(String headerBgColor) {
        this.headerBgColor = headerBgColor;
        return this;
    }

    public ListStyler<T> headerTextColor(String headerTextColor) {
        this.headerTextColor = headerTextColor;
        return this;
    }

    public ListStyler<T> rowHoverBgColor(String rowHoverBgColor) {
        this.rowHoverBgColor = rowHoverBgColor;
        return this;
    }

    public ListStyler<T> borderColor(String borderColor) {
        this.borderColor = borderColor;
        return this;
    }

    public ListStyler<T> searchBgColor(String searchBgColor) {
        this.searchBgColor = searchBgColor;
        return this;
    }

    public ListStyler<T> emptyTextColor(String emptyTextColor) {
        this.emptyTextColor = emptyTextColor;
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
        String finalBgColor = getFinalBackgroundColor(theme);
        String finalBorderColor = borderColor != null ? borderColor : theme.colors().border();
        int finalBorderRadius = borderRadius > 0 ? borderRadius : theme.radius().md();

        Utils.updateBackgroundColor(vbox, finalBgColor);
        Utils.updateBorderWidth(vbox, borderWidth > 0 ? borderWidth : theme.border().width());
        Utils.updateBorderColor(vbox, finalBorderColor);
        Utils.updateBorderRadius(vbox, finalBorderRadius);
    }

    // Getters for use by component
    public String getHeaderBgColor() { return headerBgColor; }
    public String getHeaderTextColor() { return headerTextColor; }
    public String getRowHoverBgColor() { return rowHoverBgColor; }
    public String getBorderColor() { return borderColor; }
    public String getSearchBgColor() { return searchBgColor; }
    public String getEmptyTextColor() { return emptyTextColor; }
    public int getBorderWidth() { return borderWidth; }
    public int getBorderRadius() { return borderRadius; }
}