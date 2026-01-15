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
        setupListener();
        return this;
    }


    private void setupListener() {
        field.textProperty().addListener((obs, oldValue, newValue) -> {
            if (internalChange) return;

            rawValue = newValue;

            if (onChange != null) {
                OnChangeResult result = onChange.apply(newValue);

                if (result != null) {
                    // Atualiza o campo com o valor formatado para exibição
                    if (result.getDisplayValue() != null && !result.getDisplayValue().equals(newValue)) {
                        setTextInternal(result.getDisplayValue());
                    }
                    
                    // O state será atualizado no bind() com o valor bruto
                    lastStateValue = result.getStateValue();
                }
            }
        });
    }


    protected void bind(State<String> state) {
        state.subscribe(v -> {
            internalChange = true;
            field.setText(v);
            internalChange = false;
        });

        // Quando existe onChange, usa o valor do state do onChange handler
        // Quando não existe onChange, usa o valor direto do campo
        field.textProperty().addListener((o, old, v) -> {
            if (!internalChange) {
                if (onChange != null && lastStateValue != null) {
                    state.set(lastStateValue);
                    lastStateValue = null; // Limpa após usar
                } else if (onChange == null) {
                    state.set(v);
                }
            }
        });
    }

    protected void setTextInternal(String value) {
        if (value.equals(field.getText())) return;

        internalChange = true;
        field.setText(value);
        internalChange = false;
    }

    public String getRawValue() {
        return rawValue;
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
