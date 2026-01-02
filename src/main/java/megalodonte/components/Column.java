package megalodonte.components;

import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import megalodonte.ColumnProps;
import megalodonte.ColumnStyler;

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

    public Column c_child(Component component){
        this.vBox.getChildren().add(component.getNode());

        if (component instanceof SpacerVertical c) {
            VBox.setVgrow(c.getNode(), Priority.ALWAYS);
        }
        return this;
    }
}
