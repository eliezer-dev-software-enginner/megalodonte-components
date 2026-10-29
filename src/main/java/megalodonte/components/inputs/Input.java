package megalodonte.components.inputs;

import javafx.scene.control.TextField;
import megalodonte.props.InputProps;
import megalodonte.base.state.State;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * usa {@code javafx.scene.control.TextField} por baixo, então herda
 * a skin do Modena (ver DECISIONS.md, 2026-08-03) — o fix de `text-field.css`
 * funciona com o estilo do tema aplicado ao campo nativo.
 */
public class Input extends InputBase {

    public Input(State<String> state) {
        this(state,  new InputProps());
    }

    public Input(State<String> state, InputProps props) {
        super(new TextField(), props);
        applyTextFieldStylesheet();
        bind(state);
    }

    public Input(String value) {
        super(new TextField(value), new InputProps());
        applyTextFieldStylesheet();
    }

    @Override public Input onChange(Function<String, OnChangeResult> handler) { super.onChange(handler); return this; }
    @Override public Input onInitialize(Function<String, OnChangeResult> handler) { super.onInitialize(handler); return this; }
    @Override public Input onEnter(Runnable handler) { super.onEnter(handler); return this; }
    @Override public Input onChangeFocus(Consumer<Boolean> handler) { super.onChangeFocus(handler); return this; }
    @Override public Input lockCursorToEnd() { super.lockCursorToEnd(); return this; }
    @Override public Input left(Node node) { super.left(node); return this; }
    @Override public Input right(Node node) { super.right(node); return this; }

    /** Expande este campo para a largura disponível, inclusive dentro de um Row. */
    public Input fillWidth() {
        container.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(container, Priority.ALWAYS);
        return this;
    }

    // Fix do bevel/innershadow padrão do Modena em .text-field, mesma técnica
    // já usada em TextAreaInput (text-area.css) pros cantos do TextArea.
    private void applyTextFieldStylesheet() {
        field.getStylesheets().add(
                getClass().getResource("/text-field.css").toExternalForm()
        );
    }
}
