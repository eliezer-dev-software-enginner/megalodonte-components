package megalodonte.components;

import javafx.scene.Node;
import megalodonte.props.Props;


public abstract class TextComponent<T extends Props> extends Component {
    protected T props;
    protected Runnable onClickHandler;

    protected TextComponent(Node node) {
        super(node);
    }

    protected TextComponent(Node node, T props) {
        super(node, props);
        this.props = props;
    }

    public <C extends TextComponent<T>> C onClick(Runnable handler) {
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
