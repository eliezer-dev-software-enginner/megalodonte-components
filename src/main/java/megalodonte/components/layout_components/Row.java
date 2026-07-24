package megalodonte.components.layout_components;

import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import megalodonte.base.state.ForEachState;
import megalodonte.base.components.Component;
import megalodonte.components.SpacerHorizontal;
import megalodonte.props.RowProps;

import java.util.List;

public class Row extends Component  implements LayoutComponent {
    private final HBox nodeInternal;
    private HBox itemsHBox = null;
    private RowProps rowProps;

    public Row(){
        this(new RowProps());
    }

    public Row(RowProps props){
        super(new HBox(), props);
        this.nodeInternal = (HBox) this.node;
        this.rowProps = props;

        // 1. Ocupa toda a largura disponível (Largura Máxima Infinita)
        this.nodeInternal.setMaxWidth(Double.MAX_VALUE);

        // 2. Trava a altura vertical estritamente no tamanho calculado dos filhos
        this.nodeInternal.setMinHeight(Region.USE_PREF_SIZE);
        this.nodeInternal.setMaxHeight(Region.USE_PREF_SIZE);

        // 3. Garante que se o pai for uma VBox, ele nunca vai esticar esta Row verticalmente
        VBox.setVgrow(this.nodeInternal, Priority.NEVER);
    }

    public Row r_child(Component component){
        this.nodeInternal.getChildren().add(component.getNode());

        if (component instanceof SpacerHorizontal c) {
            HBox.setHgrow(c.getNode(), Priority.ALWAYS);
        }

//        if (component.props instanceof megalodonte.props.ButtonProps buttonProps) {
//            javafx.geometry.Insets margins = buttonProps.getMargins();
//            if (margins != null) {
//                HBox.setMargin(component.getNode(), margins);
//            }
//        }

        return this;
    }

    public RowProps props() {
        return rowProps;
    }

    @Override
    public Row children(Component... components) {
        for (Component c : components) {
            r_child(c);
        }
        return this;
    }

    @Override
    public Row items(List<? extends Component> components) {
        for (Component c : components) {
            r_child(c);
        }
        return this;
    }

    @Override
    public <T, C extends Component > Row items(ForEachState<T, C> forEachState) {
        if (this.itemsHBox != null) {
            throw new IllegalStateException("items() só pode ser chamado uma vez por Row");
        }

        this.itemsHBox = new HBox();
        this.nodeInternal.getChildren().add(this.itemsHBox);

        List<C> componentes = forEachState.getComponents();
        for (C component : componentes) {
            this.itemsHBox.getChildren().add(component.getNode());
        }

        forEachState.getState().subscribe(newItems -> {
            this.itemsHBox.getChildren().clear();
            List<C> novosComponentes = forEachState.getComponents();
            for (C component : novosComponentes) {
                this.itemsHBox.getChildren().add(component.getNode());
            }
        });

        return this;
    }

    @Override
    public <T, C extends Component > Row items(ForEachState<T, C> forEachState, boolean isScrollable) {
        if (this.itemsHBox != null) {
            throw new IllegalStateException("items() só pode ser chamado uma vez por Row");
        }

        this.itemsHBox = new HBox();

        if (isScrollable) {
            ScrollPane scrollPane = new ScrollPane(this.itemsHBox);
            scrollPane.setFitToHeight(true);
            scrollPane.setFitToWidth(false);
            scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
            scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

            // Se a Row deve ser estática e justa, remova o Hgrow ALWAYS:
            // HBox.setHgrow(scrollPane, Priority.ALWAYS);

            this.nodeInternal.getChildren().add(scrollPane);
        }

//        if (isScrollable) {
//            ScrollPane scrollPane = new ScrollPane(this.itemsHBox);
//            scrollPane.setFitToHeight(true);
//            scrollPane.setFitToWidth(false);
//            scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
//            scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
//            HBox.setHgrow(scrollPane, Priority.ALWAYS);
//            this.nodeInternal.getChildren().add(scrollPane);
//        }
        else {
            this.nodeInternal.getChildren().add(this.itemsHBox);
        }

        List<C> componentes = forEachState.getComponents();
        for (C component : componentes) {
            this.itemsHBox.getChildren().add(component.getNode());
        }

        forEachState.getState().subscribe(newItems -> {
            this.itemsHBox.getChildren().clear();
            List<C> novosComponentes = forEachState.getComponents();
            for (C component : novosComponentes) {
                this.itemsHBox.getChildren().add(component.getNode());
            }
        });

        return this;
    }
}
