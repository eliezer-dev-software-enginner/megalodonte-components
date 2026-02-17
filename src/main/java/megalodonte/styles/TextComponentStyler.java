package megalodonte.styles;

import javafx.scene.Node;
import megalodonte.utils.Utils;

public abstract class TextComponentStyler<T extends megalodonte.props.TextComponentProps, S extends TextComponentStyler<T, S>> extends Estilizador<T>{
    protected String textColor;

    public S color(String color) {
        this.textColor = color;
        return (S) this;
    }

    public S textColor(String color) {
        this.textColor = color;
        return (S) this;
    }

    protected void applyColor(Node node, String color, String fxField) {
        var current = node.getStyle();
        var updated = Utils.UpdateEspecificStyle(current, fxField, color);
        node.setStyle(updated);
    }
}
