package megalodonte.components;

import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.application.Platform;
import javafx.util.StringConverter;
import megalodonte.base.state.ReadableState;
import megalodonte.base.theme.ThemeInterface;
import megalodonte.base.theme.ThemeManager;
import megalodonte.props.SelectProps;
import megalodonte.base.state.State;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.function.Function;
import java.util.function.BiPredicate;
import java.lang.reflect.Field;
import megalodonte.base.components.Component;

import static megalodonte.styles.util.StyleUtils.updateBackgroundColor;
import static megalodonte.styles.util.StyleUtils.updateTextColor_Input;
import static megalodonte.styles.util.StyleUtils.updateFontSize;
import static megalodonte.styles.util.StyleUtils.applyStyleProperty;

public class Select<T> extends Component  {

    private final ComboBox<T> comboBox;
    private final SelectProps selectProps;
    private final List<T> sourceItems = new ArrayList<>();
    private BiPredicate<T, T> itemComparator = (a, b) -> a == b;
    private Function<T, String> displayMapper = item -> item == null ? "" : item.toString();
    private boolean searchable;
    private boolean updatingEditor;

    public Select() {
        this(new SelectProps());
    }

    public Select(SelectProps props) {
        super(new ComboBox<T>(), props);
        this.selectProps = props;
        this.comboBox = (ComboBox<T>) this.node;
        applyDefaultCellTheme();
        if (props.isSearchable()) searchable();
    }

    /**
     * Célula padrão themada (usa item.toString()). displayText(...) troca por uma
     * versão com mapper customizado, mas reaproveita o mesmo estilo de tema — assim
     * o popup do dropdown não volta a ficar com a cor padrão (azul) do JavaFX.
     */
    private void applyDefaultCellTheme() {
        comboBox.setCellFactory(lv -> themedListCell(displayMapper));
        comboBox.setButtonCell(buttonCell(displayMapper));
    }

    /**
     * Célula da lista do popup: recebe fundo/hover/seleção do tema.
     */
    private ListCell<T> themedListCell(Function<T, String> mapper) {
        ListCell<T> cell = new ListCell<>() {
            @Override
            protected void updateItem(T item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? "" : mapper.apply(item));
                styleListCell(this);
            }
        };
        cell.hoverProperty().addListener((obs, was, isHover) -> styleListCell(cell));
        cell.selectedProperty().addListener((obs, was, isSelected) -> styleListCell(cell));
        return cell;
    }

    /**
     * Célula que mostra o valor selecionado com o ComboBox fechado. Fica
     * transparente de propósito: o próprio ComboBox já tem fundo/borda/radius
     * do tema (via SelectProps) — se essa célula também pintar um fundo (como a
     * da lista faz), vira um retângulo reto por cima do box já arredondado.
     */
    private ListCell<T> buttonCell(Function<T, String> mapper) {
        return new ListCell<>() {
            @Override
            protected void updateItem(T item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? "" : mapper.apply(item));
                updateBackgroundColor(this, "transparent");
                updateFontSize(this, ThemeManager.theme().typography().small());
                var colors = ThemeManager.theme().colors();
                String foreground = selectProps.getTextColor();
                if (foreground == null || foreground.isBlank()) foreground = switch (selectProps.getTone()) {
                    case PRIMARY -> colors.textPrimary();
                    case SECONDARY, DISABLED -> colors.textSecondary();
                    case INVERTED -> colors.background();
                };
                setTextFill(javafx.scene.paint.Color.web(foreground));
            }
        };
    }

    private void styleListCell(ListCell<T> cell) {
        if (cell.isEmpty()) {
            cell.setStyle("");
            return;
        }

        ThemeInterface theme = ThemeManager.theme();
        // O popup do dropdown roda numa Scene separada da do ComboBox (ver
        // comentário em SelectProps.applyTheme), então -fx-font-size setado
        // ali não chega até aqui — precisa ser aplicado direto na célula.
        updateFontSize(cell, theme.typography().small());
        String bg = cell.isSelected() ? theme.colors().selection()
                : cell.isHover() ? theme.colors().hover()
                : theme.colors().background();

        updateBackgroundColor(cell, bg);
        updateTextColor_Input(cell, theme.colors().textPrimary());
    }

    public Select<T> items(Iterable<T> items) {
        var replacement = new ArrayList<T>();
        if (items != null) items.forEach(replacement::add);
        replaceItems(replacement);
        return this;
    }

    /**
     * Binds the Select items to a reactive State<List<T>>.
     * When the state changes, the ComboBox items are automatically updated.
     * 
     * @param state the State<List<T>> containing the items
     * @return this Select instance for method chaining
     */
    public Select<T> items(State<List<T>> state) {
        state.subscribe(items -> {
            replaceItems(items);
        });
        return this;
    }

