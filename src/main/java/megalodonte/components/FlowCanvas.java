package megalodonte.components;

import javafx.scene.Cursor;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.control.Label;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import megalodonte.base.components.Component;
import megalodonte.base.theme.ThemeManager;
import megalodonte.base.theme.ThemeInterface;

import java.util.List;
import java.util.function.Consumer;

/** Reusable visual trigger-to-action graph; dragging a card updates the caller's model. */
public final class FlowCanvas extends Component {
    public record Node(String id, String title, String trigger, double x, double y) { }
    public record Move(String id, double x, double y) { }

    private final Pane surface;
    private Consumer<String> onSelect = ignored -> { };
    private Consumer<Move> onMove = ignored -> { };
    private List<Node> nodes = List.of();
    private String selectedId;
    private double panX;
    private double panY;
    private double lastSceneX;
    private double lastSceneY;

    public FlowCanvas() {
        super(new Pane(), null);
        surface = (Pane) getJavaFxNode();
        surface.setMinSize(0, 0);
        surface.setPrefSize(640, 440);
        surface.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        Rectangle viewportClip = new Rectangle();
        viewportClip.widthProperty().bind(surface.widthProperty());
        viewportClip.heightProperty().bind(surface.heightProperty());
        surface.setClip(viewportClip);
        surface.setPickOnBounds(true);
        surface.setCursor(Cursor.OPEN_HAND);
        HBox.setHgrow(surface, Priority.ALWAYS);
        surface.setOnMousePressed(event -> {
            if (event.getTarget() != surface || !event.isPrimaryButtonDown()) return;
            lastSceneX = event.getSceneX();
            lastSceneY = event.getSceneY();
            surface.setCursor(Cursor.CLOSED_HAND);
        });
        surface.setOnMouseDragged(event -> {
            if (event.getTarget() != surface || !event.isPrimaryButtonDown()) return;
            double dx = event.getSceneX() - lastSceneX;
            double dy = event.getSceneY() - lastSceneY;
            panX += dx;
            panY += dy;
            for (var child : surface.getChildren()) {
                child.setLayoutX(child.getLayoutX() + dx);
                child.setLayoutY(child.getLayoutY() + dy);
            }
            lastSceneX = event.getSceneX();
            lastSceneY = event.getSceneY();
        });
        surface.setOnMouseReleased(event -> {
            if (event.getTarget() == surface) surface.setCursor(Cursor.OPEN_HAND);
        });
        ThemeManager.state().subscribe(theme -> {
            if (theme != null) show(nodes);
        });
    }

    public FlowCanvas onSelect(Consumer<String> callback) {
        onSelect = callback;
        return this;
    }

    public FlowCanvas onMove(Consumer<Move> callback) {
        onMove = callback;
        return this;
    }

    public void select(String id) {
        selectedId = id;
        show(nodes);
    }

    public void resetView() {
        panX = 0;
        panY = 0;
        show(nodes);
    }

    public double viewToModelX(double x) { return x - panX; }
    public double viewToModelY(double y) { return y - panY; }

    public void show(List<Node> nodes) {
        this.nodes = List.copyOf(nodes);
        surface.getChildren().clear();
        ThemeInterface theme = ThemeManager.theme();
        if (theme == null) return;
        surface.setStyle("-fx-background-color: " + theme.colors().background() + ";");
        for (Node node : nodes) {
            double x = node.x() + panX;
            double y = node.y() + panY;
            Pane group = new Pane();
            group.setId("flow-" + node.id());
            group.setPickOnBounds(true);
            group.setPrefSize(294, 88);
            group.setLayoutX(x);
            group.setLayoutY(y);
            Line edge = new Line(112, 44, 154, 44);
            edge.setStyle("-fx-stroke: " + theme.colors().border() + "; -fx-stroke-width: 2;");
            group.getChildren().addAll(edge,
                    card("GATILHO", node.trigger(), 0, theme.colors().primary(), theme, node.id().equals(selectedId)),
                    card("AÇÃO JAVA", node.title(), 154, theme.colors().secondary(), theme, node.id().equals(selectedId)));
            group.setCursor(Cursor.HAND);
            final double[] anchor = new double[2];
            final double[] start = new double[2];
            group.setOnMousePressed(event -> {
                anchor[0] = event.getSceneX() - group.getLayoutX();
                anchor[1] = event.getSceneY() - group.getLayoutY();
                start[0] = group.getLayoutX();
                start[1] = group.getLayoutY();
            });
            group.setOnMouseDragged(event -> {
                group.setLayoutX(event.getSceneX() - anchor[0]);
                group.setLayoutY(event.getSceneY() - anchor[1]);
            });
            group.setOnMouseReleased(event -> {
                if (Math.abs(group.getLayoutX() - start[0]) > 1 || Math.abs(group.getLayoutY() - start[1]) > 1) {
                    onMove.accept(new Move(node.id(), group.getLayoutX() - panX, group.getLayoutY() - panY));
                } else {
                    select(node.id());
                    onSelect.accept(node.id());
                }
            });
            surface.getChildren().add(group);
        }
    }

    private VBox card(String caption, String title, double x, String accent, ThemeInterface theme, boolean selected) {
        Label heading = new Label(caption);
        heading.setStyle("-fx-font-size: 10px; -fx-font-weight: bold; -fx-text-fill: "
                + theme.colors().textSecondary() + ";");
        Label label = new Label(title == null ? "" : title);
        label.setWrapText(true);
        label.setStyle("-fx-font-size: 14px; -fx-text-fill: " + theme.colors().textPrimary() + ";");
        VBox card = new VBox(5, heading, label);
        card.setPrefSize(140, 88);
        card.setMinSize(140, 88);
        card.setMaxSize(140, 88);
        card.setLayoutX(x);
        card.setLayoutY(0);
        card.setStyle("-fx-background-color: " + theme.colors().surface() + "; -fx-background-radius: 10;"
                + " -fx-border-color: " + (selected ? theme.colors().primary() : accent) + "; -fx-border-width: " + (selected ? 3 : 2) + ";"
                + " -fx-border-radius: 10; -fx-padding: 12;");
        return card;
    }
}
