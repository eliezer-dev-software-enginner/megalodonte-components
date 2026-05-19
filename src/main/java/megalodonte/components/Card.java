package megalodonte.components;

import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import megalodonte.props.CardProps;
import megalodonte.base.components.Component;
public class Card extends Component  {
    private final StackPane container;
    private CardProps cardProps;

    public Card(Component content) {
        this(content, new CardProps());
    }

    private void applySizeConstraints() {
        // O StackPane em si não deve crescer além do que props definiu
        this.container.setMinSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
        this.container.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);

        // O filho não deve tentar ocupar mais do que o StackPane oferece
        if (!container.getChildren().isEmpty()
                && container.getChildren().get(0) instanceof Region child) {
            child.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE); // deixa o StackPane controlar
            StackPane.setAlignment(child, javafx.geometry.Pos.TOP_LEFT); // evita centralização que estica
        }
    }
//    private void applySizeConstraints() {
//        this.container.setMinSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
//        this.container.setMaxSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
//
//        if (container.getChildren().size() > 0 && container.getChildren().get(0) instanceof Region child) {
//            child.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
//        }
//    }


    public Card(Component content, CardProps props) {
        super(new StackPane(), props);
        this.container = (StackPane) node;
        this.cardProps = props;

        this.container.getChildren().add(content.getNode());
        applySizeConstraints();
    }

    public CardProps props() {
        return cardProps;
    }

}
