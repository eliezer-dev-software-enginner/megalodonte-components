package megalodonte.components;

import javafx.stage.FileChooser;
import javafx.stage.Window;

import java.net.URI;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/** Native file selection exposed through the Megalodonte component library. */
public final class FilePicker {
    private FilePicker() {}

    public static Optional<URI> chooseImage(Window owner) {
        return chooseFile(owner, "Selecionar imagem", "Imagens", List.of("*.png", "*.jpg", "*.jpeg"));
    }

    public static Optional<URI> chooseFile(Window owner, String title, String description, List<String> extensions) {
        Objects.requireNonNull(owner, "owner");
        Objects.requireNonNull(title, "title");
        Objects.requireNonNull(description, "description");
        if (extensions == null || extensions.isEmpty()) throw new IllegalArgumentException("extensions must not be empty");
        var chooser = new FileChooser();
        chooser.setTitle(title);
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(
                description, extensions));
        var selected = chooser.showOpenDialog(owner);
        return selected == null ? Optional.empty() : Optional.of(selected.toURI());
    }
}
