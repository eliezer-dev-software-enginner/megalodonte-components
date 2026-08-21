package megalodonte.components;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
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

    public Modal(State<Boolean> visible, Component content) {
        super(new StackPane());
        this.backdrop = (StackPane) node;

        backdrop.setAlignment(Pos.CENTER);
        updateBackgroundColor(backdrop, "rgba(0,0,0,0.55)");
        backdrop.setPickOnBounds(true);
        backdrop.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            if (e.getTarget() == backdrop) visible.set(false);
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
        var cardStack = new StackPane(card.getJavaFxNode(), closeButton.getJavaFxNode());
        cardStack.setPickOnBounds(false);

        backdrop.getChildren().add(cardStack);

        applyVisibility(visible.get(), false);
        visible.subscribe(v -> applyVisibility(v, true));
    }

    private void applyVisibility(boolean visible, boolean animate) {
        if (visible) {
            backdrop.setVisible(true);
            backdrop.setMouseTransparent(false);
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
