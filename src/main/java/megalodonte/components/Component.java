package megalodonte.components;

import javafx.scene.Node;
import megalodonte.Estilizador;
import megalodonte.Props;

public abstract class Component {
    protected final Node node;
    protected Props props;
    protected Estilizador styler;

    public Node getNode() {
        return node;
    }

    protected Component(Node node) {
        this.node = node;
    }

    protected Component(Node node, Props props) {
        this.node = node;

        setProps(node, props);
    }

    protected Component(Node node, Props props, Estilizador styler) {
        this.node = node;

        setProps(node, props);

        if(styler != null){
            this.styler = styler;
            this.styler.apply(node);
        }
    }

    private void setProps(Node node, Props props) {
        if(props != null){
            this.props = props;
            this.props.apply(node);
        }
    }

    public static Component FromJavaFxNode(Node node){
        //classe anonima
        return new Component(node){};
    }
}
