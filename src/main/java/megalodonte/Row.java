package megalodonte;

import javafx.scene.layout.HBox;

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

    public Row child(Component component){
        this.nodeInternal.getChildren().add(component.getNode());
        return this;
    }
}
