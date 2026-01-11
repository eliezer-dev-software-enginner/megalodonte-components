package megalodonte.props;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.VBox;

public class ColumnProps extends Props {
    private double minWidth;
    private double maxWidth;
    private double width = -1;
    private double maxHeight;
    private int spacingUnits = 0;
    private int paddingUnitsDown;
    private int paddingUnitsTop;
    private int paddingUnitsLeft;
    private int paddingUnitsRight;

    private int height;

    public ColumnProps height(int height){
        this.height = height;
        return this;
    }

    public ColumnProps width(double width){
        this.width = width;
        return this;
    }

    private enum Alignment {CENTER_HORIZONTALLY, CENTER_VERTICALLY}
    private Alignment alignment;

    public  ColumnProps maxWidth(double maxWidth){
        this.maxWidth = maxWidth;
        return this;
    }

    public ColumnProps minWidth(double minWidth){
        this.minWidth = minWidth;
        return this;
    }

    public ColumnProps spacingOf(int units){
        this.spacingUnits = units;
        return this;
    }

    public ColumnProps paddingAll(int units){
       this.paddingUnitsTop = units;
       this.paddingUnitsRight = units;
       this.paddingUnitsDown = units;
       this.paddingUnitsLeft = units;

        return this;
    }

    public ColumnProps paddingTop(int units){
        this.paddingUnitsTop = units;
        return this;
    }

    public ColumnProps paddingRight(int units){
        this.paddingUnitsRight = units;
        return this;
    }

    public ColumnProps paddingDown(int units){
        this.paddingUnitsDown = units;
        return this;
    }

    public ColumnProps paddingLeft(int units){
        this.paddingUnitsLeft = units;
        return this;
    }

    public ColumnProps centerHorizontally(){
        alignment = Alignment.CENTER_HORIZONTALLY;
        return this;
    }

    public ColumnProps centerVertically(){
        alignment = Alignment.CENTER_VERTICALLY;
        return this;
    }

    private Runnable runnable_onClick;
    public ColumnProps onClick(Runnable callback){
        this.runnable_onClick = callback;
        return this;
    }



    @Override
    public void apply(Node node) {
        if (node instanceof VBox vBox) {
            if (minWidth > 0) {
                vBox.setMinWidth(minWidth);
            }
            if (width >= 0) {
                vBox.setPrefWidth(width);
            }
            if(maxWidth > 0){
                vBox.setMaxWidth(maxWidth);
            }
            if (maxHeight > 0) {
                vBox.setMaxHeight(maxHeight);
            }
            if(spacingUnits > 0){
                vBox.setSpacing(spacingUnits);
            }

            if(alignment != null){
                if(alignment.equals(Alignment.CENTER_HORIZONTALLY)){
                    vBox.setAlignment(Pos.TOP_CENTER);
                }
                if(alignment.equals(Alignment.CENTER_VERTICALLY)){
                    vBox.setAlignment(Pos.CENTER);
                }
            }

            if (runnable_onClick != null) {
                vBox.setOnMouseClicked(ev-> {
                    //ev.consume();
                    runnable_onClick.run();
                });
            }

            if(height > 0){
                vBox.setPrefHeight(height);
                vBox.setMinHeight(height);
                vBox.setMaxHeight(height);
            }

            vBox.setPadding(new Insets(paddingUnitsTop, paddingUnitsRight, paddingUnitsDown, paddingUnitsLeft));
        }
    }
}
