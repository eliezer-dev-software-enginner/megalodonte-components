package megalodonte.props;

import javafx.scene.Node;
import javafx.scene.image.ImageView;
import megalodonte.base.scale.ScaleProvider;
import megalodonte.base.theme.ThemeInterface;

public class ImageProps extends Props {
    private double width = 100;
    private double height = 100;
    private Boolean preserveRatio = true;

    public ImageProps size(double size){
        this.width(size);
        this.height(size);
        return this;
    }

    public ImageProps width(double width){
        this.width = width;
        return this;
    }
    public ImageProps height(double height){
        this.height = height;
        return this;
    }

    public ImageProps preserveRatio(boolean preserve){
        this.preserveRatio = preserve;
        return this;
    }

    @Override
    protected void applyTheme(Node node, Props props, ThemeInterface theme) {
        if (node instanceof ImageView image) {
            if (width >= 0) {
                image.setFitWidth(ScaleProvider.scale(width));
            }
            if (height >= 0) {
                image.setFitHeight(ScaleProvider.scale(height));
            }
            image.setPreserveRatio(preserveRatio);
        }
    }
}
