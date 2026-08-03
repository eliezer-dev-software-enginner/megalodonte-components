package megalodonte.components.inputs;

import javafx.scene.control.TextField;
import megalodonte.props.InputProps;
import megalodonte.base.state.State;

/**
 * @deprecated usa {@code javafx.scene.control.TextField} por baixo, então herda
 * a skin do Modena (ver DECISIONS.md, 2026-08-03) — o fix de `text-field.css`
 * funciona, mas ainda depende de neutralizar CSS nativo. Use
 * {@link megalodonte.components.v2.Input} (construído do zero, sem nenhum
 * Control nativo, zero disputa de CSS possível) pra código novo.
 */
@Deprecated(forRemoval = true)
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

    // Fix do bevel/innershadow padrão do Modena em .text-field, mesma técnica
    // já usada em TextAreaInput (text-area.css) pros cantos do TextArea.
    private void applyTextFieldStylesheet() {
        field.getStylesheets().add(
                getClass().getResource("/text-field.css").toExternalForm()
        );
    }
}