package megalodonte.components;

import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import megalodonte.props.RowProps;

public class Row extends Component {
    private final HBox nodeInternal;
    private RowProps rowProps;

    public Row(){
        this(new RowProps());
    }

    public Row(RowProps props){
        super(new HBox(), props);
        this.nodeInternal = (HBox) this.node;
        this.rowProps = props;
    }

    public Row r_child(Component component){
        this.nodeInternal.getChildren().add(component.getNode());

        if (component instanceof SpacerHorizontal c) {
            HBox.setHgrow(c.getNode(), Priority.ALWAYS);
        }

        if (component.props instanceof megalodonte.props.ButtonProps buttonProps) {
            javafx.geometry.Insets margins = buttonProps.getMargins();
            if (margins != null) {
                HBox.setMargin(component.getNode(), margins);
            }
        }

        return this;
    }
    
    public Row r_childs(Component... components){
        for (Component c : components) {
            r_child(c);
        }
        return this;
    }

    public RowProps props() {
        return rowProps;
    }
}
