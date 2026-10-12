package megalodonte.components;

import javafx.beans.value.ChangeListener;
import javafx.scene.control.*;
import megalodonte.base.components.Component;
import megalodonte.base.theme.ThemeManager;
import java.util.List;
import java.util.Objects;
import java.util.function.IntConsumer;

/** Virtualized, keyboard-accessible data list. Platform controls stay inside the framework. */
public final class DataList extends Component implements AutoCloseable {
    public record Item(String label, String detail, String badge) {
        public Item { Objects.requireNonNull(label); Objects.requireNonNull(detail); Objects.requireNonNull(badge); }
    }
    private final ListView<Item> list;
    private final ChangeListener<Number> selectionListener;
    private IntConsumer onSelect = index -> {};
    private boolean updating;
    public DataList() {
        super(new ListView<Item>());
        @SuppressWarnings("unchecked") var control = (ListView<Item>) node;
        list = control;
        list.setFixedCellSize(54);
        list.getStyleClass().add("megalodonte-data-list");
        list.getStylesheets().add(getClass().getResource("/desktop-list.css").toExternalForm());
        var colors = ThemeManager.theme().colors();
        var placeholder = new Label("Nenhum item");
        placeholder.setStyle("-fx-text-fill: " + colors.textSecondary() + ";");
        list.setPlaceholder(placeholder);
        list.setStyle("-fx-background-color: " + colors.surface() + "; -fx-control-inner-background: " + colors.surface() + "; -fx-text-fill: " + colors.textPrimary() + "; -fx-font-size: 13px; -fx-focus-color: transparent; -fx-faint-focus-color: transparent; -fx-background-insets: 0; -fx-border-color: " + colors.border() + ";");
        list.setCellFactory(view -> new ListCell<>() {
            { setTextOverrun(OverrunStyle.ELLIPSIS); setPrefWidth(0); }
            @Override protected void updateItem(Item item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : (item.badge().isBlank() ? "" : item.badge() + "  ") + item.label() + (item.detail().isBlank() ? "" : "\n" + item.detail()));
                setAccessibleText(getText());
                setGraphic(null);
                setStyle("-fx-background-color: " + (isSelected() ? colors.hover() : colors.surface()) + "; -fx-text-fill: " + colors.textPrimary() + ";");
            }
        });
        selectionListener = (observable, previous, next) -> { if (!updating) onSelect.accept(next.intValue()); };
        list.getSelectionModel().selectedIndexProperty().addListener(selectionListener);
    }
    /** Atomic replacement preserves the current index when possible and emits no business action. */
    public void setItems(List<Item> items) {
        var checked = List.copyOf(items);
        int selected = selectedIndex();
        updating = true;
        try { list.getItems().setAll(checked); list.getSelectionModel().clearSelection(); if (selected >= 0 && selected < checked.size()) list.getSelectionModel().select(selected); }
        finally { updating = false; }
    }
    public void select(int index) {
        updating = true;
        try { if (index < 0 || index >= list.getItems().size()) list.getSelectionModel().clearSelection(); else { list.getSelectionModel().select(index); list.scrollTo(index); } }
        finally { updating = false; }
    }
    public int selectedIndex() { return list.getSelectionModel().getSelectedIndex(); }
    public List<Item> items() { return List.copyOf(list.getItems()); }
    public void onSelect(IntConsumer callback) { onSelect = Objects.requireNonNull(callback); }
    @Override public void close() {
        list.getSelectionModel().selectedIndexProperty().removeListener(selectionListener);
        onSelect = index -> {};
        list.getItems().clear();
        list.setCellFactory(null);
    }
}
