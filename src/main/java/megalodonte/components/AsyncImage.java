package megalodonte.components;

import javafx.scene.image.ImageView;
import megalodonte.base.components.Component;
import megalodonte.props.ImageProps;

/** Image loaded in the background so remote assets never block the UI thread. */
public final class AsyncImage extends Component {
    public AsyncImage(String source) {
        this(source, new ImageProps());
    }

    public AsyncImage(String source, ImageProps props) {
        super(new ImageView(), props);
        if (source == null || source.isBlank())
            throw new IllegalArgumentException("Remote image source is required");
        ((ImageView) node).setImage(new javafx.scene.image.Image(source, true));
    }
}
