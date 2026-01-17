package megalodonte.components;

import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import megalodonte.props.ColumnProps;
import megalodonte.ForEachState;
import megalodonte.styles.ColumnStyler;

import java.util.List;

public class Column extends Component {
    private final VBox vBox;
    private VBox itemsVBox = null; // VBox separado para items dinâmicos

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
    
    /**
     * Adiciona componentes dinâmicos usando ForEachState
     * Usa VBox separado para isolar completamente os itens dinâmicos
     * 
     * @param forEachState o ForEachState contendo os componentes a renderizar
     * @return this para method chaining
     */
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

    /**
     * Creates a new Column component with default settings.
     * 
     * @return a new Column instance
     */
    public static Column of() {
        return new Column();
    }

    /**
     * Creates a new Column component with the specified properties.
     * 
     * @param props the column properties
     * @return a new Column instance
     */
    public static Column of(ColumnProps props) {
        return new Column(props);
    }

    /**
     * Creates a new Column component with the specified properties and styler.
     * 
     * @param props the column properties
     * @param styler the column styler
     * @return a new Column instance
     */
    public static Column of(ColumnProps props, ColumnStyler styler) {
        return new Column(props, styler);
    }
}
