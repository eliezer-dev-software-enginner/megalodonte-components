package megalodonte.components.inputs;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.TextInputControl;
import javafx.scene.layout.StackPane;
import megalodonte.components.Component;
import megalodonte.InputProps;
import megalodonte.State;

public abstract class InputBase extends Component {

    protected final TextInputControl field;
    protected final StackPane container;

    protected Node left;
    protected Node right;

    protected InputBase(TextInputControl field, InputProps props) {
        super(new StackPane());
        this.container = (StackPane) node;
        this.field = field;

        container.getChildren().add(field);

        if (props != null) {
            props.apply(field);
        }
    }

    protected void bind(State<String> state) {
        state.subscribe(field::setText);
        field.textProperty().addListener((o, old, v) -> state.set(v));
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
