package megalodonte.props;

import megalodonte.ReadableState;

public abstract class TextComponentProps extends Props {
    protected Integer fontSize;
    protected ReadableState<Integer> fontSizeState;
    protected String fontWeight;

    public TextComponentProps fontSize(int fontSize) {
        this.fontSize = fontSize;
        return this;
    }

    public TextComponentProps fontSize(ReadableState<Integer> state) {
        this.fontSizeState = state;
        return this;
    }

    public TextComponentProps bold() {
        this.fontWeight = "bold";
        return this;
    }

    public TextComponentProps fontWeight(String weight) {
        this.fontWeight = weight;
        return this;
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
}
