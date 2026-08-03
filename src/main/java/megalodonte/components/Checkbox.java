package megalodonte.components;

import javafx.scene.control.CheckBox;
import megalodonte.base.components.Component;
import megalodonte.base.state.State;
import megalodonte.props.CheckboxProps;

public class Checkbox extends Component {

    private final CheckBox javaFxCheckbox;

    public Checkbox(String label, State<Boolean> state) {
        this(label, state, new CheckboxProps());
    }

    public Checkbox(String label, State<Boolean> state, CheckboxProps props) {
        super(new CheckBox(label), props);
        this.javaFxCheckbox = (CheckBox) this.node;

        javaFxCheckbox.selectedProperty().addListener((obs, old, v) -> state.set(v));
        state.subscribe(javaFxCheckbox::setSelected);
    }
}
