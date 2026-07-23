package megalodonte.components.layout_components;

import javafx.animation.Animation;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Region;
import megalodonte.base.state.ForEachState;
import megalodonte.base.components.Component;
import megalodonte.props.FlowRowProps;
import megalodonte.props.Props;

import java.util.ArrayList;
import java.util.List;

public class FlowRow extends Component implements LayoutComponent {
    private final FlowPane nodeInternal;
    private final List<Node> reactiveItemNodes = new ArrayList<>();
    private final List<Node> scrollableReactiveItemNodes = new ArrayList<>();
    private FlowPane itemsFlowPane = null;
    private Component.Transition transition;

    public FlowRow() {
        this(new FlowRowProps());
    }

    public FlowRow(FlowRowProps props) {
        super(new FlowPane(), props);
        this.nodeInternal = (FlowPane) this.node;
        nodeInternal.setMaxHeight(Region.USE_PREF_SIZE);
    }

    public FlowRow r_child(Component component) {
        this.nodeInternal.getChildren().add(component.getNode());
        return this;
    }

    /**
     * Anima a entrada/saída de itens quando o ForEachState reativo (items(ForEachState))
     * adiciona ou remove cards — sem isso, o card só aparece/some instantaneamente
     * ("pop" seco). Use as animações prontas de {@link megalodonte.base.Animations},
     * ex: .withTransition(Animations::fadeScale).
     */
    public FlowRow withTransition(Component.Transition transition) {
        this.transition = transition;
        return this;
    }

    @Override
    public Props props() {
        return null;
    }

    @Override
    public FlowRow children(Component... components) {
        for (Component c : components) {
            r_child(c);
        }
        return this;
    }

    @Override
    public FlowRow items(List<? extends Component> components) {
        for (Component c : components) {
            r_child(c);
        }
        return this;
    }

    @Override
    public <T, C extends Component> FlowRow items(ForEachState<T, C> forEachState) {
        reconcileReactiveItems(nodeInternal, reactiveItemNodes, forEachState.getComponents());

        forEachState.getState().subscribe(newItems ->
                reconcileReactiveItems(nodeInternal, reactiveItemNodes, forEachState.getComponents()));

        return this;
    }

    /**
     * Reconcilia os filhos reativos de um FlowPane por identidade de Node em vez de
     * limpar tudo e recriar: só os itens que realmente entraram/saíram da lista são
     * adicionados/removidos (e animados, se houver transition setada); itens que
     * continuam presentes ficam intocados. Preserva filhos estáticos adicionados via
     * children()/r_child() e continuam sendo filhos diretos do pane (sem sub-pane
     * aninhado), pra não quebrar o wrap real entre eles e o restante do conteúdo.
     */
    private void reconcileReactiveItems(FlowPane pane, List<Node> tracked, List<? extends Component> newComponents) {
        List<Node> newNodes = new ArrayList<>(newComponents.size());
        for (Component c : newComponents) {
            newNodes.add(c.getNode());
        }

        List<Node> removed = new ArrayList<>(tracked);
        removed.removeAll(newNodes);
        for (Node n : removed) {
            tracked.remove(n);
            if (transition != null) {
                Animation anim = transition.play(Component.CreateFromJavaFxNode(n), false);
                if (anim != null) {
                    anim.setOnFinished(e -> pane.getChildren().remove(n));
                    anim.play();
                    continue;
                }
            }
            pane.getChildren().remove(n);
        }

        for (Component c : newComponents) {
            Node n = c.getNode();
            if (tracked.contains(n)) continue;

            tracked.add(n);
            pane.getChildren().add(n);
            playEntering(c);
        }
    }

    /**
     * Toca a animação de entrada só quando o node já estiver numa Scene viva.
     * render() roda inteiro antes da árvore ser anexada à Stage (ver Router), então
     * tocar a animação na hora — nesse momento sem Scene — faz ela rodar e terminar
     * em segundo plano antes da tela aparecer, e o usuário nunca vê nada.
     */
    private void playEntering(Component c) {
        if (transition == null) return;

        Node n = c.getNode();
        if (n.getScene() != null) {
            Animation anim = transition.play(c, true);
            if (anim != null) anim.play();
            return;
        }

        n.sceneProperty().addListener(new javafx.beans.value.ChangeListener<>() {
            @Override
            public void changed(javafx.beans.value.ObservableValue<? extends javafx.scene.Scene> obs,
                                 javafx.scene.Scene oldScene, javafx.scene.Scene newScene) {
                if (newScene == null) return;
                n.sceneProperty().removeListener(this);
                Animation anim = transition.play(c, true);
                if (anim != null) anim.play();
            }
        });
    }

    @Override
    public <T, C extends Component> FlowRow items(ForEachState<T, C> forEachState, boolean isScrollable) {
        if (this.itemsFlowPane != null) {
            throw new IllegalStateException("items() só pode ser chamado uma vez por FlowRow");
        }

        this.itemsFlowPane = new FlowPane();
        itemsFlowPane.setHgap(nodeInternal.getHgap());
        itemsFlowPane.setVgap(nodeInternal.getVgap());
        itemsFlowPane.setMaxWidth(Double.MAX_VALUE);
        itemsFlowPane.prefWrapLengthProperty().bind(itemsFlowPane.widthProperty());

        if (isScrollable) {
            ScrollPane scrollPane = new ScrollPane(itemsFlowPane);
            scrollPane.setFitToWidth(true);
            scrollPane.setFitToHeight(false);
            scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
            scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
            this.nodeInternal.getChildren().add(scrollPane);
        } else {
            this.nodeInternal.getChildren().add(itemsFlowPane);
        }

        reconcileReactiveItems(itemsFlowPane, scrollableReactiveItemNodes, forEachState.getComponents());

        forEachState.getState().subscribe(newItems ->
                reconcileReactiveItems(itemsFlowPane, scrollableReactiveItemNodes, forEachState.getComponents()));

        return this;
    }
}