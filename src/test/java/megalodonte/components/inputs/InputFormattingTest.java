package megalodonte.components.inputs;

import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import megalodonte.base.state.State;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.util.WaitForAsyncUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;

class InputFormattingTest extends ApplicationTest {
    private StackPane root;

    @Override public void start(Stage stage) {
        root = new StackPane();
        stage.setScene(new Scene(root, 260, 100));
        stage.show();
    }

    @Test void backspaceKeepsMaskedTextAndStateInSync() {
        State<String> state = State.of("1234");
        Input input = new Input(state)
                .onInitialize(value -> OnChangeResult.of(group(value), value))
                .onChange(value -> {
                    String digits = value.replaceAll("[^0-9]", "");
                    return OnChangeResult.of(group(digits), digits);
                })
                .lockCursorToEnd();
        TextField field = (TextField) ((StackPane) input.getJavaFxNode()).getChildren().getFirst();
        interact(() -> {
            root.getChildren().add(input.getJavaFxNode());
            field.requestFocus();
            field.positionCaret(field.getLength());
            field.deletePreviousChar();
        });
        WaitForAsyncUtils.waitForFxEvents();
        assertEquals("123", state.get());
        assertEquals("123", field.getText());

        interact(field::deletePreviousChar);
        WaitForAsyncUtils.waitForFxEvents();
        assertEquals("12", state.get());
        assertEquals("12", field.getText());
    }

    private static String group(String digits) {
        return digits.length() > 3 ? digits.substring(0, digits.length() - 3) + "." + digits.substring(digits.length() - 3) : digits;
    }
}
