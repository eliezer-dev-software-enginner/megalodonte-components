package megalodonte.components.inputs;

import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.Group;
import javafx.scene.shape.Line;
import javafx.scene.shape.SVGPath;
import javafx.scene.paint.Color;
import megalodonte.props.InputProps;

/** Megalodonte's native single-line field for declarative, session-owned bindings. */
public final class NativeTextInput extends InputBase {
    private TextField revealedField;
    private Button visibilityButton;

    public NativeTextInput(boolean password, InputProps props) {
        super(password ? new PasswordField() : new TextField(), props);
        var stylesheet = getClass().getResource("/text-field.css");
        if (stylesheet != null) field.getStylesheets().add(stylesheet.toExternalForm());
    }

    public TextField control() { return (TextField) field; }

    /** The bound PasswordField stays in place; the revealed field mirrors its text. */
    public NativeTextInput enableVisibilityToggle() {
        if (!(field instanceof PasswordField)) throw new IllegalStateException("Visibility toggle requires a password field");
        if (visibilityButton != null) return this;
        var stylesheet = getClass().getResource("/text-field.css");
        revealedField = new TextField();
        if (stylesheet != null) revealedField.getStylesheets().add(stylesheet.toExternalForm());
        revealedField.textProperty().bindBidirectional(field.textProperty());
        revealedField.promptTextProperty().bind(field.promptTextProperty());
        revealedField.styleProperty().bind(field.styleProperty());
        revealedField.minHeightProperty().bind(field.minHeightProperty());
        revealedField.prefHeightProperty().bind(field.prefHeightProperty());
        revealedField.maxHeightProperty().bind(field.maxHeightProperty());
        revealedField.setVisible(false);
        revealedField.setManaged(false);
        container.getChildren().add(revealedField);

        var eye = new SVGPath();
        eye.setContent("M2 12 C5 7 8 5 12 5 C16 5 19 7 22 12 C19 17 16 19 12 19 C8 19 5 17 2 12 Z M12 9 A3 3 0 1 0 12 15 A3 3 0 1 0 12 9");
        eye.setFill(Color.TRANSPARENT);
        Color iconColor = Color.web(megalodonte.base.theme.ThemeManager.theme().colors().secondary());
        eye.setStroke(iconColor);
        eye.setStrokeWidth(1.6);
        var slash = new Line(3, 21, 21, 3);
        slash.setStroke(iconColor);
        slash.setStrokeWidth(1.6);
        slash.setVisible(false);
        visibilityButton = new Button();
        visibilityButton.setGraphic(new Group(eye, slash));
        visibilityButton.setStyle("-fx-background-color: transparent; -fx-border-color: transparent; -fx-padding: 4;");
        visibilityButton.setFocusTraversable(true);
        visibilityButton.setAccessibleText("Mostrar senha");
        visibilityButton.setOnAction(event -> {
            boolean show = !revealedField.isVisible();
            int caret = ((TextField) (show ? field : revealedField)).getCaretPosition();
            revealedField.setVisible(show);
            revealedField.setManaged(show);
            field.setVisible(!show);
            field.setManaged(!show);
            slash.setVisible(show);
            visibilityButton.setAccessibleText(show ? "Ocultar senha" : "Mostrar senha");
            TextField target = show ? revealedField : (TextField) field;
            target.requestFocus();
            target.positionCaret(Math.min(caret, target.getLength()));
        });
        right(visibilityButton);
        return this;
    }

    public TextField revealedControl() { return revealedField; }
    public Button visibilityControl() { return visibilityButton; }
}
