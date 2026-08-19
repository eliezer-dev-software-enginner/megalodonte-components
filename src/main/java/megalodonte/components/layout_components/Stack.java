package megalodonte.components.layout_components;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import megalodonte.base.components.Component;

/**
 * Empilha os filhos por cima uns dos outros — o último filho adicionado fica no
 * topo (z-order), e cada filho preenche o espaço do pai por padrão (comportamento
 * nativo de {@link StackPane}). Usado, por exemplo, pra sobrepor um
 * {@link megalodonte.components.Modal} ao conteúdo normal de uma tela, os dois
 * dentro da mesma janela — sem isso, um Modal como overlay embutido não tem onde
 * "empilhar" por cima do resto da tela.
 */
public class Stack extends Component {
    private final StackPane stackPane;

    /** Canto onde um filho fica fixado via {@link #childInCorner}, sem esticar pra preencher. */
    public enum Corner { TOP_LEFT, TOP_RIGHT, BOTTOM_LEFT, BOTTOM_RIGHT }

    public Stack() {
        super(new StackPane());
        this.stackPane = (StackPane) node;
    }

    public Stack children(Component... components) {
        for (Component c : components) {
            stackPane.getChildren().add(c.getNode());
        }
        return this;
    }

    /**
     * Adiciona um filho fixado num canto do Stack — não estica pra preencher o espaço
     * disponível (diferente de {@link #children}), fica no próprio tamanho preferido,
     * ancorado naquele canto. Útil pra um botão flutuante (FAB) por cima de um conteúdo
     * scrollável, por exemplo.
     *
     * @param margin espaçamento (mesmo valor pras duas bordas que tocam o canto escolhido)
     */
    public Stack childInCorner(Component component, Corner corner, int margin) {
        var childNode = component.getNode();
        StackPane.setAlignment(childNode, toPos(corner));
        StackPane.setMargin(childNode, toInsets(corner, margin));
        stackPane.getChildren().add(childNode);
        return this;
    }

    /**
     * Deixa este Stack esticar verticalmente se o pai (uma VBox) oferecer mais espaço, em
     * vez de travar na altura preferida do conteúdo — mesmo raciocínio de
     * {@link megalodonte.v2.Show#fillHeight()}.
     */
    public Stack fillHeight() {
        stackPane.setMaxHeight(Double.MAX_VALUE);
        VBox.setVgrow(stackPane, Priority.ALWAYS);
        return this;
    }

    private static Pos toPos(Corner corner) {
        return switch (corner) {
            case TOP_LEFT -> Pos.TOP_LEFT;
            case TOP_RIGHT -> Pos.TOP_RIGHT;
            case BOTTOM_LEFT -> Pos.BOTTOM_LEFT;
            case BOTTOM_RIGHT -> Pos.BOTTOM_RIGHT;
        };
    }

    private static Insets toInsets(Corner corner, int margin) {
        double top = switch (corner) {
            case TOP_LEFT, TOP_RIGHT -> margin;
            case BOTTOM_LEFT, BOTTOM_RIGHT -> 0;
        };
        double bottom = switch (corner) {
            case BOTTOM_LEFT, BOTTOM_RIGHT -> margin;
            case TOP_LEFT, TOP_RIGHT -> 0;
        };
        double left = switch (corner) {
            case TOP_LEFT, BOTTOM_LEFT -> margin;
            case TOP_RIGHT, BOTTOM_RIGHT -> 0;
        };
        double right = switch (corner) {
            case TOP_RIGHT, BOTTOM_RIGHT -> margin;
            case TOP_LEFT, BOTTOM_LEFT -> 0;
        };
        return new Insets(top, right, bottom, left);
    }
}
