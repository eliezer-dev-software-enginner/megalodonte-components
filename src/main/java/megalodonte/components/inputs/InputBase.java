package megalodonte.components.inputs;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.TextInputControl;
import javafx.scene.layout.StackPane;
import megalodonte.components.Component;
import megalodonte.props.InputProps;
import megalodonte.State;

import java.util.function.Consumer;
import java.util.function.Function;

public abstract class InputBase extends Component {

    protected final TextInputControl field;
    protected final StackPane container;

    protected Node left;
    protected Node right;

    protected String rawValue = "";
    protected boolean internalChange = false;
    protected boolean lockCursorToEnd = false;

    protected State<String> state; // Armazena a referência para inicialização tardia
    protected Function<String, OnChangeResult> onChange;
    protected Function<String, OnChangeResult> onInitialize;

    protected InputBase(TextInputControl field, InputProps props) {
        super(new StackPane());
        this.container = (StackPane) node;
        this.field = field;

        container.getChildren().add(field);

        if (props != null) props.apply(node);
        field.caretPositionProperty().addListener((obs, oldPos, newPos) -> {
            if (lockCursorToEnd && !internalChange && newPos.intValue() < field.getText().length()) {
                field.positionCaret(field.getText().length());
            }
        });
    }

    public InputBase onChange(Function<String, OnChangeResult> handler) {
        this.onChange = handler;
        return this;
    }

    public InputBase onInitialize(Function<String, OnChangeResult> handler) {
        this.onInitialize = handler;
        // Se o bind já aconteceu, aplica a inicialização agora
        if (state != null && state.get() != null) {
            applyInitialFormat(state.get());
        }
        return this;
    }

    public void bind(State<String> state) {
        this.state = state;

        state.subscribe(v -> {
            if (internalChange) return;

            // Se o onInitialize já existe, usa ele, senão apenas seta o texto
            if (onInitialize != null) {
                applyInitialFormat(v);
            } else {
                internalChange = true;
                field.setText(v);
                internalChange = false;
            }

            if (lockCursorToEnd) {
                field.positionCaret(field.getText().length());
            }
        });

        field.textProperty().addListener((o, old, v) -> {
            if (internalChange) return;
            rawValue = v;

            if (onChange != null) {
                OnChangeResult result = onChange.apply(v);
                if (result != null) {
                    if (result.getStateValue() != null && !result.getStateValue().equals(state.get())) {
                        state.set(result.getStateValue());
                    }
                    if (result.getDisplayValue() != null && !result.getDisplayValue().equals(v)) {
                        setTextInternal(result.getDisplayValue());
                    }
                }
            } else {
                state.set(v);
            }
        });
    }

    private void applyInitialFormat(String v) {
        if (onInitialize == null) return;

        OnChangeResult result = onInitialize.apply(v);
        if (result != null) {
            internalChange = true;
            field.setText(result.getDisplayValue());
            if (result.getStateValue() != null && !result.getStateValue().equals(v)) {
                state.set(result.getStateValue());
            }
            internalChange = false;
        }
    }

    protected void setTextInternal(String value) {
        if (value != null && value.equals(field.getText())) return;
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

    public InputBase onChangeFocus(Consumer<Boolean> eventHandler) {
        this.field.focusedProperty().addListener((obs, old, newState) -> {
            eventHandler.accept(newState);
        });
        return this;
    }
}