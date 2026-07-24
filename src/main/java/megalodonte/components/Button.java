package megalodonte.components;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.util.Duration;
import megalodonte.base.components.IconInterface;
import megalodonte.base.state.ReadableState;
import megalodonte.props.ButtonProps;
import megalodonte.base.components.Component;

import java.util.Objects;

public class Button extends Component  {
    private static final double NORMAL_OPACITY = 1.0;
    private static final double HOVER_OPACITY = 0.85;
    private static final double PRESS_OPACITY = 0.65;

    private final javafx.scene.control.Button btn;
    private Timeline opacityAnimation;

    public Button(String textContent){
        this(textContent, new ButtonProps());
    }

    public Button(String textContent, ButtonProps props) {
        super(new javafx.scene.control.Button(textContent), props);
        this.btn = (javafx.scene.control.Button) this.node;
        setupButtonBehavior();
    }

    public Button(ReadableState<String> state) {
       this(state, new ButtonProps());
    }

    public Button(ReadableState<String> state, ButtonProps props) {
        super(new javafx.scene.control.Button(), props);
        this.btn = (javafx.scene.control.Button) this.node;

        state.subscribe(btn::setText);
        setupButtonBehavior();
    }

    private void setupButtonBehavior() {
        btn.setCursor(javafx.scene.Cursor.HAND); // ← API Java, não CSS inline

        setupMouseHandlers();

        btn.setOnMouseClicked(e -> {});
    }

    private void setupMouseHandlers() {
        btn.setOnMouseEntered(e -> animateOpacityTo(HOVER_OPACITY, Duration.millis(150)));
        btn.setOnMouseExited(e -> animateOpacityTo(NORMAL_OPACITY, Duration.millis(150)));
        btn.setOnMousePressed(e -> animateOpacityTo(PRESS_OPACITY, Duration.millis(100)));
        btn.setOnMouseReleased(e -> {
            boolean stillHovering = btn.contains(e.getX(), e.getY());
            animateOpacityTo(stillHovering ? HOVER_OPACITY : NORMAL_OPACITY, Duration.millis(150));
        });
    }

    private void animateOpacityTo(double target, Duration duration) {
        if (opacityAnimation != null) {
            opacityAnimation.stop();
        }
        opacityAnimation = new Timeline(
                new KeyFrame(duration, new KeyValue(btn.opacityProperty(), target))
        );
        opacityAnimation.play();
    }

    public Button onClick(Runnable handler) {
        if (handler != null) {
            btn.setOnMouseClicked(e -> handler.run());
        }
        return this;
    }

    public Button icon(IconInterface icon) {
        Objects.requireNonNull(icon);
        btn.setGraphic(icon.getNode());
        btn.setContentDisplay(javafx.scene.control.ContentDisplay.LEFT); // default
        btn.setGraphicTextGap(6);
        return this;
    }

    public Button icon(ReadableState<IconInterface> iconState) {
        this.icon(iconState.get());

        iconState.subscribe(icon -> {
            if (icon != null && icon.getNode()!=null) {
               btn.setGraphic(icon.getNode());
            } else {
                btn.setGraphic(null);
            }
        });
        return this;
    }

}
