package megalodonte.components;

import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import megalodonte.props.SelectProps;
import megalodonte.State;

import java.util.List;
import java.util.function.Function;

public class Select<T> extends Component {

    private final ComboBox<T> comboBox;

    public Select() {
        super(new ComboBox<T>());
        this.comboBox = (ComboBox<T>) this.node;
    }

    public Select(SelectProps props) {
        super(new ComboBox<T>(), props);
        this.comboBox = (ComboBox<T>) this.node;
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

    public Select<T> value(State<T> state) {
        comboBox.valueProperty().addListener((obs, o, n) -> state.set(n));
        state.subscribe(comboBox::setValue);
        return this;
    }
    
    /**
     * Sets custom display text for items while maintaining full object references.
     * 
     * @param mapper function to convert item to display text
     * @return this Select instance for method chaining
     */
    public Select<T> displayText(Function<T, String> mapper) {
        comboBox.setCellFactory(param -> new ListCell<T>() {
            @Override
            protected void updateItem(T item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? "" : mapper.apply(item));
            }
        });
        
        comboBox.setButtonCell(new ListCell<T>() {
            @Override
            protected void updateItem(T item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? "" : mapper.apply(item));
            }
        });
        return this;
    }
}

