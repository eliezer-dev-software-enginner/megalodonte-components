package megalodonte.components.v2;

import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import megalodonte.base.components.Component;
import megalodonte.base.theme.ThemeManager;

/**
 * Vertical scroll container built from scratch — no {@code ScrollPane}/
 * {@code ScrollBar} {@code Control} under the hood, same spirit as
 * {@link Input} (v2): a plain {@link Pane} clips and translates the content,
 * a hand-drawn track/thumb/up-down-arrow-button column sits on the right,
 * sized and colored directly in Java. Since no native Control is involved,
 * no Modena CSS rule ever applies here — nothing to fight for specificity
 * against, unlike {@code megalodonte.components.Scroll} (which wraps a real
 * {@code ScrollPane} and has to fight Modena's own scrollbar rules to
 * restyle it).
 * <p>
 * {@link #viewport}/{@link #track} are added to {@link #outer} as
 * <em>unmanaged</em> children, positioned entirely by hand in
 * {@link #relayout()} — an unmanaged child doesn't contribute to its
 * parent's own preferred-size computation, so {@link #outer}'s size comes
 * purely from whatever its real parent assigns it (e.g. via
 * {@code Row/Column.fillHeight()}), never from the (potentially much
 * taller) content — the same "balloon past the window" failure mode chased
 * down repeatedly for {@code Row}/{@code Show}/{@code Column} elsewhere in
 * this library.
 * <p>
 * Known limitations (out of scope for this first version): no horizontal
 * scrolling, no keyboard scrolling.
 */
public class Scroll extends Component {

    private static final double TRACK_WIDTH = 12;
    private static final double BUTTON_HEIGHT = 14;
    private static final double STEP = 30;
    private static final double MIN_THUMB_HEIGHT = 20;
    private static final double ARROW_WIDTH = 5;
    private static final double ARROW_HEIGHT = 4;
    private static final Duration REPEAT_INITIAL_DELAY = Duration.millis(350);
    private static final Duration REPEAT_INTERVAL = Duration.millis(60);

    private final Pane outer;
    private final Pane viewport;
    private final Node content;
    private final Pane track;
    private final Region upButton;
    private final Region downButton;
    private final Region thumb;
    private final Rectangle viewportClip = new Rectangle();

    private final Color thumbColor;
    private final Color thumbActiveColor;

    private double scrollY = 0;
    private double thumbDragStartY;
    private double thumbDragStartScroll;

    public Scroll(Component component) {
        super(new Pane());
        this.outer = (Pane) node;
        this.content = component.getJavaFxNode();

        var theme = ThemeManager.theme();
        this.thumbColor = Color.web(theme.colors().secondary());
        this.thumbActiveColor = Color.web(theme.colors().primary());

        this.viewport = new Pane(content);
        viewport.setClip(viewportClip);
        viewport.setManaged(false);

        this.thumb = new Region();
        thumb.setBackground(solid(thumbColor, 6));
        thumb.setCursor(Cursor.DEFAULT);
        // Managed + resizable (which a bare Region is) means Pane.layoutChildren()
        // (Region's default, since Pane doesn't override it) resets it to its own
        // preferred size on every layout pass - 0x0 for an empty Region with no
        // CSS pref size set, silently undoing every resizeRelocate() from
        // relayoutOnce() on the very next pulse. Same for the two buttons below.
        thumb.setManaged(false);

        this.upButton = arrowButton(true);
        this.downButton = arrowButton(false);
        upButton.setManaged(false);
        downButton.setManaged(false);

        this.track = new Pane(thumb, upButton, downButton);
        track.setManaged(false);

        outer.getChildren().addAll(viewport, track);
        VBox.setVgrow(outer, Priority.ALWAYS);

        setUpLayoutListeners();
        setUpScrollWheel();
        setUpButtonHandlers();
        setUpThumbDrag();
        setUpThumbHover();

        relayout();
    }

    private Region arrowButton(boolean pointingUp) {
        StackPane button = new StackPane();
        button.setCursor(Cursor.HAND);

        Polygon arrow = new Polygon();
        if (pointingUp) {
            arrow.getPoints().addAll(0.0, ARROW_HEIGHT, ARROW_WIDTH, ARROW_HEIGHT, ARROW_WIDTH / 2, 0.0);
        } else {
            arrow.getPoints().addAll(0.0, 0.0, ARROW_WIDTH, 0.0, ARROW_WIDTH / 2, ARROW_HEIGHT);
        }
        arrow.setFill(thumbColor);
        arrow.setMouseTransparent(true);

        button.setOnMouseEntered(e -> arrow.setFill(thumbActiveColor));
        button.setOnMouseExited(e -> arrow.setFill(thumbColor));

        button.getChildren().add(arrow);
        return button;
    }

    private void setUpLayoutListeners() {
        outer.widthProperty().addListener((obs, o, n) -> relayout());
        outer.heightProperty().addListener((obs, o, n) -> relayout());
        content.layoutBoundsProperty().addListener((obs, o, n) -> relayout());
    }

    private void setUpScrollWheel() {
        // Always consumed, even with nothing to scroll - same "traps the
        // gesture" contract megalodonte.components.Scroll.confineScrollEvents
        // gives, so a Scroll nested inside another scrollable never leaks a
        // wheel gesture up once it's aimed at this one.
        outer.addEventHandler(ScrollEvent.SCROLL, e -> {
            scrollBy(-e.getDeltaY());
            e.consume();
        });
    }

    private void setUpButtonHandlers() {
        setUpAutoRepeat(upButton, () -> scrollBy(-STEP));
        setUpAutoRepeat(downButton, () -> scrollBy(STEP));
    }

