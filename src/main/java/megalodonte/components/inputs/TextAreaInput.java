package megalodonte.components.inputs;

import javafx.scene.control.TextArea;
import megalodonte.InputProps;
import megalodonte.State;
import megalodonte.styles.InputStyler;

public class TextAreaInput extends InputBase {

    public TextAreaInput(State<String> state, InputProps props) {
        super(new TextArea(), props, null);
        bind(state);
    }

    public TextAreaInput(State<String> state, InputProps props, InputStyler styler) {
        super(new TextArea(), props, styler);
        bind(state);
    }
}
