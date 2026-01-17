package megalodonte.components;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;
import megalodonte.ReadableState;
import megalodonte.props.ButtonProps;

public class Button extends Component {
    private final javafx.scene.control.Button btn;
    private Timeline pressAnimation;
    private Timeline releaseAnimation;

    public Button(String textContent){
        super(new javafx.scene.control.Button(textContent));
        this.btn = (javafx.scene.control.Button) this.node;
        setupButtonBehavior();
    }

    public Button(String textContent, ButtonProps props){
        super(new javafx.scene.control.Button(textContent), props);
        this.btn = (javafx.scene.control.Button) this.node;
        setupButtonBehavior();
    }

    public Button(ReadableState<String> state) {
        super(new javafx.scene.control.Button());
        this.btn = (javafx.scene.control.Button) this.node;

        state.subscribe(btn::setText);
        setupButtonBehavior();
    }

    public Button(ReadableState<String> state, ButtonProps props) {
        super(new javafx.scene.control.Button(), props);
        this.btn = (javafx.scene.control.Button) this.node;

        state.subscribe(btn::setText);
        setupButtonBehavior();
    }

    private void setupButtonBehavior() {
        // Configurar cursor hand
        btn.setStyle(btn.getStyle() + " -fx-cursor: hand;");
        
        // Configurar animações
        this.pressAnimation = createPressAnimation();
        this.releaseAnimation = createReleaseAnimation();
        
        // Configurar handlers de mouse
        setupMouseHandlers();
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

    /**
     * Creates a new Button component with the specified text.
     * 
     * @param textContent the button text
     * @return a new Button instance
     */
    public static Button of(String textContent) {
        return new Button(textContent);
    }

    /**
     * Creates a new Button component with the specified text and properties.
     * 
     * @param textContent the button text
     * @param props the button properties
     * @return a new Button instance
     */
    public static Button of(String textContent, ButtonProps props) {
        return new Button(textContent, props);
    }

    /**
     * Creates a new Button component bound to the specified state.
     * 
     * @param state the readable state containing button text
     * @return a new Button instance
     */
    public static Button of(ReadableState<String> state) {
        return new Button(state);
    }

    /**
     * Creates a new Button component bound to the specified state with properties.
     * 
     * @param state the readable state containing button text
     * @param props the button properties
     * @return a new Button instance
     */
    public static Button of(ReadableState<String> state, ButtonProps props) {
        return new Button(state, props);
    }
}
