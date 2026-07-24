package megalodonte.components.layout_components;

import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import megalodonte.base.components.Component;
import megalodonte.components.SpacerVertical;
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

        // Por padrão, o Container "abraça" o conteúdo (não cresce além da altura
        // preferida dos filhos, nem aceita esticar se o pai oferecer mais espaço).
        // Quando fillHeight() foi pedido nas props, ContainerProps.applyTheme já
        // configurou o oposto (Vgrow ALWAYS + maxHeight ilimitado) — não pisamos
        // nisso aqui.
        if (!containerProps.hasFillHeight()) {
            container.setMaxHeight(Region.USE_PREF_SIZE);
            VBox.setVgrow(container, Priority.NEVER);
            container.setMinHeight(Region.USE_PREF_SIZE);
        }
    }

    public Container c_child(Component component){
        this.container.getChildren().add(component.getNode());

        if (component instanceof SpacerVertical c) {
            VBox.setVgrow(c.getNode(), Priority.ALWAYS);
        }

        // Reforça o "não cresce" a cada filho adicionado — mas só quando fillHeight()
        // não foi pedido, senão isso desfaria o Vgrow ALWAYS aplicado nas props.
        if (!containerProps.hasFillHeight()) {
            VBox.setVgrow(this.container, Priority.NEVER);
        }

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
