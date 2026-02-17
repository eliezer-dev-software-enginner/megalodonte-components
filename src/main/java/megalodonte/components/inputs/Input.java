package megalodonte.components.inputs;

import javafx.scene.control.TextField;
import megalodonte.props.InputProps;
import megalodonte.State;

public class Input extends InputBase {

    public Input(State<String> state) {
        this(state,  new InputProps());
    }

    public Input(State<String> state, InputProps props) {
        super(new TextField(), props);
        bind(state);
    }

    public Input(String value) {
        super(new TextField(value), new InputProps());
    }
}