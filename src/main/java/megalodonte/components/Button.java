package megalodonte.components;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;
import megalodonte.ComputedState;
import megalodonte.ReadableState;
import megalodonte.props.ButtonProps;
import megalodonte.props.TextProps;
import megalodonte.styles.ButtonStyler;
import megalodonte.styles.TextStyler;

public class Button extends TextComponent<ButtonProps, ButtonStyler> {
    private final javafx.scene.control.Button btn;
    private Timeline pressAnimation;
    private Timeline releaseAnimation;

    public Button(String textContent){
        this(textContent, new ButtonProps(), new ButtonStyler());
    }

    public Button(String textContent, ButtonProps props){
        this(textContent, props, new ButtonStyler());
    }

    public Button(String textContent, ButtonProps props, ButtonStyler styler) {
        super(new javafx.scene.control.Button(textContent), props, styler);
        this.btn = (javafx.scene.control.Button) this.node;
        setupButtonBehavior();
    }

    public Button(ReadableState<String> state) {
       this(state, new ButtonProps());
    }

    public Button(ReadableState<String> state, ButtonProps props) {
       this(state, props, new ButtonStyler());
    }

    public Button(ReadableState<String> state, ButtonProps props, ButtonStyler styler) {
        super(new javafx.scene.control.Button(), props, styler);
        this.btn = (javafx.scene.control.Button) this.node;

        state.subscribe(btn::setText);
        setupButtonBehavior();
    }

    private void setupButtonBehavior() {
        btn.setStyle(btn.getStyle() + " -fx-cursor: hand;");
        
        this.pressAnimation = createPressAnimation();
        this.releaseAnimation = createReleaseAnimation();
        
        setupMouseHandlers();
        
        btn.setOnMouseClicked(e -> {});
    }

    private Timeline createPressAnimation() {
        Timeline timeline = new Timeline(
            new KeyFrame(Duration.millis(100), event -> {
                // Efeito de pressão - reduz opacity suavemente
                String currentStyle = btn.getStyle() != null ? btn.getStyle() : "";
                String newStyle = currentStyle + " -fx-opacity: 0.7;";
                btn.setStyle(newStyle);
            })
        );
        timeline.setCycleCount(1);
        return timeline;
    }

    private Timeline createReleaseAnimation() {
        Timeline timeline = new Timeline(
            new KeyFrame(Duration.millis(150), event -> {
                // Efeito de release - restaura opacity suavemente
                String currentStyle = btn.getStyle() != null ? btn.getStyle() : "";
                String newStyle = currentStyle.replace(" -fx-opacity: 0.7;", "");
                btn.setStyle(newStyle);
            })
        );
        timeline.setCycleCount(1);
        return timeline;
    }

    private void setupMouseHandlers() {
        btn.setOnMousePressed(this::handleMousePressed);
        btn.setOnMouseReleased(this::handleMouseReleased);
        btn.setOnMouseExited(this::handleMouseExited);
    }

    private void handleMousePressed(MouseEvent event) {
        // Iniciar animação de pressão
        if (pressAnimation.getStatus() != Animation.Status.RUNNING) {
            pressAnimation.playFromStart();
        }
    }

    private void handleMouseReleased(MouseEvent event) {
        // Iniciar animação de release se o mouse ainda estiver dentro do botão
        if (btn.contains(event.getX(), event.getY())) {
            if (releaseAnimation.getStatus() != Animation.Status.RUNNING) {
                releaseAnimation.playFromStart();
            }
        }
    }

    private void handleMouseExited(MouseEvent event) {
        // Resetar opacity se o mouse sair do componente
        if (releaseAnimation.getStatus() != Animation.Status.RUNNING) {
            releaseAnimation.playFromStart();
        }
    }

    public Button onClick(Runnable handler) {
        if (handler != null) {
            btn.setOnMouseClicked(e -> handler.run());
        }
        return this;
    }
}
