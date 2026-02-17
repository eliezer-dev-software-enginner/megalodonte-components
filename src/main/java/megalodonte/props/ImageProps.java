package megalodonte.props;

import javafx.scene.Node;
import javafx.scene.image.ImageView;
import megalodonte.theme.Theme;

public class ImageProps extends Props {
    private double width = 100;
    private double height = 100;

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

    @Override
    protected void applyTheme(Node node, Props props, Theme theme) {
        if (node instanceof ImageView image) {
            if (width >= 0) {
                image.setFitWidth(width);
            }
            if (height >= 0) {
                image.setFitHeight(height);
            }

            if(width >=0 && height>=0){
                if(width == height){
                    image.setPreserveRatio(true);
                }
            }
        }
    }
}
