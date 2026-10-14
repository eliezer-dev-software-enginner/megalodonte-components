package megalodonte.components;

import javafx.scene.input.ClipboardContent;

/** Native text clipboard access for Megalodonte applications. */
public final class Clipboard {
    private Clipboard() {}

    public static void copyText(String text) {
        var content = new ClipboardContent();
        content.putString(text == null ? "" : text);
        javafx.scene.input.Clipboard.getSystemClipboard().setContent(content);
    }
}
