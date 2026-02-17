package megalodonte.props;

import javafx.scene.Node;
import javafx.scene.layout.VBox;
import megalodonte.ReadableState;
import megalodonte.theme.Theme;
import megalodonte.utils.Utils;

import java.util.List;
import java.util.function.Consumer;

import static megalodonte.styles.util.StyleUtils.getFinalBackgroundColor;

public class ListProps<T> extends CommonProps {
    private List<T> items;
    private ReadableState<String> searchTerm;
    private Consumer<T> onEdit;
    private Consumer<T> onDelete;
    private boolean showSearch = true;
    private boolean showHeader = true;
    private boolean showActions = true;
    private String emptyMessage = "Nenhum item encontrado";

    public ListProps() {}

    public ListProps<T> items(List<T> items) {
        this.items = items;
        return this;
    }

    public ListProps<T> searchTerm(ReadableState<String> searchTerm) {
        this.searchTerm = searchTerm;
        return this;
    }

    public ListProps<T> onEdit(Consumer<T> onEdit) {
        this.onEdit = onEdit;
        return this;
    }

    public ListProps<T> onDelete(Consumer<T> onDelete) {
        this.onDelete = onDelete;
        return this;
    }

    public ListProps<T> showSearch(boolean showSearch) {
        this.showSearch = showSearch;
        return this;
    }

    public ListProps<T> showHeader(boolean showHeader) {
        this.showHeader = showHeader;
        return this;
    }

    public ListProps<T> showActions(boolean showActions) {
        this.showActions = showActions;
        return this;
    }

    public ListProps<T> emptyMessage(String emptyMessage) {
        this.emptyMessage = emptyMessage;
        return this;
    }


    private String borderColor;
    private int borderWidth = 1;
    private int borderRadius = 8;


    public ListProps<T> borderColor(String borderColor) {
        this.borderColor = borderColor;
        return this;
    }

    public ListProps<T> borderWidth(int borderWidth) {
        this.borderWidth = borderWidth;
        return this;
    }

    public ListProps<T> borderRadius(int borderRadius) {
        this.borderRadius = borderRadius;
        return this;
    }

    // Getters
    public List<T> getItems() { return items; }
    public ReadableState<String> getSearchTerm() { return searchTerm; }
    public Consumer<T> getOnEdit() { return onEdit; }
    public Consumer<T> getOnDelete() { return onDelete; }
    public boolean isShowSearch() { return showSearch; }
    public boolean isShowHeader() { return showHeader; }
    public boolean isShowActions() { return showActions; }
    public String getEmptyMessage() { return emptyMessage; }


    @Override
    protected void applyTheme(Node node, Props props, Theme theme) {
        super.applyTheme(node, props, theme);
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