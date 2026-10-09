package megalodonte.components.v2;

import javafx.scene.Node;
import javafx.scene.input.KeyCode;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.*;
import javafx.scene.shape.Rectangle;
import megalodonte.base.components.Component;
import megalodonte.base.theme.ThemeManager;

/** Horizontal counterpart of v2 Scroll. Uses clipped panes and a draggable track, without ScrollPane. */
public final class HorizontalScroll extends Component {
    private static final double TRACK_HEIGHT = 14;
    private final Pane outer, viewport, track;
    private final Region thumb = new Region();
    private final Rectangle clip = new Rectangle();
    private final Node content;
    private double offset, dragX, dragOffset;
    private boolean layingOut;
    public HorizontalScroll(Component component) {
        super(new Surface()); outer = (Pane) node; content = component.getJavaFxNode();
        ((Surface)outer).layout = this::relayout;
        if (content instanceof Pane pane) pane.getChildren().addListener((javafx.collections.ListChangeListener<Node>) change -> outer.requestLayout());
        content.setManaged(false);
        viewport = new Pane(content); viewport.setManaged(false); viewport.setClip(clip);
        track = new Pane(thumb); track.setManaged(false); thumb.setManaged(false);
        thumb.setStyle("-fx-background-color: " + ThemeManager.theme().colors().primary() + "; -fx-background-radius: 5;");
        track.setStyle("-fx-background-color: #dce3ed; -fx-background-radius: 5;");
        outer.getChildren().addAll(viewport, track); outer.setMinSize(0,0); outer.setFocusTraversable(true);
        outer.widthProperty().addListener((obs,o,n) -> relayout()); outer.heightProperty().addListener((obs,o,n) -> relayout());
        content.layoutBoundsProperty().addListener((obs,o,n) -> relayout());
        outer.addEventHandler(ScrollEvent.SCROLL, event -> {
            double delta = Math.abs(event.getDeltaX()) > Math.abs(event.getDeltaY()) ? event.getDeltaX() : event.getDeltaY();
            scrollTo(offset - delta); event.consume();
        });
        outer.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.RIGHT) scrollTo(offset + 60);
            else if (event.getCode() == KeyCode.LEFT) scrollTo(offset - 60);
            else if (event.getCode() == KeyCode.HOME) scrollTo(0);
            else if (event.getCode() == KeyCode.END) scrollTo(maxScroll());
            else return;
            event.consume();
        });
        thumb.setOnMousePressed(event -> { dragX = event.getSceneX(); dragOffset = offset; event.consume(); });
        thumb.setOnMouseDragged(event -> {
            double travel = track.getWidth() - thumb.getWidth();
            if (travel > 0) scrollTo(dragOffset + (event.getSceneX() - dragX) * maxScroll() / travel);
            event.consume();
        });
        track.setOnMousePressed(event -> {
            if (event.getTarget() != thumb) scrollTo(offset + (event.getX() < thumb.getLayoutX() ? -1 : 1) * outer.getWidth() * .8);
            event.consume();
        });
        relayout();
    }
    public Node getContent() { return content; }
    public double getScrollX() { return offset; }
    public void scrollTo(double value) { offset = Math.max(0, Math.min(maxScroll(), value)); relayout(); }
    /** Reveal a child after keyboard selection or playback advances. */
    public void reveal(Node child) {
        double left = child.getBoundsInParent().getMinX(), right = child.getBoundsInParent().getMaxX();
        if (left < offset) scrollTo(left);
        else if (right > offset + outer.getWidth()) scrollTo(right - outer.getWidth());
    }
    public void refresh() { relayout(); }
    private static final class Surface extends Pane {
        Runnable layout;
        @Override protected void layoutChildren() { if (layout != null) layout.run(); }
    }
    private double maxScroll() { return Math.max(0, content.getLayoutBounds().getWidth() - outer.getWidth()); }
    private void relayout() {
        if (layingOut || outer.getWidth() <= 0 || outer.getHeight() <= 0) return;
        layingOut = true;
        try {
            double width = outer.getWidth(), height = Math.max(0, outer.getHeight() - TRACK_HEIGHT);
            if (content instanceof Region region) region.resize(Math.max(width,region.prefWidth(height)),height);
            viewport.resizeRelocate(0,0,width,height); clip.setWidth(width); clip.setHeight(height);
            offset = Math.max(0,Math.min(maxScroll(),offset)); content.relocate(-offset,0);
            track.resizeRelocate(0,height,width,TRACK_HEIGHT);
            double max = maxScroll(); double thumbWidth = Math.min(width,Math.max(28,width * width / Math.max(width,content.getLayoutBounds().getWidth())));
            thumb.resizeRelocate(max == 0 ? 0 : (width - thumbWidth) * offset / max,3,thumbWidth,8);
            track.setVisible(max > 0);
        } finally { layingOut = false; }
    }
}
