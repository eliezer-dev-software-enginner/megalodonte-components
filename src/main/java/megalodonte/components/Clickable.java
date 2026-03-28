package megalodonte.components;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import megalodonte.props.ClickableProps;

public class Clickable extends Component {

    private final StackPane container;
    private final Timeline pressAnimation;
    private final Timeline releaseAnimation;
    private Runnable onClick;

    public Clickable(Component content, Runnable onClick) {
        this(content, onClick, new ClickableProps());
    }

    public Clickable(Component content, Runnable onClick, ClickableProps props) {
        super(new StackPane(), props);
        
        this.container = (StackPane) node;
        this.onClick = onClick;
        
        // Configurar comportamento do StackPane
        setupContainerBehavior();
        
        // Adicionar conteúdo
        this.container.getChildren().add(content.getNode());
        
        // Configurar animações
        this.pressAnimation = createPressAnimation();
        this.releaseAnimation = createReleaseAnimation();
        
        // Configurar handlers de mouse
        setupMouseHandlers();
    }

    private void setupContainerBehavior() {
        // Impedir crescimento automático do Region
        container.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
        
        // Tornar clicável
        container.setPickOnBounds(true);
    }

    private Timeline createPressAnimation() {
        Timeline timeline = new Timeline(
            new KeyFrame(Duration.millis(100), event -> {
                // Efeito de pressão - reduz opacity suavemente
                String currentStyle = container.getStyle() != null ? container.getStyle() : "";
                String newStyle = currentStyle + " -fx-opacity: 0.7;";
                container.setStyle(newStyle);
            })
        );
        timeline.setCycleCount(1);
        return timeline;
    }

    private Timeline createReleaseAnimation() {
        Timeline timeline = new Timeline(
            new KeyFrame(Duration.millis(150), event -> {
                // Efeito de release - restaura opacity suavemente
                String currentStyle = container.getStyle() != null ? container.getStyle() : "";
                String newStyle = currentStyle.replace(" -fx-opacity: 0.7;", "");
                container.setStyle(newStyle);
            })
        );
        timeline.setCycleCount(1);
        return timeline;
    }

    private void setupMouseHandlers() {
        container.setOnMousePressed(this::handleMousePressed);
        container.setOnMouseReleased(this::handleMouseReleased);
        container.setOnMouseExited(this::handleMouseExited);
    }

    private void handleMousePressed(MouseEvent event) {
        // Iniciar animação de pressão
        if (pressAnimation.getStatus() != Animation.Status.RUNNING) {
            pressAnimation.playFromStart();
        }
    }

    private void handleMouseReleased(MouseEvent event) {
        if (container.contains(event.getX(), event.getY())) {
            // Disparar onClick apenas se o mouse ainda estiver dentro do componente
            if (onClick != null) {
                onClick.run();
            }
            
            // Iniciar animação de release
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