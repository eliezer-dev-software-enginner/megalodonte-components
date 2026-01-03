package megalodonte.components;

import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import megalodonte.props.CardProps;
import megalodonte.styles.CardStyler;

public class Card extends Component {

    private final StackPane container;

    public Card(Component content) {
        super(new StackPane(), new CardProps(), new CardStyler());

        this.container = (StackPane) node;

        impedirCrescimentoAutomaticoDoFilho();

        this.container.getChildren().add(content.getNode());
    }

    private void impedirCrescimentoAutomaticoDoFilho() {
        //impedir o crescimento automático do Region.
        this.container.setMaxSize(
                Region.USE_PREF_SIZE,
                Region.USE_PREF_SIZE
        );
    }

    public Card(Component content, CardProps props) {
        super(new StackPane(), props, new CardStyler());
        this.container = (StackPane) node;
        impedirCrescimentoAutomaticoDoFilho();

        this.container.getChildren().add(content.getNode());
    }

    public Card(Component content, CardProps props, CardStyler styler) {
        super(new StackPane(), props, styler);
        this.container = (StackPane) node;
        impedirCrescimentoAutomaticoDoFilho();

        this.container.getChildren().add(content.getNode());
    }
}
