package megalodonte.components;

import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import megalodonte.base.state.ReadableState;
import megalodonte.base.theme.ThemeInterface;
import megalodonte.base.theme.ThemeManager;
import megalodonte.props.SelectProps;
import megalodonte.base.state.State;

import java.util.List;
import java.util.function.Function;
import java.util.function.BiPredicate;
import java.lang.reflect.Field;
import megalodonte.base.components.Component;

public class Select<T> extends Component  {

    private final ComboBox<T> comboBox;
    private BiPredicate<T, T> itemComparator = (a, b) -> a == b;

    public Select() {
        super(new ComboBox<T>());
        this.comboBox = (ComboBox<T>) this.node;
        applyDefaultCellTheme();
    }

    public Select(SelectProps props) {
        super(new ComboBox<T>(), props);
        this.comboBox = (ComboBox<T>) this.node;
        applyDefaultCellTheme();
    }

    /**
     * Célula padrão themada (usa item.toString()). displayText(...) troca por uma
     * versão com mapper customizado, mas reaproveita o mesmo estilo de tema — assim
     * o popup do dropdown não volta a ficar com a cor padrão (azul) do JavaFX.
     */
    private void applyDefaultCellTheme() {
        Function<T, String> toString = item -> item == null ? "" : item.toString();
        comboBox.setCellFactory(lv -> themedListCell(toString));
        comboBox.setButtonCell(buttonCell(toString));
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
                setStyle("-fx-background-color: transparent;");
            }
        };
    }

    private void styleListCell(ListCell<T> cell) {
        if (cell.isEmpty()) {
            cell.setStyle("");
            return;
        }

        ThemeInterface theme = ThemeManager.theme();
        String bg = cell.isSelected() ? theme.colors().selection()
                : cell.isHover() ? theme.colors().hover()
                : theme.colors().background();

        cell.setStyle("-fx-background-color: " + bg + "; -fx-text-fill: " + theme.colors().textPrimary() + ";");
    }

    public Select<T> items(Iterable<T> items) {
        comboBox.getItems().clear();
        items.forEach(comboBox.getItems()::add);
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
            comboBox.getItems().clear();
            if (items != null) {
                comboBox.getItems().addAll(items);
            }
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
            comboBox.getItems().clear();
            if (items != null) {
                comboBox.getItems().addAll(items);
            }
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
        for (T item : comboBox.getItems()) {
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
        comboBox.setCellFactory(param -> themedListCell(mapper));
        comboBox.setButtonCell(buttonCell(mapper));
        return this;
    }
}

