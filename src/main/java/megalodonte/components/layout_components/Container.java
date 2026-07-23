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

        // 1. Limita a altura ao tamanho dos filhos
        container.setMaxHeight(Region.USE_PREF_SIZE);

        // 2. Impede que uma VBox pai force este Container a crescer verticalmente
        VBox.setVgrow(container, Priority.NEVER);

        // 3. Opcional: impede que uma HBox pai force o crescimento vertical
        // (Por padrão, HBox estica a altura dos filhos se fillHeight for true)
        // 3. SOLUÇÃO PARA HBOX: Garante que o componente mantenha sua altura preferida
        // Mesmo que a HBox pai esteja configurada para esticar (fillHeight = true)
        container.setMinHeight(Region.USE_PREF_SIZE);
    }

    public Container c_child(Component component){
        this.container.getChildren().add(component.getNode());

        if (component instanceof SpacerVertical c) {
            VBox.setVgrow(c.getNode(), Priority.ALWAYS);
        }

        // Garante que este container, quando colocado dentro de OUTRA VBox, também não cresça
        VBox.setVgrow(this.container, Priority.NEVER);

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
