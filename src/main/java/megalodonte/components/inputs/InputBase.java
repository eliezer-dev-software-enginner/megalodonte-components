package megalodonte.components.inputs;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.TextInputControl;
import javafx.scene.layout.StackPane;
import megalodonte.components.Component;
import megalodonte.InputProps;
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

    protected Function<String, String> onChange;

    // (rawValue, currentValue
    public InputBase onChange(Function<String, String> handler) {
        this.onChange = handler;
        setupListener();
        return this;
    }


    private void setupListener() {
        field.textProperty().addListener((obs, oldValue, newValue) -> {
            if (internalChange) return;

            rawValue = newValue;

            if (onChange != null) {
                String formatted = onChange.apply(newValue);

                if (formatted != null && !formatted.equals(newValue)) {
                    setTextInternal(formatted);
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

        //bind(state) não pode ouvir o texto quando existe onChange
        field.textProperty().addListener((o, old, v) -> {
            if (!internalChange) {
                state.set(v);
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
