package megalodonte.components;

import javafx.scene.control.ComboBox;
import megalodonte.SelectProps;
import megalodonte.State;

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

    public Select<T> value(State<T> state) {
        comboBox.valueProperty().addListener((obs, o, n) -> state.set(n));
        state.subscribe(comboBox::setValue);
        return this;
    }
}

