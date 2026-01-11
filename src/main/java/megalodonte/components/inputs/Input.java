package megalodonte.components.inputs;

import javafx.scene.control.TextField;
import megalodonte.props.InputProps;
import megalodonte.State;
import megalodonte.styles.InputStyler;

public class Input extends InputBase {

    public Input(State<String> state) {
        this(state,  new InputProps(), new InputStyler());
    }

    public Input(State<String> state, InputProps props) {
        super(new TextField(), props,  new InputStyler());
        bind(state);
    }

    public Input(State<String> state, InputProps props, InputStyler styler) {
        super(new TextField(), props, styler);
        bind(state);
    }


    public Input(String value) {
        super(new TextField(value), new InputProps(), new InputStyler());
    }
}