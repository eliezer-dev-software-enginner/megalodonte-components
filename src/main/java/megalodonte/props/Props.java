package megalodonte.props;

import javafx.geometry.Insets;
import javafx.scene.Node;
import megalodonte.theme.Theme;
import megalodonte.theme.ThemeManager;

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

    //public abstract void apply(Node node);

    /**
     * Template method that handles theme subscription and calls applyTheme.
     * Subclasses should implement applyTheme for their specific styling logic.
     */
    final public void apply(Node node){
        ThemeManager.state().subscribe(theme -> {
            if (theme == null) return;
            applyTheme(node, this, theme);
        });
    }

    protected abstract void applyTheme(Node node, Props props, Theme theme);
}
