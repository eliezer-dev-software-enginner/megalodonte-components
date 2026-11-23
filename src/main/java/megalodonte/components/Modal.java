package megalodonte.components;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.application.Platform;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import megalodonte.base.Animations;
import megalodonte.base.components.Component;
import megalodonte.base.state.State;
import megalodonte.base.theme.ThemeManager;
import megalodonte.props.ButtonProps;

import static megalodonte.styles.util.StyleUtils.updateBackgroundColor;

/**
 * Modal reativo, no espírito de um Modal de React: recebe um {@code State<Boolean>}
 * que controla sua visibilidade — some/aparece quando o state muda, e o próprio
 * Modal também escreve nesse state quando o usuário fecha (clique fora ou no "✕"),
 * exatamente como um componente controlado.
 * <p>
 * Diferente de {@code Components.ShowModal} (abre uma {@code Stage} nova — janela
 * separada do sistema operacional), este é um overlay embutido: fundo escurecido +
 * o conteúdo passado, centralizados, tudo dentro da MESMA janela. Pra isso
 * funcionar, o node raiz deste componente precisa ficar por CIMA do conteúdo
 * normal da tela na mesma árvore — use {@link megalodonte.components.layout_components.Stack}
 * pra empilhar os dois:
 * <pre>{@code
 * new Stack().children(
 *         conteudoNormalDaTela(),
 *         new Modal(vm.mostrarPromoState, promoContent())
 * );
 * }</pre>
 */
public class Modal extends Component {

    private final StackPane backdrop;
    private final StackPane cardStack;
    private final Region cardNode;
    private final Region contentNode;

    public Modal(State<Boolean> visible, Component content) {
        super(new StackPane());
        this.backdrop = (StackPane) node;

        backdrop.setAlignment(Pos.CENTER);
        updateBackgroundColor(backdrop, "rgba(0,0,0,0.55)");
        backdrop.setPickOnBounds(true);
        backdrop.setFocusTraversable(true);
        backdrop.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            if (e.getTarget() == backdrop) visible.set(false);
        });
        backdrop.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.ESCAPE && Boolean.TRUE.equals(visible.get())) {
                visible.set(false);
                event.consume();
            }
        });

        var closeButton = new Button("✕", new ButtonProps()
                .height(26)
                .fontSize(13)
                .bgColor("transparent")
                .textColor(ThemeManager.theme().colors().textPrimary())
        ).onClick(() -> visible.set(false));
        StackPane.setAlignment(closeButton.getJavaFxNode(), Pos.TOP_RIGHT);
        StackPane.setMargin(closeButton.getJavaFxNode(), new Insets(6, 6, 0, 0));

        var card = new Card(content);
        this.cardNode = (Region) card.getJavaFxNode();
        this.contentNode = content.getJavaFxNode() instanceof Region region ? region : null;
        this.cardStack = new StackPane(card.getJavaFxNode(), closeButton.getJavaFxNode());
        cardStack.setPickOnBounds(false);

        backdrop.getChildren().add(cardStack);

        applyVisibility(visible.get(), false);
        visible.subscribe(v -> applyVisibility(v, true));
    }

    /**
     * Keeps a large modal inside the window and lets scrollable content consume
     * the bounded viewport. Small modals keep their default content-sized card
     * unless this mode is requested explicitly.
     */
    public Modal fillAvailableSpace() {
        backdrop.setPadding(new Insets(24));
        cardStack.setMinSize(0, 0);
        cardStack.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        cardNode.setMinSize(0, 0);
        cardNode.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        if (contentNode != null) {
            contentNode.setMinSize(0, 0);
            contentNode.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
            VBox.setVgrow(contentNode, Priority.ALWAYS);
        }
        return this;
    }

    private void applyVisibility(boolean visible, boolean animate) {
        if (visible) {
            backdrop.setVisible(true);
            backdrop.setMouseTransparent(false);
            Platform.runLater(backdrop::requestFocus);
            if (animate) Animations.pop(this, true).play();
        } else {
            backdrop.setMouseTransparent(true);
            if (animate) {
                var anim = Animations.pop(this, false);
                anim.setOnFinished(e -> backdrop.setVisible(false));
                anim.play();
            } else {
                backdrop.setVisible(false);
            }
        }
    }
}
