package megalodonte.components;

import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.input.DataFormat;
import javafx.scene.input.TransferMode;
import megalodonte.base.components.Component;
import megalodonte.base.theme.ThemeInterface;
import megalodonte.base.theme.ThemeManager;

import java.util.List;
import java.util.function.Consumer;

/** Theme-aware diagram surface with independently positioned and connected nodes. */
public final class DiagramCanvas extends Component {
    public record Node(String id, String title, String kind, double x, double y) { }
    public record Edge(String from, String to, String kind) { }
    public record Move(String id, double x, double y) { }
    public record Drop(String type, double x, double y) { }
    static final DataFormat COMPONENT_TYPE = new DataFormat("application/x-megalodonte-diagram-component");

    private static final double WIDTH = 150;
    private static final double HEIGHT = 80;
    private final Pane surface;
    private List<Node> nodes = List.of();
    private List<Edge> edges = List.of();
    private String selectedId;
    private Consumer<String> onSelect = ignored -> { };
    private Consumer<Move> onMove = ignored -> { };
    private Consumer<Drop> onDrop = ignored -> { };
    private double panX;
    private double panY;
    private double lastSceneX;
    private double lastSceneY;

    public DiagramCanvas() {
        super(new Pane(), null);
        surface = (Pane) getJavaFxNode();
        surface.setMinSize(0, 0);
        surface.setPrefSize(640, 440);
        surface.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        HBox.setHgrow(surface, Priority.ALWAYS);
        Rectangle clip = new Rectangle();
        clip.widthProperty().bind(surface.widthProperty());
        clip.heightProperty().bind(surface.heightProperty());
        surface.setClip(clip);
        surface.setCursor(Cursor.OPEN_HAND);
        surface.setOnMousePressed(event -> {
            if (event.getTarget() != surface || !event.isPrimaryButtonDown()) return;
            lastSceneX = event.getSceneX(); lastSceneY = event.getSceneY();
            surface.setCursor(Cursor.CLOSED_HAND);
        });
        surface.setOnMouseDragged(event -> {
            if (event.getTarget() != surface || !event.isPrimaryButtonDown()) return;
            double dx = event.getSceneX() - lastSceneX, dy = event.getSceneY() - lastSceneY;
            panX += dx; panY += dy;
            lastSceneX = event.getSceneX(); lastSceneY = event.getSceneY();
            draw();
        });
        surface.setOnMouseReleased(event -> surface.setCursor(Cursor.OPEN_HAND));
        surface.setOnDragOver(event -> {
            if (event.getDragboard().hasContent(COMPONENT_TYPE)) event.acceptTransferModes(TransferMode.COPY);
            event.consume();
        });
        surface.setOnDragDropped(event -> {
            Object type = event.getDragboard().getContent(COMPONENT_TYPE);
            boolean accepted = type instanceof String && !((String) type).isBlank();
            if (accepted) {
                var point = surface.sceneToLocal(event.getSceneX(), event.getSceneY());
                onDrop.accept(new Drop((String) type, point.getX() - panX, point.getY() - panY));
            }
            event.setDropCompleted(accepted);
            event.consume();
        });
        ThemeManager.state().subscribe(theme -> { if (theme != null) draw(); });
    }

    public DiagramCanvas onSelect(Consumer<String> callback) { onSelect = callback; return this; }
    public DiagramCanvas onMove(Consumer<Move> callback) { onMove = callback; return this; }
    public DiagramCanvas onDrop(Consumer<Drop> callback) { onDrop = callback; return this; }
    public void show(List<Node> nodes, List<Edge> edges) {
        this.nodes = List.copyOf(nodes); this.edges = List.copyOf(edges); draw();
    }
    public void select(String id) { selectedId = id; draw(); }
    public void resetView() { panX = 0; panY = 0; draw(); }

    private void draw() {
        surface.getChildren().clear();
        ThemeInterface theme = ThemeManager.theme();
        if (theme == null) return;
        surface.setStyle("-fx-background-color: " + theme.colors().background() + ";");
        for (var edge : edges) {
            Node from = nodes.stream().filter(node -> node.id().equals(edge.from())).findFirst().orElse(null);
            Node to = nodes.stream().filter(node -> node.id().equals(edge.to())).findFirst().orElse(null);
            if (from == null || to == null) continue;
            double x1 = from.x() + WIDTH + panX, y1 = from.y() + HEIGHT / 2 + panY;
            double x2 = to.x() + panX, y2 = to.y() + HEIGHT / 2 + panY;
            Line line = new Line(x1, y1, x2, y2);
            line.setStroke(javafx.scene.paint.Color.web(theme.colors().primary()));
            line.setStrokeWidth(2);
            if ("IF".equals(edge.kind())) line.getStrokeDashArray().setAll(2d, 4d);
            if ("ELSE".equals(edge.kind())) line.getStrokeDashArray().setAll(10d, 5d);
            Polygon arrow = new Polygon(x2, y2, x2 - 9, y2 - 5, x2 - 9, y2 + 5);
            arrow.setFill(javafx.scene.paint.Color.web(theme.colors().primary()));
            surface.getChildren().addAll(line, arrow);
        }
        for (var node : nodes) {
            Pane card = new Pane();
            card.setId("diagram-" + node.id());
            card.setPrefSize(WIDTH, HEIGHT);
            card.setLayoutX(node.x() + panX); card.setLayoutY(node.y() + panY);
            card.setStyle("-fx-background-color: " + theme.colors().surface()
                    + "; -fx-background-radius: 9; -fx-border-radius: 9; -fx-border-width: "
                    + (node.id().equals(selectedId) ? "3" : "2") + "; -fx-border-color: "
                    + theme.colors().primary() + ";");
            Label kind = new Label(node.kind());
            kind.setLayoutX(12); kind.setLayoutY(9);
            kind.setStyle("-fx-font-size: 10px; -fx-font-weight: bold; -fx-text-fill: "
                    + theme.colors().textSecondary() + ";");
            Label title = new Label(node.title());
            title.setLayoutX(12); title.setLayoutY(29); title.setPrefWidth(WIDTH - 24);
            title.setWrapText(true);
            title.setStyle("-fx-font-size: 14px; -fx-text-fill: " + theme.colors().textPrimary() + ";");
            card.getChildren().addAll(kind, title);
            card.setCursor(Cursor.HAND);
            final double[] anchor = new double[2], start = new double[2];
            card.setOnMousePressed(event -> {
                anchor[0] = event.getSceneX() - card.getLayoutX();
                anchor[1] = event.getSceneY() - card.getLayoutY();
                start[0] = card.getLayoutX(); start[1] = card.getLayoutY();
            });
            card.setOnMouseDragged(event -> {
                card.setLayoutX(event.getSceneX() - anchor[0]);
                card.setLayoutY(event.getSceneY() - anchor[1]);
            });
            card.setOnMouseReleased(event -> {
                if (Math.abs(card.getLayoutX() - start[0]) > 1 || Math.abs(card.getLayoutY() - start[1]) > 1)
                    onMove.accept(new Move(node.id(), card.getLayoutX() - panX, card.getLayoutY() - panY));
                else { select(node.id()); onSelect.accept(node.id()); }
            });
            surface.getChildren().add(card);
        }
    }
}
