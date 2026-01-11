package megalodonte.props;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;

public class SelectProps extends Props {
    private double minWidth;
    private double maxWidth;
    private double maxHeight;
    private int paddingUnitsDown;
    private int paddingUnitsTop;
    private int paddingUnitsLeft;
    private int paddingUnitsRight;

    private int height;

    public SelectProps height(int height){
        this.height = height;
        return this;
    }


    public SelectProps maxWidth(double maxWidth){
        this.maxWidth = maxWidth;
        return this;
    }

    public SelectProps minWidth(double minWidth){
        this.minWidth = minWidth;
        return this;
    }


    public SelectProps paddingAll(int units){
       this.paddingUnitsTop = units;
       this.paddingUnitsRight = units;
       this.paddingUnitsDown = units;
       this.paddingUnitsLeft = units;

        return this;
    }

    public SelectProps paddingTop(int units){
        this.paddingUnitsTop = units;
        return this;
    }

    public SelectProps paddingRight(int units){
        this.paddingUnitsRight = units;
        return this;
    }

    public SelectProps paddingDown(int units){
        this.paddingUnitsDown = units;
        return this;
    }

    public SelectProps paddingLeft(int units){
        this.paddingUnitsLeft = units;
        return this;
    }


    @Override
    public void apply(Node node) {
        if (node instanceof ComboBox cBox) {
            if (minWidth > 0) {
                cBox.setMinWidth(minWidth);
            }
            if(maxWidth > 0){
                cBox.setMaxWidth(maxWidth);
            }
            if (maxHeight > 0) {
                cBox.setMaxHeight(maxHeight);
            }

            if(height > 0){
                cBox.setPrefHeight(height);
                cBox.setMinHeight(height);
                cBox.setMaxHeight(height);
            }

            cBox.setPadding(new Insets(paddingUnitsTop, paddingUnitsRight, paddingUnitsDown, paddingUnitsLeft));
        }
    }
}
