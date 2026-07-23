package megalodonte.components;

import javafx.scene.layout.*;
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
        applySizeConstraints(props);
    }

    private void applySizeConstraints(CardProps props) {
        boolean fillWidth = props.hasFillWidth();
        boolean fixedWidth = props.hasFixedWidth();
        boolean fixedHeight = props.hasFixedHeight();
        boolean fillHeight = props.hasFillHeight();

        if (fillWidth) {
            this.container.setMinWidth(Region.USE_COMPUTED_SIZE);
            this.container.setPrefWidth(Region.USE_COMPUTED_SIZE);
            this.container.setMaxWidth(Double.MAX_VALUE);
            HBox.setHgrow(this.container, Priority.ALWAYS);
        } else if (fixedWidth) {
            this.container.setMinWidth(Region.USE_PREF_SIZE);
            this.container.setMaxWidth(Region.USE_PREF_SIZE);
        } else {
            this.container.setMaxWidth(Double.MAX_VALUE);
        }

        if (fixedHeight) {
            this.container.setMinHeight(Region.USE_PREF_SIZE);
            this.container.setMaxHeight(Region.USE_PREF_SIZE);
        } else if (fillHeight) {
            this.container.setMinHeight(Region.USE_COMPUTED_SIZE);
            this.container.setMaxHeight(Double.MAX_VALUE);
            VBox.setVgrow(this.container, Priority.ALWAYS); // se o pai for VBox
        } else {
            // hug content: não deixa esticar além do necessário
            this.container.setMinHeight(Region.USE_COMPUTED_SIZE);
            this.container.setMaxHeight(Region.USE_COMPUTED_SIZE);
        }

        if (!container.getChildren().isEmpty()
                && container.getChildren().get(0) instanceof Region child) {
            // não força o filho a MAX_VALUE em altura por padrão — só se o Card pedir fillHeight
            child.setMaxWidth(Double.MAX_VALUE);
            if (fillHeight) {
                child.setMaxHeight(Double.MAX_VALUE);
            } else {
                child.setMaxHeight(Region.USE_COMPUTED_SIZE);
            }
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
