package megalodonte.components;

import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import megalodonte.base.components.IconInterface;

import java.util.Objects;

public class Icon extends IconInterface {
    private final ImageView imageView;

    public Icon(String path, double size) {
        var image = new Image(Objects.requireNonNull(
                getClass().getResourceAsStream(path),
                "Ícone não encontrado: " + path
        ));
        this.imageView = new ImageView(image);
        this.imageView.setFitWidth(size);
        this.imageView.setFitHeight(size);
        this.imageView.setPreserveRatio(true);
    }

    @Override
    public Node getNode() {
        return imageView;
    }
}