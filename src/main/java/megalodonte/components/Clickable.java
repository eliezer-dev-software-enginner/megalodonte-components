package megalodonte.components;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.Cursor;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import megalodonte.base.components.Component;

public class Clickable extends Component {
    private static final double NORMAL_OPACITY = 1.0;
    private static final double HOVER_OPACITY = 0.85;
    private static final double PRESS_OPACITY = 0.65;

    private final StackPane container;
    private Timeline opacityAnimation;
    private Runnable onClick;

    public Clickable(Component content, Runnable onClick) {
        super(new StackPane(), null);

        this.container = (StackPane) node;
        this.onClick = onClick;

        // Configurar comportamento do StackPane
        setupContainerBehavior();

        // Adicionar conteúdo
        this.container.getChildren().add(content.getNode());

        // Configurar handlers de mouse
        setupMouseHandlers();
    }

    private void setupContainerBehavior() {
        // Impedir crescimento automático do Region
        container.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);

        // Tornar clicável
        container.setPickOnBounds(true);
        container.setCursor(Cursor.HAND);
    }

    private void setupMouseHandlers() {
        container.setOnMouseEntered(e -> animateOpacityTo(HOVER_OPACITY, Duration.millis(150)));
        container.setOnMouseExited(e -> animateOpacityTo(NORMAL_OPACITY, Duration.millis(150)));
        container.setOnMousePressed(e -> animateOpacityTo(PRESS_OPACITY, Duration.millis(100)));
        container.setOnMouseReleased(this::handleMouseReleased);
    }

    private void handleMouseReleased(MouseEvent event) {
        boolean stillInside = container.contains(event.getX(), event.getY());
        animateOpacityTo(stillInside ? HOVER_OPACITY : NORMAL_OPACITY, Duration.millis(150));

        // Disparar onClick apenas se o mouse ainda estiver dentro do componente
        if (stillInside && onClick != null) {
            onClick.run();
        }
    }

    private void animateOpacityTo(double target, Duration duration) {
        if (opacityAnimation != null) {
            opacityAnimation.stop();
        }
        opacityAnimation = new Timeline(
                new KeyFrame(duration, new KeyValue(container.opacityProperty(), target))
        );
        opacityAnimation.play();
    }

    /**
     * Configura manualmente o callback de clique.
     */
    public void setOnClick(Runnable onClick) {
        this.onClick = onClick;
    }

    /**
     * Configura o comportamento de feedback tátil (vibração) para dispositivos móveis.
     * No desktop, este método não tem efeito.
     */
    public void enableHapticFeedback() {
        // Futura implementação para feedback tátil em dispositivos móveis
        // No desktop, podemos adicionar som ou outro feedback visual
    }

    /**
     * Configura o delay antes de considerar um clique como longo press.
     * Útil para implementar menus de contexto ou outras ações.
     */
    public void setLongPressDelay(Duration delay) {
        // Implementação futura para long press
    }

    /**
     * Desativa temporariamente os cliques no componente.
     */
    public void setEnabled(boolean enabled) {
        container.setDisable(!enabled);
        container.setMouseTransparent(!enabled);
    }
}
