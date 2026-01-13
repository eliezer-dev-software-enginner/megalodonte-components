package megalodonte.components;


import megalodonte.props.ImageProps;
import megalodonte.ReadableState;

public class Image extends Component {
    private final javafx.scene.image.ImageView imgview;

    public Image(String imgSource){
        super(new javafx.scene.image.ImageView(imgSource));
        this.imgview = (javafx.scene.image.ImageView) this.node;
    }

    public Image(String imgSource, ImageProps props){
        super(new javafx.scene.image.ImageView(imgSource), props);
        this.imgview = (javafx.scene.image.ImageView) this.node;
    }

    public Image(ReadableState<String> imgSourceState) {
        super(new javafx.scene.image.ImageView());
        this.imgview = (javafx.scene.image.ImageView) this.node;
        
        String initialValue = imgSourceState.get();
        if (initialValue != null && !initialValue.isEmpty()) {
            imgview.setImage(new javafx.scene.image.Image(initialValue));
        }

        imgSourceState.subscribe(s -> {
            if (s != null && !s.isEmpty()) {
                imgview.setImage(new javafx.scene.image.Image(s));
            } else {
                imgview.setImage(null);
            }
        });
    }

    public Image(ReadableState<String> imgSourceState, ImageProps props) {
        super(new javafx.scene.image.ImageView(), props);
        this.imgview = (javafx.scene.image.ImageView) this.node;
        
        String initialValue = imgSourceState.get();
        if (initialValue != null && !initialValue.isEmpty()) {
            imgview.setImage(new javafx.scene.image.Image(initialValue));
        }

        imgSourceState.subscribe(s -> {
            if (s != null && !s.isEmpty()) {
                imgview.setImage(new javafx.scene.image.Image(s));
            } else {
                imgview.setImage(null);
            }
        });
    }
}
