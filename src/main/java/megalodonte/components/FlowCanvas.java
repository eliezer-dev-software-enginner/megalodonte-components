package megalodonte.components;

import javafx.scene.Cursor;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import javafx.scene.shape.Line;
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

    public FlowCanvas() {
        super(new Pane(), null);
        surface = (Pane) getJavaFxNode();
        surface.setMinSize(640, 440);
        surface.setPrefSize(1000, 700);
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

    public void show(List<Node> nodes) {
        this.nodes = List.copyOf(nodes);
        surface.getChildren().clear();
        ThemeInterface theme = ThemeManager.theme();
        if (theme == null) return;
        surface.setStyle("-fx-background-color: " + theme.colors().background() + ";");
        for (Node node : nodes) {
            double x = Math.max(8, node.x());
            double y = Math.max(8, node.y());
            Pane group = new Pane();
            group.setPrefSize(294, 88);
            group.setLayoutX(x);
            group.setLayoutY(y);
            Line edge = new Line(112, 44, 154, 44);
            edge.setStyle("-fx-stroke: " + theme.colors().border() + "; -fx-stroke-width: 2;");
            group.getChildren().addAll(edge,
                    card("GATILHO", node.trigger(), 0, theme.colors().primary(), theme),
                    card("AÇÃO JAVA", node.title(), 154, theme.colors().secondary(), theme));
            group.setCursor(Cursor.HAND);
            group.setOnMouseClicked(event -> onSelect.accept(node.id()));
            final double[] anchor = new double[2];
            group.setOnMousePressed(event -> {
                anchor[0] = event.getSceneX() - group.getLayoutX();
                anchor[1] = event.getSceneY() - group.getLayoutY();
            });
            group.setOnMouseDragged(event -> {
                group.setLayoutX(Math.max(8, event.getSceneX() - anchor[0]));
                group.setLayoutY(Math.max(8, event.getSceneY() - anchor[1]));
            });
            group.setOnMouseReleased(event -> onMove.accept(new Move(node.id(), group.getLayoutX(), group.getLayoutY())));
            surface.getChildren().add(group);
        }
    }

    private VBox card(String caption, String title, double x, String accent, ThemeInterface theme) {
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
                + " -fx-border-color: " + accent + "; -fx-border-width: 2;"
                + " -fx-border-radius: 10; -fx-padding: 12;");
        return card;
    }
}
