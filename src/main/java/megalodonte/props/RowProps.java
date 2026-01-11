package megalodonte.props;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.HBox;

public class RowProps extends Props {
    private double minWidth = -1;
    private double maxWidth = Double.MAX_VALUE;
    private double width = -1;
    private double maxHeight = Double.MAX_VALUE;
    private int spacingUnits = 0;
    private int paddingUnitsDown;
    private int paddingUnitsTop;
    private int paddingUnitsLeft;
    private int paddingUnitsRight;

    private enum Alignment {BOTTOM_VERTICALLY, CENTER_HORIZONTALLY}
    private Alignment alignment;

    public RowProps bottomVertically(){
        alignment = Alignment.BOTTOM_VERTICALLY;
        return this;
    }

    public RowProps centerHorizontally(){
        alignment = Alignment.CENTER_HORIZONTALLY;
        return this;
    }

    public RowProps minWidth(double minWidth){
        this.minWidth = minWidth;
        return this;
    }

    public RowProps width(double width){
        this.width = width;
        return this;
    }

    public RowProps maxWidth(double maxWidth){
        this.maxWidth = maxWidth;
        return this;
    }

    public RowProps spacingOf(int units){
        this.spacingUnits = units;
        return this;
    }

    public RowProps paddingAll(int units){
       this.paddingUnitsTop = units;
       this.paddingUnitsRight = units;
       this.paddingUnitsDown = units;
       this.paddingUnitsLeft = units;

        return this;
    }

    public RowProps paddingTop(int units){
        this.paddingUnitsTop = units;
        return this;
    }

    public RowProps paddingRight(int units){
        this.paddingUnitsRight = units;
        return this;
    }

    public RowProps paddingDown(int units){
        this.paddingUnitsDown = units;
        return this;
    }

    public RowProps paddingLeft(int units){
        this.paddingUnitsLeft = units;
        return this;
    }

    @Override
    public void apply(Node node) {
        if (node instanceof HBox hBox) {
            if (minWidth >= 0) {
                hBox.setMinWidth(minWidth);
            }
            if (width >= 0) {
                hBox.setPrefWidth(width);
            }
            if (maxWidth >= 0 && maxWidth != Double.MAX_VALUE) {
                hBox.setMaxWidth(maxWidth);
            }
            if (maxHeight >= 0 && maxHeight != Double.MAX_VALUE) {
                hBox.setMaxHeight(maxHeight);
            }
            if(spacingUnits > 0){
                hBox.setSpacing(spacingUnits);
            }

            if(alignment == Alignment.BOTTOM_VERTICALLY){
                hBox.setAlignment(Pos.BOTTOM_LEFT);
            }
            if(alignment == Alignment.CENTER_HORIZONTALLY){
                hBox.setAlignment(Pos.CENTER);
            }

            hBox.setPadding(new Insets(paddingUnitsTop, paddingUnitsRight, paddingUnitsDown, paddingUnitsLeft));
        }
    }
}
