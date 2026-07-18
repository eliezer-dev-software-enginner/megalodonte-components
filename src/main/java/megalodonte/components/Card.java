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

    public Card(Component content, CardProps props) {
        super(new StackPane(), props);
        this.container = (StackPane) node;
        this.cardProps = props;

        this.container.getChildren().add(content.getNode());
        applySizeConstraints(props.hasFixedWidth(), props.hasFixedHeight());
    }



    private void applySizeConstraints(boolean fixedWidth, boolean fixedHeight) {
        if (fixedWidth) {
            this.container.setMinWidth(Region.USE_PREF_SIZE);
            this.container.setMaxWidth(Region.USE_PREF_SIZE);
        } else {
            this.container.setMaxWidth(Double.MAX_VALUE); // aceita a largura que o pai oferecer
        }

        if (fixedHeight) {
            this.container.setMinHeight(Region.USE_PREF_SIZE);
            this.container.setMaxHeight(Region.USE_PREF_SIZE);
        } else {
            this.container.setMaxHeight(Double.MAX_VALUE);
        }

        if (!container.getChildren().isEmpty()
                && container.getChildren().get(0) instanceof Region child) {
            child.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
            StackPane.setAlignment(child, javafx.geometry.Pos.TOP_LEFT);
        }
    }
//    private void applySizeConstraints() {
//        // O StackPane em si não deve crescer além do que props definiu
//        this.container.setMinSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
//        this.container.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
//
//        // O filho não deve tentar ocupar mais do que o StackPane oferece
//        if (!container.getChildren().isEmpty()
//                && container.getChildren().get(0) instanceof Region child) {
//            child.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE); // deixa o StackPane controlar
//            StackPane.setAlignment(child, javafx.geometry.Pos.TOP_LEFT); // evita centralização que estica
//        }
//    }


    public CardProps props() {
        return cardProps;
    }

}
