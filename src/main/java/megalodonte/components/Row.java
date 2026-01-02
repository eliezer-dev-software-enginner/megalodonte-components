package megalodonte.components;

import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import megalodonte.RowProps;
import megalodonte.RowStyler;

public class Row extends Component {
    private final HBox nodeInternal;

    public Row(){
        super(new HBox());
        this.nodeInternal = (HBox) this.node;
    }

    public Row(RowProps props){
        super(new HBox(), props);
        this.nodeInternal = (HBox) this.node;
    }

    public Row(RowProps props, RowStyler styler){
        super(new HBox(), props, styler);
        this.nodeInternal = (HBox) this.node;
    }

    public Row r_child(Component component){
        this.nodeInternal.getChildren().add(component.getNode());

        if (component instanceof SpacerHorizontal c) {
            HBox.setHgrow(c.getNode(), Priority.ALWAYS);
        }

        return this;
    }
}
