package megalodonte.components;

import javafx.scene.layout.StackPane;
import megalodonte.props.CardProps;
import megalodonte.styles.CardStyler;

public class Card extends Component {

    private final StackPane container;

    public Card(Component content) {
        super(new StackPane(), new CardProps(), new CardStyler());

        this.container = (StackPane) node;
        this.container.getChildren().add(content.getNode());
    }

    public Card(Component content, CardProps props) {
        super(new StackPane(), props, new CardStyler());
        this.container = (StackPane) node;
        this.container.getChildren().add(content.getNode());
    }

    public Card(Component content, CardProps props, CardStyler styler) {
        super(new StackPane(), props, styler);
        this.container = (StackPane) node;
        this.container.getChildren().add(content.getNode());
    }
}
