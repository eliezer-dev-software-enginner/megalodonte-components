package megalodonte.components;

import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import megalodonte.props.CardProps;

public class Card extends Component {
    private final StackPane container;
    private CardProps cardProps;

    public Card(Component content) {
        this(content, new CardProps());
    }

    private void impedirCrescimentoAutomaticoDoFilho() {
        this.container.setMaxSize(
                Region.USE_PREF_SIZE,
                Region.USE_PREF_SIZE
        );
    }


    public Card(Component content, CardProps props) {
        super(new StackPane(), props);
        this.container = (StackPane) node;
        this.cardProps = props;
        impedirCrescimentoAutomaticoDoFilho();

        this.container.getChildren().add(content.getNode());
    }

    public CardProps props() {
        return cardProps;
    }

}
