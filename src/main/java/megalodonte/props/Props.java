package megalodonte.props;

import javafx.geometry.Insets;
import javafx.scene.Node;

public abstract class Props {
    protected double marginTop = -1;
    protected double marginBottom = -1;
    protected double marginLeft = -1;
    protected double marginRight = -1;
    protected Insets margins;

    public enum Margin {
        TOP, BOTTOM, LEFT, RIGHT
    }

    public Props margin(Margin margin, double value) {
        switch (margin) {
            case TOP -> marginTop = value;
            case BOTTOM -> marginBottom = value;
            case LEFT -> marginLeft = value;
            case RIGHT -> marginRight = value;
        }
        updateMargins();
        return this;
    }

    public Props marginAll(double value) {
        marginTop = marginBottom = marginLeft = marginRight = value;
        updateMargins();
        return this;
    }

    public Props marginTop(double value) {
        this.marginTop = value;
        updateMargins();
        return this;
    }

    public Props marginBottom(double value) {
        this.marginBottom = value;
        updateMargins();
        return this;
    }

    public Props marginLeft(double value) {
        this.marginLeft = value;
        updateMargins();
        return this;
    }

    public Props marginRight(double value) {
        this.marginRight = value;
        updateMargins();
        return this;
    }

    private void updateMargins() {
        if (marginTop >= 0 || marginBottom >= 0 || marginLeft >= 0 || marginRight >= 0) {
            margins = new Insets(
                marginTop >= 0 ? marginTop : 0,
                marginRight >= 0 ? marginRight : 0,
                marginBottom >= 0 ? marginBottom : 0,
                marginLeft >= 0 ? marginLeft : 0
            );
        }
    }

    public Insets getMargins() {
        return margins;
    }

    public abstract void apply(Node node);
}
