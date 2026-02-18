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

    private void applySizeConstraints() {
        this.container.setMinSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
        this.container.setMaxSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
        
        if (container.getChildren().size() > 0 && container.getChildren().get(0) instanceof Region child) {
            child.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
        }
    }


    public Card(Component content, CardProps props) {
        super(new StackPane(), props);
        this.container = (StackPane) node;
        this.cardProps = props;
        applySizeConstraints();

        this.container.getChildren().add(content.getNode());
    }

    public CardProps props() {
        return cardProps;
    }

}
