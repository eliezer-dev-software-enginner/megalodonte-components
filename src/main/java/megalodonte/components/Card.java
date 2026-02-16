package megalodonte.components;

import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import megalodonte.props.CardProps;
import megalodonte.styles.CardStyler;

public class Card extends Component {

    private final StackPane container;
    private CardProps cardProps;
    private CardStyler cardStyler;

    public Card(Component content) {
        this(content, new CardProps(), new CardStyler());
    }

    private void impedirCrescimentoAutomaticoDoFilho() {
        this.container.setMaxSize(
                Region.USE_PREF_SIZE,
                Region.USE_PREF_SIZE
        );
    }

    public Card(Component content, CardProps props) {
        this(content, props, new CardStyler());
    }

    public Card(Component content, CardProps props, CardStyler styler) {
        super(new StackPane(), props, styler);
        this.container = (StackPane) node;
        this.cardProps = props;
        this.cardStyler = styler;
        impedirCrescimentoAutomaticoDoFilho();

        this.container.getChildren().add(content.getNode());
    }

    public CardProps props() {
        return cardProps;
    }

    public CardStyler style() {
        return cardStyler;
    }
}
