package megalodonte.props;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.TilePane;
import megalodonte.base.scale.ScaleProvider;
import megalodonte.base.theme.ThemeInterface;

public class GridFlowProps extends Props {
    private double minWidth = -1;
    private double maxHeight;
    private int spacingVertical;
    private int spacingHorizontal;

    private int paddingUnitsDown;
    private int paddingUnitsTop;
    private int paddingUnitsLeft;
    private int paddingUnitsRight;

    @Override
    protected void applyTheme(Node node, Props props, ThemeInterface theme) {
        if (node instanceof TilePane tilePane) {
            //tamanho de cada tile (quadrado)
            if (tileWidth > 0) {
                tilePane.setPrefTileWidth(ScaleProvider.scale(tileWidth));
            }
            if (tileHeight > 0) {
                tilePane.setPrefTileHeight(ScaleProvider.scale(tileHeight));
            }

            //tamanho do container inteiro
            if (minWidth > 0) {
                tilePane.setMinWidth(ScaleProvider.scale(minWidth));
            }
            if (maxHeight > 0) {
                tilePane.setMaxHeight(ScaleProvider.scale(maxHeight));
            }
            
            tilePane.setTileAlignment(Pos.TOP_LEFT);

            if(spacingVertical > 0){
                tilePane.setVgap(ScaleProvider.scale(spacingVertical));
            }
            if(spacingHorizontal > 0){
                tilePane.setHgap(ScaleProvider.scale(spacingHorizontal));
            }

            if(alignment == Alignment.CENTER_HORIZONTALLY){
                tilePane.setAlignment(Pos.TOP_CENTER);
            }

            tilePane.setPadding(new Insets(
                    ScaleProvider.scale(paddingUnitsTop),
                    ScaleProvider.scale(paddingUnitsRight),
                    ScaleProvider.scale(paddingUnitsDown),
                    ScaleProvider.scale(paddingUnitsLeft)
            ));
        }
    }

    private enum Alignment {CENTER_HORIZONTALLY}
    private Alignment alignment;

    private double tileWidth = -1;
    private double tileHeight = -1;

    public GridFlowProps tileSize(double width, double height) {
        this.tileWidth = width;
        this.tileHeight = height;
        return this;
    }


    public GridFlowProps minWidth(double minWidth){
        this.minWidth = minWidth;
        return this;
    }

    public GridFlowProps spacingOf(int units){
        spacingVertical(units);
        spacingHorizontal(units);
        return this;
    }

    public GridFlowProps spacingVertical(int units){
        this.spacingVertical = units;
        return this;
    }

    public GridFlowProps spacingHorizontal(int units){
        this.spacingHorizontal = units;
        return this;
    }

    public GridFlowProps centerHorizontally(){
        alignment = Alignment.CENTER_HORIZONTALLY;
        return this;
    }
}
