package megalodonte.components;

import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.scene.Node;
import megalodonte.base.ComponentInterface;
import megalodonte.props.Props;

import java.util.function.Consumer;
import java.util.function.Function;

public abstract class Component implements ComponentInterface<Component> {
    protected final Node node;
    protected Props props;

    public Node getNode() {
        return node;
    }

    protected Component(Node node) {
        this.node = node;
    }

    protected Component(Node node, Props props) {
        this.node = node;
        setProps(node, props);
        animationHandler.apply(this);
    }

    private void setProps(Node node, Props props) {
        if(props != null){
            this.props = props;
            this.props.apply(node);
        }
    }

    @Override
    public Node getJavaFxNode() {
        return node;
    }

    @Override
    public Component fromJavaFxNode(Node newNode) {
        return CreateFromJavaFxNode(newNode);
    }

    /**
     * Factory method estático para criar um Component a partir de um Node JavaFX existente.
     * Este método segue o padrão Factory e cria um component wrapper.
     */
    public static Component CreateFromJavaFxNode(Node node) {
        if (node == null) {
            throw new IllegalArgumentException("Node can not be null");
        }
        
        // Wrapper component para um Node JavaFX existente
        return new Component(node) {
            @Override
            public Node getJavaFxNode() {
                return node; // Retorna o node original
            }

            @Override
            public Component fromJavaFxNode(Node newNode) {
                return CreateFromJavaFxNode(newNode); // Delega para o método estático
            }
        };
    }

    private Function<Component, Animation> animationHandler;
    public Component attachAnimation(Function<Component, Animation> animationHandler){
        this.animationHandler = animationHandler;
        return this;
    }
}