package megalodonte.components.layout_components;

import javafx.scene.layout.Pane;
import megalodonte.base.components.Component;
import megalodonte.props.CanvaProps;

/**
 * Free-positioning container — its internal node is a plain {@link Pane}, not a
 * {@code VBox}/{@code HBox} like {@link Container}/{@link Row}. Children don't
 * auto-arrange; each one sits wherever it's placed via {@code layoutX}/
 * {@code layoutY}, so this is the component to reach for when you need to
 * position things freely (e.g. a drag-and-drop surface, an overlay, a simple
 * diagram) instead of flowing top-to-bottom or left-to-right.
 */
public class Canva extends Component {
    private final Pane pane;
    private final CanvaProps canvaProps;

    public Canva() {
        this(new CanvaProps());
    }

    public Canva(CanvaProps props) {
        super(new Pane(), props);
        this.pane = (Pane) this.node;
        this.canvaProps = props;
    }

    /** Adds a child at the origin (0, 0). Use {@link #child(Component, double, double)} to position it. */
    public Canva child(Component component) {
        return child(component, 0, 0);
    }

    /** Adds a child positioned at absolute ({@code x}, {@code y}) coordinates inside the canvas. */
    public Canva child(Component component, double x, double y) {
        var node = component.getNode();
        node.setLayoutX(x);
        node.setLayoutY(y);
        this.pane.getChildren().add(node);
        return this;
    }

    /** Adds every child at the origin (0, 0) — reposition them individually via {@link #child(Component, double, double)}. */
    public Canva children(Component... components) {
        for (Component c : components) {
            child(c);
        }
        return this;
    }

    public CanvaProps props() {
        return canvaProps;
    }
}