//    public Select<T> items(ReadableState<List<T>> state) {
//        state.subscribe(items -> {
//            // Fecha o dropdown antes de trocar a lista
//            comboBox.hide();
//            comboBox.getSelectionModel().clearSelection();
//            comboBox.getItems().clear();
//            if (items != null) {
//                comboBox.getItems().addAll(items);
//            }
//        });
//        return this;
//    }

    public Select<T> items(ReadableState<List<T>> state) {
        state.subscribe(items -> {
            replaceItems(items);
        });
        return this;
    }




    public Select<T> expandWhen(ReadableState<Boolean> condition) {
        condition.subscribe(shouldExpand -> {
            if (shouldExpand) {
                comboBox.show();
            } else {
                comboBox.hide();
            }
        });
        return this;
    }

    public Select<T> value(State<T> state) {
        comboBox.valueProperty().addListener((obs, o, n) -> state.set(n));
        state.subscribe(newValue -> {
            if (newValue != null) {
                T matchingItem = findMatchingItem(newValue);
                comboBox.setValue(matchingItem);
            } else {
                comboBox.setValue(null);
            }
        });
        return this;
    }

    /**
     * Sets a custom comparator to determine if two items are equal.
     * This is useful when working with objects that don't implement equals() properly
     * or when you want to compare items based on a specific field (like ID).
     * 
     * @param comparator function that takes two items and returns true if they should be considered equal
     * @return this Select instance for method chaining
     */
    public Select<T> itemComparator(BiPredicate<T, T> comparator) {
        this.itemComparator = comparator;
        return this;
    }

    /**
     * Finds an item in the ComboBox items list that matches the target value
     * using the custom comparator.
     * 
     * @param target the value to find
     * @return the matching item from the list, or null if not found
     */
    private T findMatchingItem(T target) {
        for (T item : sourceItems) {
            if (itemComparator.test(item, target)) {
                return item;
            }
        }
        return target;
    }

    /**
     * Configures the Select to compare items by their 'id' field using reflection.
     * This is useful for model objects that have an 'id' field but don't implement equals().
     * 
     * @return this Select instance for method chaining
     */
    public Select<T> compareById() {
        this.itemComparator = (a, b) -> {
            if (a == b) return true;
            if (a == null || b == null) return false;
            
            try {
                Field idFieldA = a.getClass().getDeclaredField("id");
                Field idFieldB = b.getClass().getDeclaredField("id");
                idFieldA.setAccessible(true);
                idFieldB.setAccessible(true);
                
                Object idA = idFieldA.get(a);
                Object idB = idFieldB.get(b);
                
                return java.util.Objects.equals(idA, idB);
            } catch (Exception e) {
                return false;
            }
        };
        return this;
    }
    
    /**
     * Sets custom display text for items while maintaining full object references.
     * 
     * @param mapper function to convert item to display text
     * @return this Select instance for method chaining
     */
    public Select<T> displayText(Function<T, String> mapper) {
        this.displayMapper = mapper == null ? item -> item == null ? "" : item.toString() : mapper;
        comboBox.setCellFactory(param -> themedListCell(displayMapper));
        comboBox.setButtonCell(buttonCell(displayMapper));
        if (searchable) configureConverter();
        return this;
    }

    /**
     * Enables an editable, searchable dropdown. Typing filters the available
     * options by their display text while selecting an option still returns the
     * original typed object through {@link #value(State)}.
     */
    public Select<T> searchable() {
        if (searchable) return this;
        searchable = true;
        comboBox.setEditable(true);
        configureConverter();
        applySearchableSkin();
        comboBox.skinProperty().addListener((observable, oldSkin, newSkin) -> applySearchableSkin());
        comboBox.getEditor().textProperty().addListener((observable, oldText, newText) -> {
            if (updatingEditor) return;
            T selected = comboBox.getValue();
            String selectedText = selected == null ? "" : display(selected);
            if (selected != null && selectedText.equals(newText)) return;
            if (selected != null) {
                updatingEditor = true;
                try {
                    comboBox.setValue(null);
                    comboBox.getEditor().setText(newText);
                } finally {
                    updatingEditor = false;
                }
            }
            applyFilter(newText);
            if (comboBox.getItems().isEmpty()) {
                comboBox.hide();
            } else if ((comboBox.isFocused() || comboBox.getEditor().isFocused()) && !comboBox.isShowing()) {
                comboBox.show();
            }
        });
        comboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) setEditorText(display(newValue));
        });
        comboBox.setOnShowing(event -> {
            String query = comboBox.getEditor().getText();
            T selected = comboBox.getValue();
            applyFilter(selected != null && display(selected).equals(query) ? "" : query);
            if (comboBox.getItems().isEmpty()) Platform.runLater(comboBox::hide);
        });
        comboBox.getEditor().setOnMousePressed(event -> {
            if (!comboBox.isShowing()) {
                applyFilter(comboBox.getEditor().getText());
                if (!comboBox.getItems().isEmpty()) comboBox.show();
            }
        });
        return this;
    }

    private void applySearchableSkin() {
        Platform.runLater(() -> {
            var colors = ThemeManager.theme().colors();
            var editor = comboBox.getEditor();
            updateBackgroundColor(editor, colors.background());
            updateTextColor_Input(editor, colors.textPrimary());
            applyStyleProperty(editor, "0", "-fx-background-insets");
            applyStyleProperty(editor, "transparent", "-fx-border-color");
            applyStyleProperty(editor, "0", "-fx-border-width");
            applyStyleProperty(editor, "6", "-fx-background-radius");
            applyStyleProperty(editor, "6", "-fx-border-radius");
            var arrowButton = comboBox.lookup(".arrow-button");
            if (arrowButton != null) {
                arrowButton.setManaged(false);
                arrowButton.setVisible(false);
            }
        });
    }

    private void configureConverter() {
        comboBox.setConverter(new StringConverter<>() {
            @Override public String toString(T item) { return display(item); }

            @Override public T fromString(String text) {
                String expected = text == null ? "" : text.trim();
                return sourceItems.stream()
                        .filter(item -> display(item).equalsIgnoreCase(expected))
                        .findFirst().orElse(null);
            }
        });
    }

    private void replaceItems(Iterable<T> items) {
        T selected = comboBox.getValue();
        sourceItems.clear();
        if (items != null) items.forEach(sourceItems::add);
        applyFilter(searchable ? comboBox.getEditor().getText() : "");
        if (selected != null) comboBox.setValue(findMatchingItem(selected));
    }

    private void applyFilter(String query) {
        comboBox.getItems().setAll(filteredItems(sourceItems, query, displayMapper));
    }

    static <T> List<T> filteredItems(List<T> source, String query, Function<T, String> display) {
        String normalized = query == null ? "" : query.strip().toLowerCase(Locale.ROOT);
        if (normalized.isEmpty()) return List.copyOf(source);
        return source.stream().filter(item -> {
            String text = item == null ? "" : display.apply(item);
            return text != null && text.toLowerCase(Locale.ROOT).contains(normalized);
        }).toList();
    }

    private String display(T item) {
        if (item == null) return "";
        String value = displayMapper.apply(item);
        return value == null ? "" : value;
    }

    private void setEditorText(String value) {
        updatingEditor = true;
        try {
            comboBox.getEditor().setText(value);
        } finally {
            updatingEditor = false;
        }
    }
}
