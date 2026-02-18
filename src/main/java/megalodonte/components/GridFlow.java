package megalodonte.components;

import javafx.scene.layout.TilePane;
import megalodonte.props.GridFlowProps;

//Problemático
public class GridFlow extends Component {
    private final TilePane tilePane;

    public GridFlow(){
        super(new TilePane());
        this.tilePane = (TilePane) this.node;
    }

    public GridFlow(GridFlowProps props){
        super(new TilePane(), props);
        this.tilePane = (TilePane) this.node;
    }

    public GridFlow child(Component component){
        this.tilePane.getChildren().add(component.getNode());
        return this;
    }

    public <T> GridFlow items(
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
