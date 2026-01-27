package megalodonte.components;

import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import megalodonte.props.CardProps;
import megalodonte.styles.CardStyler;

public class Scroll extends Component {

    private final ScrollPane scrollPane;

    public Scroll(Component content) {
        //super(new ScrollPane(), new CardProps(), new CardStyler());
        super(new ScrollPane(), null, null);
        this.scrollPane = (ScrollPane) node;
        defaultConfig();
    }


//    public Scroll props(CardProps props) {
//        super(new ScrollPane(), props, new CardStyler());
//        this.scrollPane = (ScrollPane) node;
//        defaultConfig();
//
//        return this;
//    }

//    public Scroll(Component content, CardProps props, CardStyler styler) {
//        super(new StackPane(), props, styler);
//        this.container = (StackPane) node;
//        impedirCrescimentoAutomaticoDoFilho();
//
//        this.container.getChildren().add(content.getNode());
//    }

    private void defaultConfig(){
        this.scrollPane.setStyle("-fx-background-color: transparent;-fx-border-color: transparent;");
        VBox.setVgrow(scrollPane, Priority.ALWAYS);
        this.scrollPane.setFitToWidth(true);
        this.scrollPane.setContent(getJavaFxNode());

    }
}
