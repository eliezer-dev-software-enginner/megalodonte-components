package megalodonte.components;

import javafx.scene.image.Image;
import java.nio.file.Files;
import java.nio.file.Path;

final class LocalImages {
    private LocalImages() {}
    static Image load(String source, double width, double height) {
        if (source == null || source.isBlank()) return null;
        try {
            Path path = Path.of(source);
            if (!Files.isRegularFile(path)) return null;
            var image = new Image(path.toUri().toString(), width, height, true, true, false);
            return image.isError() ? null : image;
        } catch (RuntimeException exception) { return null; }
    }
}
