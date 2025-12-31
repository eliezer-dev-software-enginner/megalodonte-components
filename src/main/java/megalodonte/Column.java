package megalodonte;

import javafx.scene.layout.VBox;

public class Column extends Component {
    private final VBox vBox;

    public Column(){
        super(new VBox());
        this.vBox = (VBox) this.node;
    }

    public Column(ColumnProps props){
        super(new VBox(), props);
        this.vBox = (VBox) this.node;
    }

    public Column(ColumnProps props, ColumnStyler styler){
        super(new VBox(), props, styler);
        this.vBox = (VBox) this.node;
    }

    public Column child(Component component){
        this.vBox.getChildren().add(component.getNode());
        return this;
    }
}
