package megalodonte.components;

public class TextFlow extends Component {
    private final javafx.scene.text.TextFlow container;

    public TextFlow(Component content) {
        super((new javafx.scene.text.TextFlow()), null);
        
        this.container = (javafx.scene.text.TextFlow) node;
        //setupContainerBehavior();
        
        // Adicionar conteúdo
        this.container.getChildren().add(content.getNode());
    }

//    private void setupContainerBehavior() {
//        // Impedir crescimento automático do Region
//        container.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
//
//        // Tornar clicável
//        container.setPickOnBounds(true);
//    }

}