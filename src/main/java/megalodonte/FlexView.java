package megalodonte;

import javafx.scene.layout.TilePane;

public class FlexView extends Component {
    private final TilePane tilePane;

    public FlexView(){
        super(new TilePane());
        this.tilePane = (TilePane) this.node;
    }

    public FlexView(FlexViewProps props){
        super(new TilePane(), props);
        this.tilePane = (TilePane) this.node;
    }

    public FlexView(FlexViewProps props, FlexViewStyler styler){
        super(new TilePane(), props, styler);
        this.tilePane = (TilePane) this.node;
    }

    public FlexView child(Component component){
        this.tilePane.getChildren().add(component.getNode());
        return this;
    }

    public <T> FlexView items(
            Iterable<T> items,
            Renderer<T> renderer
    ) {
        for (T item : items) {
            Component c = renderer.render(item);
            tilePane.getChildren().add(c.getNode());
        }
        return this;
    }
}
