package megalodonte.components;

import javafx.scene.image.ImageView;
import megalodonte.base.components.Component;
import megalodonte.props.ImageProps;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/** Image loaded in the background so remote assets never block the UI thread. */
public final class AsyncImage extends Component {
    private static final ConcurrentMap<String, javafx.scene.image.Image> CACHE = new ConcurrentHashMap<>();

    public AsyncImage(String source) {
        this(source, new ImageProps());
    }

    public AsyncImage(String source, ImageProps props) {
        super(new ImageView(), props);
        if (source == null || source.isBlank())
            throw new IllegalArgumentException("Async image source is required");
        String normalized = source.strip();
        ((ImageView) node).setImage(CACHE.computeIfAbsent(normalized,
                value -> new javafx.scene.image.Image(value, true)));
    }
}
