package megalodonte.components;

import javafx.geometry.Insets;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import megalodonte.props.ButtonProps;
import megalodonte.props.ColumnProps;
import megalodonte.ForEachState;

import java.util.List;

public class Column extends Component {
    private final VBox vBox;
    private VBox itemsVBox = null;
    private final ColumnProps columnProps;

    public Column(){
        this(new ColumnProps());
    }

    public Column(ColumnProps props){
        super(new VBox(), props);
        this.vBox = (VBox) this.node;
        this.columnProps = props;
    }

    public Column c_child(Component component){
        this.vBox.getChildren().add(component.getNode());

        if (component instanceof SpacerVertical c) {
            VBox.setVgrow(c.getNode(), Priority.ALWAYS);
        }

        if (component.props instanceof ButtonProps buttonProps) {
            Insets margins = buttonProps.getMargins();
            if (margins != null) {
                VBox.setMargin(component.getNode(), margins);
            }
        }

        return this;
    }
    
    public Column c_childs(Component... components){
        for (Component c : components) {
            c_child(c);
        }
        return this;
    }
    
    public <T, C extends Component> Column items(ForEachState<T, C> forEachState) {
        // Só pode ser chamado uma vez por Column
        if (this.itemsVBox != null) {
            throw new IllegalStateException("items() só pode ser chamado uma vez por Column");
        }
        
        // Cria VBox separado para os itens dinâmicos
        this.itemsVBox = new VBox();
        this.vBox.getChildren().add(this.itemsVBox);
        
        // Adiciona os componentes iniciais
        List<C> componentes = forEachState.getComponents();
        for (C component : componentes) {
            this.itemsVBox.getChildren().add(component.getNode());
        }
        
        // Se inscreve para atualizar automaticamente quando o estado mudar
        forEachState.getState().subscribe(newItems -> {
            // Limpa completamente o VBox dos itens
            this.itemsVBox.getChildren().clear();
            
            // Adiciona os componentes atualizados
            List<C> novosComponentes = forEachState.getComponents();
            for (C component : novosComponentes) {
                this.itemsVBox.getChildren().add(component.getNode());
            }
        });
        
        return this;
    }

    public ColumnProps props() {
        return columnProps;
    }
}
