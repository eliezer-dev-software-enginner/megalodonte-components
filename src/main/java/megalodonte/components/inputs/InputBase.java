package megalodonte.components.inputs;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.TextInputControl;
import javafx.scene.layout.StackPane;
import megalodonte.components.Component;
import megalodonte.props.InputProps;
import megalodonte.State;
import megalodonte.styles.InputStyler;

import java.util.function.BiConsumer;
import java.util.function.Function;

public abstract class InputBase extends Component {

    protected final TextInputControl field;
    protected final StackPane container;

    protected Node left;
    protected Node right;

    protected String rawValue = "";
    protected boolean internalChange = false;
    protected String lastStateValue = null;
    protected boolean lockCursorToEnd = false;

    protected InputBase(TextInputControl field, InputProps props, InputStyler styler) {
        super(new StackPane());
        this.container = (StackPane) node;
        this.field = field;

        container.getChildren().add(field);

       if(props!=null){
           props.apply(node);
       }
       if(styler != null){
           styler.apply(node, props);
       }
    }

    protected Function<String, OnChangeResult> onChange;

    // (rawValue, currentValue
public InputBase onChange(Function<String, OnChangeResult> handler) {
        this.onChange = handler;
        return this;
    }

    private void setupListener() {
        // Não usa mais este listener, a lógica foi movida para bind()
    }

    public void bind(State<String> state) {
        state.subscribe(v -> {
            internalChange = true;
            field.setText(v);
            if (lockCursorToEnd) {
                field.positionCaret(field.getText().length());
            }
            internalChange = false;
        });

        // Listener único que maneja tanto formatação quanto atualização do state
        field.textProperty().addListener((o, old, v) -> {
            if (internalChange) return;

            rawValue = v;

            if (onChange != null) {
                OnChangeResult result = onChange.apply(v);

                if (result != null) {
                    // Atualiza o state com o valor bruto
                    if (!result.getStateValue().equals(state.get())) {
                        state.set(result.getStateValue());
                    }

                    // Atualiza o campo com o valor formatado (se necessário)
                    if (result.getDisplayValue() != null && !result.getDisplayValue().equals(v)) {
                        setTextInternal(result.getDisplayValue());
                    }
                }
            } else {
                // Sem onChange - atualiza state diretamente
                state.set(v);
            }
        });

        // Listener para travar cursor no final (se ativado)
        if (lockCursorToEnd) {
            field.caretPositionProperty().addListener((obs, oldPos, newPos) -> {
                if (!internalChange && newPos.intValue() < field.getText().length()) {
                    field.positionCaret(field.getText().length());
                }
            });
}
    }

    protected void setTextInternal(String value) {
        if (value.equals(field.getText())) return;

        internalChange = true;
        field.setText(value);
        if (lockCursorToEnd) {
            field.positionCaret(field.getText().length());
        }
        internalChange = false;
    }

    public InputBase lockCursorToEnd() {
        this.lockCursorToEnd = true;
        return this;
    }

    public InputBase left(Node node) {
        this.left = node;
        StackPane.setMargin(node, new Insets(0, 0, 0, 8));
        StackPane.setAlignment(node, javafx.geometry.Pos.CENTER_LEFT);

        container.getChildren().add(node);
        adjustPadding();
        return this;
    }

    public InputBase right(Node node) {
        this.right = node;
        StackPane.setMargin(node, new Insets(0, 8, 0, 0));
        StackPane.setAlignment(node, javafx.geometry.Pos.CENTER_RIGHT);

        container.getChildren().add(node);
        adjustPadding();
        return this;
    }

    protected void adjustPadding() {
        double leftPad = left != null ? 32 : 8;
        double rightPad = right != null ? 32 : 8;

        field.setPadding(new Insets(0, rightPad, 0, leftPad));
    }
}
