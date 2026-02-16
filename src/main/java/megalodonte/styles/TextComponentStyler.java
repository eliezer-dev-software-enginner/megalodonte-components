package megalodonte.styles;

public abstract class TextComponentStyler<T extends megalodonte.props.TextComponentProps, S extends TextComponentStyler<T, S>> extends BaseStyler<T, S> {

    public S color(String color) {
        this.textColor = color;
        return (S) this;
    }
    public S textColor(String color) {
        this.textColor = color;
        return (S) this;
    }
}
