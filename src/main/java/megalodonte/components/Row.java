package megalodonte.components;

import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import megalodonte.props.RowProps;
import megalodonte.styles.RowStyler;

public class Row extends Component {
    private final HBox nodeInternal;
    private RowProps rowProps;
    private RowStyler rowStyler;

    public Row(){
        this(new RowProps(), new RowStyler());
    }

    public Row(RowProps props){
        this(props, new RowStyler());
    }

    public Row(RowProps props, RowStyler styler){
        super(new HBox(), props, styler);
        this.nodeInternal = (HBox) this.node;
        this.rowProps = props;
        this.rowStyler = styler;
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
    
    public Row r_child(Component... components){
        for (Component c : components) {
            r_child(c);
        }
        return this;
    }

    public RowProps props() {
        return rowProps;
    }

    public RowStyler style() {
        return rowStyler;
    }
}
