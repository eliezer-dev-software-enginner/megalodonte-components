package megalodonte.components.layout_components;

import javafx.scene.layout.StackPane;
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
}
