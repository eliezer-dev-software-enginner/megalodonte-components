package megalodonte.components;

import javafx.scene.control.*;
import javafx.scene.input.*;
import megalodonte.base.components.Component;
import megalodonte.base.theme.ThemeManager;
import java.util.Arrays;
import java.util.Objects;

/** Virtualized unified diff with line selection, keyboard navigation and clipboard support. */
public final class DiffView extends Component implements AutoCloseable {
    public enum Kind { HEADER, HUNK, ADDED, REMOVED, CONTEXT }
    private final ListView<String> list;
    private final ContextMenu menu;
    private String text = "";
    public DiffView() {
        super(new ListView<String>());
        @SuppressWarnings("unchecked") var control = (ListView<String>) node;
        list = control;
        list.setFixedCellSize(23);
        list.getStyleClass().add("megalodonte-diff-view");
        list.getStylesheets().add(getClass().getResource("/desktop-list.css").toExternalForm());
        list.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        var colors = ThemeManager.theme().colors();
        var placeholder = new Label("Selecione um arquivo para visualizar as alterações");
        placeholder.setStyle("-fx-text-fill: " + colors.textSecondary() + ";");
        list.setPlaceholder(placeholder);
        list.setStyle("-fx-font-family: monospace; -fx-font-size: 12px; -fx-control-inner-background: " + colors.surface() + "; -fx-focus-color: transparent; -fx-faint-focus-color: transparent; -fx-background-insets: 0; -fx-border-color: " + colors.border() + ";");
        list.setCellFactory(view -> new ListCell<>() {
            { selectedProperty().addListener((observable, previous, selected) -> paint()); }
            @Override protected void updateItem(String item, boolean empty) { super.updateItem(item, empty); setText(empty ? null : item); paint(); }
            private void paint() {
                if (isEmpty() || getItem() == null) { setStyle(""); return; }
                String background = switch (kind(getItem())) { case ADDED -> "#1c3a2a"; case REMOVED -> "#44262b"; case HUNK -> "#24384c"; case HEADER -> colors.background(); default -> colors.surface(); };
                setStyle("-fx-text-fill: " + colors.textPrimary() + "; -fx-background-color: " + (isSelected() ? colors.hover() : background) + "; -fx-font-family: monospace;");
            }
        });
        var selected = new javafx.scene.control.MenuItem("Copiar linhas selecionadas"); selected.setOnAction(event -> copy(selectedText()));
        var all = new javafx.scene.control.MenuItem("Copiar diff completo"); all.setOnAction(event -> copy(text));
        menu = new ContextMenu(selected, all); list.setContextMenu(menu);
        list.setOnKeyPressed(event -> { if (event.isShortcutDown() && event.getCode() == KeyCode.C) { copy(selectedText()); event.consume(); } });
    }
    public static Kind kind(String line) {
        if (line.startsWith("diff ") || line.startsWith("index ") || line.startsWith("--- ") || line.startsWith("+++ ")) return Kind.HEADER;
        if (line.startsWith("@@")) return Kind.HUNK;
        if (line.startsWith("+")) return Kind.ADDED;
        if (line.startsWith("-")) return Kind.REMOVED;
        return Kind.CONTEXT;
    }
    public void setText(String value) {
        text = Objects.requireNonNull(value);
        list.getSelectionModel().clearSelection();
        list.getItems().setAll(text.isEmpty() ? java.util.List.of() : Arrays.asList(text.split("\\r?\\n", -1)));
        list.scrollTo(0);
    }
    public String text() { return text; }
    public String selectedText() { return String.join("\n", list.getSelectionModel().getSelectedItems()); }
    private static void copy(String value) { var content = new ClipboardContent(); content.putString(value); javafx.scene.input.Clipboard.getSystemClipboard().setContent(content); }
    @Override public void close() {
        menu.hide(); menu.getItems().forEach(item -> item.setOnAction(null));
        list.setContextMenu(null); list.setOnKeyPressed(null); list.setCellFactory(null); list.getItems().clear(); text = "";
    }
}