    /**
     * Press-and-hold auto-repeat: fires {@code action} once immediately on
     * press, then again every {@link #REPEAT_INTERVAL} after an initial
     * {@link #REPEAT_INITIAL_DELAY} pause, for as long as the button stays
     * pressed — same two-stage timing native scrollbar arrow buttons use
     * (a short pause before repeating avoids a single accidental extra step
     * on every plain click). Stops on release or if the press drags off the
     * button, matching {@code arrowButton()}'s own hover handlers - uses
     * {@code addEventHandler} instead of {@code setOnMouseExited} so it
     * doesn't clobber the hover color-reset already wired there.
     */
    private void setUpAutoRepeat(Region button, Runnable action) {
        PauseTransition initialDelay = new PauseTransition(REPEAT_INITIAL_DELAY);
        Timeline repeat = new Timeline(new KeyFrame(REPEAT_INTERVAL, e -> action.run()));
        repeat.setCycleCount(Timeline.INDEFINITE);
        initialDelay.setOnFinished(e -> repeat.play());

        Runnable stop = () -> {
            initialDelay.stop();
            repeat.stop();
        };

        button.addEventHandler(MouseEvent.MOUSE_PRESSED, e -> {
            if (button.isDisabled()) return;
            action.run();
            initialDelay.playFromStart();
            e.consume();
        });
        button.addEventHandler(MouseEvent.MOUSE_RELEASED, e -> {
            stop.run();
            e.consume();
        });
        button.addEventHandler(MouseEvent.MOUSE_EXITED, e -> stop.run());
    }

    private void setUpThumbDrag() {
        thumb.setOnMousePressed(e -> {
            thumbDragStartY = e.getSceneY();
            thumbDragStartScroll = scrollY;
            e.consume();
        });
        thumb.setOnMouseDragged(e -> {
            double maxScroll = maxScroll();
            if (maxScroll <= 0) return;

            double trackAreaHeight = Math.max(1, outer.getHeight() - 2 * BUTTON_HEIGHT - thumb.getHeight());
            double scrollPerPixel = maxScroll / trackAreaHeight;
            setScroll(thumbDragStartScroll + (e.getSceneY() - thumbDragStartY) * scrollPerPixel);
            e.consume();
        });
    }

    private void setUpThumbHover() {
        thumb.setOnMouseEntered(e -> thumb.setBackground(solid(thumbActiveColor, 6)));
        thumb.setOnMouseExited(e -> thumb.setBackground(solid(thumbColor, 6)));
    }

    private void scrollBy(double delta) {
        setScroll(scrollY + delta);
    }

    private void setScroll(double value) {
        scrollY = clamp(value, 0, maxScroll());
        content.setLayoutY(-scrollY);
        relayout();
    }

    private double contentHeight() {
        return content.getLayoutBounds().getHeight();
    }

    private double maxScroll() {
        return Math.max(0, contentHeight() - outer.getHeight());
    }

    private boolean layingOut = false;

    /**
     * Resizing {@code content} below re-fires its own
     * {@code layoutBoundsProperty()} listener (registered in
     * {@link #setUpLayoutListeners()}), which calls back into this same
     * method mid-execution - without a re-entrancy guard, that nested call
     * runs to completion using the same (correct) inputs, but then control
     * returns to the outer call, which keeps going and clobbers the result
     * with a second, now-inconsistent pass (observed as the thumb ending up
     * 0x0 instead of its computed height).
     */
    private void relayout() {
        if (layingOut) return;
        layingOut = true;
        try {
            relayoutOnce();
        } finally {
            layingOut = false;
        }
    }

    private void relayoutOnce() {
        double outerW = outer.getWidth();
        double outerH = outer.getHeight();
        if (outerW <= 0 || outerH <= 0) return;

        double viewportW = Math.max(0, outerW - TRACK_WIDTH);

        // fit-to-width: resize the content to the viewport's width if it can
        // be resized, same effect as ScrollPane.setFitToWidth(true).
        if (content instanceof Region contentRegion) {
            contentRegion.resize(viewportW, contentRegion.prefHeight(viewportW));
        }

        viewport.resizeRelocate(0, 0, viewportW, outerH);
        viewportClip.setWidth(viewportW);
        viewportClip.setHeight(outerH);

        double maxScroll = maxScroll();
        scrollY = clamp(scrollY, 0, maxScroll);
        content.setLayoutY(-scrollY);

        track.resizeRelocate(viewportW, 0, TRACK_WIDTH, outerH);
        upButton.resizeRelocate(0, 0, TRACK_WIDTH, BUTTON_HEIGHT);
        downButton.resizeRelocate(0, outerH - BUTTON_HEIGHT, TRACK_WIDTH, BUTTON_HEIGHT);

        boolean scrollable = maxScroll > 0;
        double trackAreaHeight = Math.max(0, outerH - 2 * BUTTON_HEIGHT);
        double contentH = contentHeight();
        double thumbHeight = scrollable
                ? Math.max(MIN_THUMB_HEIGHT, Math.min(trackAreaHeight, trackAreaHeight * (outerH / contentH)))
                : trackAreaHeight;
        double thumbTravel = Math.max(0, trackAreaHeight - thumbHeight);
        double thumbY = BUTTON_HEIGHT + (scrollable ? thumbTravel * (scrollY / maxScroll) : 0);

        thumb.resizeRelocate(2, thumbY, TRACK_WIDTH - 4, thumbHeight);
        upButton.setDisable(!scrollable);
        downButton.setDisable(!scrollable);
    }

    private static Background solid(Color color, double radius) {
        return new Background(new BackgroundFill(color, new CornerRadii(radius), Insets.EMPTY));
    }

    private static double clamp(double value, double min, double max) {
        return Math.max(min, Math.min(max, value));
    }
}
