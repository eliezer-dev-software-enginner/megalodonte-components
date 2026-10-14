package megalodonte.components;

import javafx.stage.FileChooser;
import javafx.stage.Window;

import java.net.URI;
import java.util.Objects;
import java.util.Optional;

/** Native file selection exposed through the Megalodonte component library. */
public final class FilePicker {
    private FilePicker() {}

    public static Optional<URI> chooseImage(Window owner) {
        Objects.requireNonNull(owner, "owner");
        var chooser = new FileChooser();
        chooser.setTitle("Selecionar imagem");
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(
                "Imagens", "*.png", "*.jpg", "*.jpeg"));
        var selected = chooser.showOpenDialog(owner);
        return selected == null ? Optional.empty() : Optional.of(selected.toURI());
    }
}
