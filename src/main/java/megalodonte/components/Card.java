package megalodonte.components;

import javafx.scene.layout.*;
import megalodonte.props.CardProps;
import megalodonte.base.components.Component;

public class Card extends Component {
    private final VBox container;
    private CardProps cardProps;

    public Card(Component content) {
        this(content, new CardProps());
    }

    public Card(Component content, CardProps props) {
        super(new VBox(), props);
        this.container = (VBox) node;
        this.cardProps = props;

        this.container.getChildren().add(content.getNode());
        applySizeConstraints(props);
    }

    private void applySizeConstraints(CardProps props) {
        boolean fillWidth = props.hasFillWidth();
        boolean fixedWidth = props.hasFixedWidth();
        boolean fixedHeight = props.hasFixedHeight();
        boolean fillHeight = props.hasFillHeight();

        if (fillWidth) {
            this.container.setMaxWidth(Double.MAX_VALUE);
            HBox.setHgrow(this.container, Priority.ALWAYS); // se o pai do Card for HBox
        } else if (fixedWidth) {
            this.container.setMinWidth(Region.USE_PREF_SIZE);
            this.container.setMaxWidth(Region.USE_PREF_SIZE);
        } else {
            this.container.setMaxWidth(Double.MAX_VALUE); // pode crescer se o pai oferecer espaço, sem forçar
        }

        if (fixedHeight) {
            this.container.setMinHeight(Region.USE_PREF_SIZE);
            this.container.setMaxHeight(Region.USE_PREF_SIZE);
        } else if (fillHeight) {
            this.container.setMaxHeight(Double.MAX_VALUE);
            VBox.setVgrow(this.container, Priority.ALWAYS); // se o pai do Card for VBox
        }else{
            container.setMaxHeight(Region.USE_PREF_SIZE);

            // 2. Impede que uma VBox pai force este Container a crescer verticalmente
            VBox.setVgrow(container, Priority.NEVER);

            // 3. Opcional: impede que uma HBox pai force o crescimento vertical
            // (Por padrão, HBox estica a altura dos filhos se fillHeight for true)
            // 3. SOLUÇÃO PARA HBOX: Garante que o componente mantenha sua altura preferida
            // Mesmo que a HBox pai esteja configurada para esticar (fillHeight = true)
            container.setMinHeight(Region.USE_PREF_SIZE);
        }
        // hug content (nem fixedHeight nem fillHeight): não precisa setar nada,
        // VBox já reporta altura = altura do conteúdo por padrão.

        if (!container.getChildren().isEmpty()
                && container.getChildren().get(0) instanceof Region child) {
            child.setMaxWidth(Double.MAX_VALUE); // reforço defensivo, VBox.fillWidth já faz isso na maioria dos casos
            if (fillHeight) {
                child.setMaxHeight(Double.MAX_VALUE);
                VBox.setVgrow(child, Priority.ALWAYS); // conteúdo absorve o espaço extra dentro do Card
            } else {
                // AJUSTE DEFENSIVO: Se o Card é "hug content", o filho também não pode forçar crescimento
                VBox.setVgrow(child, Priority.NEVER);
            }
        }
    }

    public CardProps props() {
        return cardProps;
    }
}