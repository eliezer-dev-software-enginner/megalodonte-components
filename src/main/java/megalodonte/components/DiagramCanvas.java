package megalodonte.components;

import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.shape.Line;
import javafx.scene.shape.Circle;
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
    public record Node(String id, String title, String kind, double x, double y,
                       boolean acceptsInput, boolean acceptsOutput) {
        public Node(String id, String title, String kind, double x, double y) {
            this(id, title, kind, x, y, true, true);
        }
    }
    public record Edge(String id, String from, String to, String kind) {
        public Edge(String from, String to, String kind) { this(from + "->" + to, from, to, kind); }
    }
    public record Move(String id, double x, double y) { }
    public record Drop(String type, double x, double y) { }
    public record Connection(String from, String to) { }
    static final DataFormat COMPONENT_TYPE = new DataFormat("application/x-megalodonte-diagram-component");

    private static final double WIDTH = 150;
    private static final double HEIGHT = 80;
    private final Pane surface;
    private List<Node> nodes = List.of();
    private List<Edge> edges = List.of();
    private String selectedId;
    private Consumer<String> onSelect = ignored -> { };
    private Consumer<String> onEdgeSelect = ignored -> { };
    private Consumer<Move> onMove = ignored -> { };
    private Consumer<Drop> onDrop = ignored -> { };
    private Consumer<Connection> onConnect = ignored -> { };
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
    public DiagramCanvas onEdgeSelect(Consumer<String> callback) { onEdgeSelect = callback; return this; }
    public DiagramCanvas onMove(Consumer<Move> callback) { onMove = callback; return this; }
    public DiagramCanvas onDrop(Consumer<Drop> callback) { onDrop = callback; return this; }
    public DiagramCanvas onConnect(Consumer<Connection> callback) { onConnect = callback; return this; }
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
            Line hitArea = new Line(x1, y1, x2, y2);
            hitArea.setId("diagram-edge-" + edge.id());
            hitArea.setStroke(javafx.scene.paint.Color.TRANSPARENT);
            hitArea.setStrokeWidth(16);
            hitArea.setCursor(Cursor.HAND);
            hitArea.setOnMouseClicked(event -> { onEdgeSelect.accept(edge.id()); event.consume(); });
            arrow.setCursor(Cursor.HAND);
            arrow.setOnMouseClicked(event -> { onEdgeSelect.accept(edge.id()); event.consume(); });
            surface.getChildren().addAll(line, hitArea, arrow);
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
            if (node.acceptsInput()) addHandle(node, card, false, theme);
            if (node.acceptsOutput()) addHandle(node, card, true, theme);
        }
    }

    private void addHandle(Node node, Pane card, boolean output, ThemeInterface theme) {
        Circle handle = new Circle(7);
        handle.setId("diagram-" + (output ? "output-" : "input-") + node.id());
        handle.centerXProperty().bind(card.layoutXProperty().add(output ? WIDTH : 0));
        handle.centerYProperty().bind(card.layoutYProperty().add(HEIGHT / 2));
        handle.setFill(javafx.scene.paint.Color.web(theme.colors().surface()));
        handle.setStroke(javafx.scene.paint.Color.web(theme.colors().primary()));
        handle.setStrokeWidth(2);
        handle.setCursor(output ? Cursor.CROSSHAIR : Cursor.HAND);
        Tooltip.install(handle, new Tooltip(output ? "Arraste até a entrada de outro componente" : "Entrada do componente"));
        if (output) {
            Line preview = new Line();
            preview.setStroke(javafx.scene.paint.Color.web(theme.colors().primary()));
            preview.setStrokeWidth(2);
            preview.setMouseTransparent(true);
            Polygon arrow = new Polygon();
            arrow.setFill(javafx.scene.paint.Color.web(theme.colors().primary()));
            arrow.setMouseTransparent(true);
            handle.setOnMousePressed(event -> {
                if (!event.isPrimaryButtonDown()) return;
                preview.setStartX(handle.getCenterX()); preview.setStartY(handle.getCenterY());
                preview.setEndX(handle.getCenterX()); preview.setEndY(handle.getCenterY());
                surface.getChildren().addAll(preview, arrow);
                event.consume();
            });
            handle.setOnMouseDragged(event -> {
                if (!surface.getChildren().contains(preview)) return;
                var point = surface.sceneToLocal(event.getSceneX(), event.getSceneY());
                preview.setEndX(point.getX()); preview.setEndY(point.getY());
                double angle = Math.atan2(point.getY() - preview.getStartY(), point.getX() - preview.getStartX());
                double backX = point.getX() - 10 * Math.cos(angle), backY = point.getY() - 10 * Math.sin(angle);
                arrow.getPoints().setAll(point.getX(), point.getY(), backX + 5 * Math.sin(angle),
                        backY - 5 * Math.cos(angle), backX - 5 * Math.sin(angle), backY + 5 * Math.cos(angle));
                event.consume();
            });
            handle.setOnMouseReleased(event -> {
                boolean dragging = surface.getChildren().remove(preview);
                surface.getChildren().remove(arrow);
                if (dragging) {
                    var point = surface.sceneToLocal(event.getSceneX(), event.getSceneY());
                    nodes.stream().filter(target -> target.acceptsInput() && !target.id().equals(node.id()))
                            .filter(target -> Math.hypot(point.getX() - (target.x() + panX),
                                    point.getY() - (target.y() + HEIGHT / 2 + panY)) <= 12)
                            .findFirst().ifPresent(target -> onConnect.accept(new Connection(node.id(), target.id())));
                }
                event.consume();
            });
        }
        surface.getChildren().add(handle);
    }
}
