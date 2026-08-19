package megalodonte.props;

import javafx.scene.Node;
import megalodonte.base.state.ReadableState;

import static megalodonte.styles.util.StyleUtils.applyStyleProperty;

public abstract class TextComponentProps<T extends TextComponentProps<T>> extends Props {
    protected Integer fontSize;
    protected ReadableState<Integer> fontSizeState;
    protected String fontWeight;
    protected String textColor;
    protected String fontFamily;

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

    /** Ex.: {@code "'Courier New', monospace"} — qualquer valor CSS válido de font-family. */
    public T fontFamily(String fontFamily) {
        this.fontFamily = fontFamily;
        return (T) this;
    }

    public String getFontFamily() {
        return fontFamily;
    }

    protected void applyFontFamily(Node node) {
        if (fontFamily != null && !fontFamily.isBlank()) {
            applyStyleProperty(node, fontFamily, "-fx-font-family");
        }
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
        applyStyleProperty(node, color, fxField);
    }
}
