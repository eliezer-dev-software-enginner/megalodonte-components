package megalodonte.components;

import javafx.scene.Node;
import javafx.scene.control.Pagination;

import java.util.ArrayList;
import java.util.List;

public class PaginatedTable<T> {
    private final SimpleTable<T> table;
    private final Pagination pagination;
    private final List<T> allItems = new ArrayList<>();
    private final int pageSize;

    public PaginatedTable(SimpleTable<T> table, int pageSize) {
        this.table = table;
        this.pageSize = pageSize;
        this.pagination = new Pagination();
        pagination.setPageFactory(this::createPage);
    }

    public void setItems(List<T> items) {
        allItems.clear();
        allItems.addAll(items);
        int pageCount = Math.max(1, (int) Math.ceil(allItems.size() / (double) pageSize));
        pagination.setPageCount(pageCount);
        pagination.setCurrentPageIndex(0);
    }

    private Node createPage(int pageIndex) {
        int from = pageIndex * pageSize;
        int to = Math.min(from + pageSize, allItems.size());
        table.clear();
        allItems.subList(from, to).forEach(table::addItem);
        return table.getTableView();
    }

    public Node getNode() {
        return pagination;
    }
}