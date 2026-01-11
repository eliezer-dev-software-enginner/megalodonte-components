package megalodonte.props;

import megalodonte.props.CommonProps;
import megalodonte.ReadableState;

import java.util.List;
import java.util.function.Consumer;

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

    // Getters
    public List<T> getItems() { return items; }
    public ReadableState<String> getSearchTerm() { return searchTerm; }
    public Consumer<T> getOnEdit() { return onEdit; }
    public Consumer<T> getOnDelete() { return onDelete; }
    public boolean isShowSearch() { return showSearch; }
    public boolean isShowHeader() { return showHeader; }
    public boolean isShowActions() { return showActions; }
    public String getEmptyMessage() { return emptyMessage; }
}