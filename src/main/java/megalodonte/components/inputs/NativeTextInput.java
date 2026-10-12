package megalodonte.components.inputs;

import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import megalodonte.props.InputProps;

/** Megalodonte's native single-line field for declarative, session-owned bindings. */
public final class NativeTextInput extends InputBase {
    public NativeTextInput(boolean password, InputProps props) {
        super(password ? new PasswordField() : new TextField(), props);
        var stylesheet = getClass().getResource("/text-field.css");
        if (stylesheet != null) field.getStylesheets().add(stylesheet.toExternalForm());
    }

    public TextField control() { return (TextField) field; }
}
