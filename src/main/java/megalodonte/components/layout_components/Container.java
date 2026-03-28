package megalodonte.components.layout_components;

import javafx.scene.layout.VBox;
import megalodonte.base.Component;
import megalodonte.props.ContainerProps;

public class Container extends Component {
    private final VBox container;
    private VBox itemsVBox = null;
    private final ContainerProps containerProps;

    public Container(){
        this(new ContainerProps());
    }

    public Container(ContainerProps props){
        super(new VBox(), props);
        this.container = (VBox) this.node;
        this.containerProps = props;
    }

    public Container c_child(Component component){
        this.container.getChildren().add(component.getNode());

//        if (component instanceof SpacerVertical c) {
//            VBox.setVgrow(c.getNode(), Priority.ALWAYS);
//        }

//        if (component.props instanceof ButtonProps buttonProps) {
//            Insets margins = buttonProps.getMargins();
//            if (margins != null) {
//                VBox.setMargin(component.getNode(), margins);
//            }
//        }

        return this;
    }

    public ContainerProps props() {
        return containerProps;
    }

    public Container children(Component... components) {
        for (Component c : components) {
            c_child(c);
        }
        return this;
    }
}
