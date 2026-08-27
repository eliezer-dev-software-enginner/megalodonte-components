package megalodonte.props;

import javafx.scene.Node;
import javafx.scene.layout.Region;
import megalodonte.base.scale.ScaleProvider;
import megalodonte.base.theme.ThemeInterface;

public abstract class LayoutProps<T extends LayoutProps<T>> extends Props implements Paddable<T> {
    protected double minWidth = -1;
    protected double maxWidth = -1;
    protected double width = -1;
    protected double maxHeight = -1;
    protected double height = -1;
    protected int spacingUnits = 0;
    protected int paddingUnitsTop = UNSET;
    protected int paddingUnitsRight = UNSET;
    protected int paddingUnitsDown = UNSET;
    protected int paddingUnitsLeft = UNSET;
    protected Runnable onClick;

    @SuppressWarnings("unchecked")
    public T minWidth(double minWidth) {
        this.minWidth = minWidth;
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    public T maxWidth(double maxWidth) {
        this.maxWidth = maxWidth;
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    public T width(double width) {
        this.width = width;
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    public T maxHeight(double maxHeight) {
        this.maxHeight = maxHeight;
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    public T height(double height) {
        this.height = height;
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    public T spacingOf(int units) {
        this.spacingUnits = units;
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    public T paddingAll(int units) {
        this.paddingUnitsTop = units;
        this.paddingUnitsRight = units;
        this.paddingUnitsDown = units;
        this.paddingUnitsLeft = units;
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    public T paddingTop(int units) {
        this.paddingUnitsTop = units;
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    public T paddingRight(int units) {
        this.paddingUnitsRight = units;
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    public T paddingDown(int units) {
        this.paddingUnitsDown = units;
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    public T paddingLeft(int units) {
        this.paddingUnitsLeft = units;
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    public T onClick(Runnable callback) {
        this.onClick = callback;
        return (T) this;
    }

    protected void applyBaseLayout(Node node, ThemeInterface theme) {
        if (!(node instanceof Region region)) return;

        if (minWidth > 0) {
            region.setMinWidth(ScaleProvider.scale(minWidth));
        }
        if (width > 0) {
            region.setPrefWidth(ScaleProvider.scale(width));
        }
        if (maxWidth > 0) {
            region.setMaxWidth(ScaleProvider.scale(maxWidth));
        }
        if (maxHeight > 0) {
            region.setMaxHeight(ScaleProvider.scale(maxHeight));
        }
        if (height > 0) {
            double scaled = ScaleProvider.scale(height);
            region.setPrefHeight(scaled);
            region.setMinHeight(scaled);
            region.setMaxHeight(scaled);
        }

        region.setPadding(resolvePadding(theme, paddingUnitsTop, paddingUnitsRight, paddingUnitsDown, paddingUnitsLeft));

        if (onClick != null) {
            region.setOnMouseClicked(ev -> onClick.run());
        }
    }
}
