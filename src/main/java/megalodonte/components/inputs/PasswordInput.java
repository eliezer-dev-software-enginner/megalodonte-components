package megalodonte.components.inputs;

import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;
import megalodonte.InputProps;
import megalodonte.State;
import megalodonte.styles.InputStyler;

public class PasswordInput extends InputBase {

    private final PasswordField passwordField = new PasswordField();
    private final TextField textField = new TextField();
    private boolean visible = false;

    public PasswordInput(State<String> state, InputProps props) {
        super(new PasswordField(), props, new InputStyler());

        bind(state);
        installToggle(state);
    }

    public PasswordInput(State<String> state, InputProps props, InputStyler styler) {
        super(new PasswordField(), props, styler);

        bind(state);
        installToggle(state);
    }

    private void installToggle(State<String> state) {
        Button toggle = new Button("👁");

        toggle.setOnAction(e -> {
            visible = !visible;

            container.getChildren().remove(field);

            TextInputControl newField =
                    visible ? textField : passwordField;

            newField.setText(field.getText());
            container.getChildren().add(0, newField);

            bind(state);
        });

        right(toggle);
    }
}