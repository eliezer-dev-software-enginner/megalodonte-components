package megalodonte.components;

import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import megalodonte.base.components.Component;
import java.nio.file.Files;
import java.nio.file.Path;

/** Local image preview with a centered 16:9 viewport. */
public final class ImagePreview extends Component implements AutoCloseable {
    private final ImageView image = new ImageView();
    private final Label placeholder = new Label("Selecione uma cena e importe uma imagem");
    private final StackPane frame = new StackPane(image, placeholder);

    public ImagePreview() {
        super(new Pane() {
            @Override protected void layoutChildren() {
                super.layoutChildren();
                StackPane frame = (StackPane) getChildren().getFirst();
                double width = Math.min(getWidth(), getHeight() * 16.0 / 9);
                double height = width * 9 / 16;
                frame.resizeRelocate((getWidth()-width)/2, (getHeight()-height)/2, width, height);
            }
        });
        Pane pane = (Pane) node;
        frame.setId("videoFrame"); frame.setManaged(false);
        frame.setStyle("-fx-background-color: #000000;");
        pane.setStyle("-fx-background-color: #151b28; -fx-background-radius: 8;");
        pane.getChildren().add(frame);
        placeholder.setStyle("-fx-text-fill: #c9d4e6;");
        image.setPreserveRatio(true);
        image.fitWidthProperty().bind(frame.widthProperty());
        image.fitHeightProperty().bind(frame.heightProperty());
    }

    public void setSource(String source) {
        var loaded = LocalImages.load(source, 1600, 900);
        image.setImage(loaded);
        placeholder.setVisible(loaded == null);
    }

    public double viewportAspectRatio() { return frame.getWidth() / frame.getHeight(); }

    public boolean hasImage() { return image.getImage() != null; }

    @Override public void close() {
        image.fitWidthProperty().unbind();
        image.fitHeightProperty().unbind();
        image.setImage(null);
    }
}
