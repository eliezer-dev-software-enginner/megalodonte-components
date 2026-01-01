package megalodonte.components.inputs;

import javafx.scene.control.TextArea;
import megalodonte.InputProps;
import megalodonte.State;

public class TextAreaInput extends InputBase {

    public TextAreaInput(State<String> state, InputProps props) {
        super(new TextArea(), props);
        bind(state);
    }
}
