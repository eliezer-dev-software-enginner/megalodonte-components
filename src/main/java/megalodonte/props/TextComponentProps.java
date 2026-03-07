package megalodonte.props;

import javafx.scene.Node;
import megalodonte.ReadableState;
import megalodonte.utils.Utils;

public abstract class TextComponentProps<T extends TextComponentProps<T>> extends Props {
    protected Integer fontSize;
    protected ReadableState<Integer> fontSizeState;
    protected String fontWeight;
    protected String textColor;

    public T fontSize(int fontSize) {
        this.fontSize = fontSize;
        return (T) this;
    }

    public T fontSize(ReadableState<Integer> state) {
        this.fontSizeState = state;
        return (T) this;
    }

    public T bold() {
        this.fontWeight = "bold";
        return (T) this;
    }

    public T fontWeight(String weight) {
        this.fontWeight = weight;
        return (T) this;
    }

    public Integer getFontSize() {
        return fontSize;
    }

    public ReadableState<Integer> getFontSizeState() {
        return fontSizeState;
    }

    public String getFontWeight() {
        return fontWeight;
    }


    public T color(String color) {
        this.textColor = color;
        return (T) this;
    }

    public T textColor(String color) {
        this.textColor = color;
        return (T) this;
    }

    protected void applyColor(Node node, String color, String fxField) {
        var current = node.getStyle();
        var updated = Utils.UpdateEspecificStyle(current, fxField, color);
        node.setStyle(updated);
    }
}
