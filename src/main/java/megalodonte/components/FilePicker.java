package megalodonte.components;

import javafx.stage.FileChooser;
import javafx.stage.DirectoryChooser;
import javafx.stage.Window;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/** Native file selection exposed through the Megalodonte component library. */
public final class FilePicker {
    private FilePicker() {}

    /** Opens an image picker without a window owner. */
    public static Optional<URI> chooseImage() { return chooseImage(null); }

    public static Optional<URI> chooseImage(Window owner) {
        return chooseFile(owner, "Selecionar imagem", "Imagens", List.of("*.png", "*.jpg", "*.jpeg"));
    }

    public static Optional<URI> chooseFile(Window owner, String title, String description, List<String> extensions) {
        Objects.requireNonNull(title, "title");
        Objects.requireNonNull(description, "description");
        if (extensions == null || extensions.isEmpty()) throw new IllegalArgumentException("extensions must not be empty");
        var chooser = new FileChooser();
        chooser.setTitle(title);
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(
                description, extensions));
        var selected = chooser.showOpenDialog(resolveOwner(owner));
        return selected == null ? Optional.empty() : Optional.of(selected.toURI());
    }

    public static Optional<Path> chooseDirectory(Window owner, String title) {
        var chooser = new DirectoryChooser();
        chooser.setTitle(Objects.requireNonNull(title, "title"));
        var selected = chooser.showDialog(resolveOwner(owner));
        return selected == null ? Optional.empty() : Optional.of(selected.toPath());
    }

    /** Opens a save picker without a window owner. */
    public static Optional<Path> saveFile(String title, String initialName,
                                          String description, List<String> extensions) {
        return saveFile(null, title, initialName, description, extensions);
    }

    public static Optional<Path> saveFile(Window owner, String title, String initialName,
                                          String description, List<String> extensions) {
        var chooser = new FileChooser();
        chooser.setTitle(Objects.requireNonNull(title, "title"));
        chooser.setInitialFileName(Objects.requireNonNull(initialName, "initialName"));
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(description, extensions));
        var selected = chooser.showSaveDialog(resolveOwner(owner));
        return selected == null ? Optional.empty() : Optional.of(selected.toPath());
    }

    private static Window resolveOwner(Window owner) {
        if (owner != null) return owner;
        return Window.getWindows().stream().filter(Window::isFocused).findFirst().orElse(null);
    }

    public static void openDirectory(Path directory) throws IOException {
        Path path = Objects.requireNonNull(directory, "directory").toAbsolutePath().normalize();
        if (!Files.isDirectory(path)) throw new IOException("Directory does not exist: " + path);
        String system = System.getProperty("os.name", "").toLowerCase();
        String command = system.contains("win") ? "explorer" : system.contains("mac") ? "open" : "xdg-open";
        new ProcessBuilder(command, path.toString()).start();
    }
}
