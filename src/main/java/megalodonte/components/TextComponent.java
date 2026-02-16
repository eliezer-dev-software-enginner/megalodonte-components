package megalodonte.components;

import javafx.scene.Node;
import megalodonte.props.Props;
import megalodonte.styles.Estilizador;

public abstract class TextComponent<T extends Props, S extends Estilizador<T>> extends Component {
    protected T props;
    protected S styler;
    protected Runnable onClickHandler;

    protected TextComponent(Node node) {
        super(node);
    }

    protected TextComponent(Node node, T props) {
        super(node, props);
        this.props = props;
    }

    protected TextComponent(Node node, T props, S styler) {
        super(node, props, styler);
        this.props = props;
        this.styler = styler;
    }

    public <C extends TextComponent<T, S>> C onClick(Runnable handler) {
        this.onClickHandler = handler;
        applyOnClick();
        return (C) this;
    }

    private void applyOnClick() {
        if (node instanceof javafx.scene.control.Button button && onClickHandler != null) {
            button.setOnMouseClicked(e -> onClickHandler.run());
        }
    }
}
