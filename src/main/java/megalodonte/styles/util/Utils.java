package megalodonte.styles.util;

import javafx.scene.Node;
import javafx.scene.paint.Color;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Single, centralized place for reading and writing JavaFX inline styles
 * ({@code Node.setStyle(String)}) across megalodonte-components.
 * <p>
 * JavaFX has no API to set a single CSS property on a node's inline style
 * without touching the rest of the style string, so every property update
 * has to read the current style, merge the new value in and write the whole
 * string back. This class owns that merge logic ({@link #setStyleProperty})
 * and exposes named, semantic helpers (background color, border, text
 * color, font size, ...) on top of it, so the rest of the library never has
 * to hand-roll {@code node.getStyle() + "..."} concatenation itself.
 * <p>
 * There used to be a near-identical copy of this class in megalodonte-base
 * ({@code megalodonte.utils.Utils}, deprecated and removed). This is now the
 * only copy — anything in megalodonte-components (or any module that
 * depends on it) that needs to touch a node's inline style should go
 * through here instead of calling {@code getStyle()}/{@code setStyle()}
 * directly.
 */
public class Utils {
    public static final String FX_FONT_SIZE = "-fx-font-size";

    /** Used by Text. */
    public static final String FX_FILL = "-fx-fill";

    /** Used by Button and other text-bearing controls. */
    public static final String FX_TEXT_FILL = "-fx-text-fill";

    public static final String FX_BG_COLOR = "-fx-background-color";
    public static final String FX_BG_RADIUS = "-fx-background-radius";
    public static final String FX_BORDER_WIDTH = "-fx-border-width";
    public static final String FX_BORDER_COLOR = "-fx-border-color";
    public static final String FX_BORDER_RADIUS = "-fx-border-radius";
    public static final String FX_FONT_WEIGHT = "-fx-font-weight";
    public static final String FX_PLACEHOLDER_COLOR = "-fx-prompt-text-fill";
    public static final String FX_PLACEHOLDER_FONT_SIZE = "-fx-prompt-font-size";
    public static final String FX_CURSOR = "-fx-cursor";

    /**
     * Sets the node's background image, stretched to cover and centered.
     */
    public static void updateBgImage(Node node, String url) {
        var updated = setStyleProperty(node.getStyle(), "-fx-background-image", String.format("url('%s')", url));
        updated = setStyleProperty(updated, "-fx-background-size", "cover");
        updated = setStyleProperty(updated, "-fx-background-position", "center");
        node.setStyle(updated);
    }

    /** Text fill color — used by Text-based components ({@code -fx-fill}). */
    public static void updateTextColor(Node node, String color) {
        applyStyleProperty(node, color, FX_FILL);
    }

    /** Placeholder/prompt text color — used by input controls. */
    public static void updatePlaceholderColor(Node node, String color) {
        applyStyleProperty(node, color, FX_PLACEHOLDER_COLOR);
    }

    /** Placeholder/prompt font size, in pixels — keeps the prompt in sync with the input's own font size. */
    public static void updatePlaceholderFontSize(Node node, int fontSizePx) {
        applyStyleProperty(node, fontSizePx + "px", FX_PLACEHOLDER_FONT_SIZE);
    }

    /** Text fill color for text-bearing controls ({@code -fx-text-fill}), e.g. Button, inputs. */
    public static void updateTextColor_Input(Node node, String color) {
        applyStyleProperty(node, color, FX_TEXT_FILL);
    }

    public static void updateFontSize(Node node, int value) {
        applyStyleProperty(node, String.valueOf(value), FX_FONT_SIZE);
    }

    public static void updateFontWeight(Node node, String weight) {
        applyStyleProperty(node, weight, FX_FONT_WEIGHT);
    }

    public static void updateBorderColor(Node node, String color) {
        applyStyleProperty(node, color, FX_BORDER_COLOR);
    }

    public static void updateBorderWidth(Node node, int width) {
        applyStyleProperty(node, width + "px", FX_BORDER_WIDTH);
    }

    public static void updateBorderRadius(Node node, int radius) {
        applyStyleProperty(node, radius + "px", FX_BORDER_RADIUS);
        applyStyleProperty(node, radius + "px", FX_BG_RADIUS);
    }

    /** Background color — can be used on any Region (Pane, VBox, HBox, controls, ...). */
    public static void updateBackgroundColor(Node node, String color) {
        applyStyleProperty(node, color, FX_BG_COLOR);
    }

    public static void updateCursor(Node node, String value) {
        applyStyleProperty(node, value, FX_CURSOR);
    }

    /**
     * Reads {@code node.getStyle()}, merges {@code property: value;} into it via
     * {@link #setStyleProperty}, and writes the result back. This is the one
     * place in the library allowed to do a getStyle()/setStyle() round trip —
     * everything else should call this (or one of the semantic {@code updateXxx}
     * helpers above) instead of touching {@code getStyle()}/{@code setStyle()} directly.
     */
    public static void applyStyleProperty(Node node, String value, String property) {
        node.setStyle(setStyleProperty(node.getStyle(), property, value));
    }

    /**
     * Merges a single {@code property: value;} declaration into an existing inline
     * style string, replacing the property's previous value if it was already
     * present, or appending it otherwise. Pure string transform — does not touch
     * any Node, so it's also usable when the caller already has both the current
     * style string and the target node's style in hand (e.g. querying it once for
     * several updates).
     */
    public static String setStyleProperty(String currentStyle, String property, String value) {
        String newDeclaration = property + ": " + value + ";";

        if (currentStyle.contains(property)) {
            currentStyle = currentStyle.replaceAll(
                    "(?i)" + property + ":\\s*[^;]+;",
                    newDeclaration);
        } else {
            if (!currentStyle.isEmpty() && !currentStyle.endsWith(" ")) {
                currentStyle += " ";
            }
            currentStyle += newDeclaration;
        }

        return currentStyle;
    }

    /**
     * Reads back the value of a single property from an inline style string, or
     * an empty string if the property isn't set.
     */
    public static String getStylePropertyValue(String currentStyle, String property) {
        if (currentStyle.contains(property)) {
            Pattern pattern = Pattern.compile(property + ":\\s*([^;]+);");
            Matcher matcher = pattern.matcher(currentStyle);

            if (matcher.find()) {
                return matcher.group(1);
            }
        }

        return "";
    }

    public static String colorToHex(Color color) {
        return String.format("#%02x%02x%02x",
                (int) (color.getRed() * 255),
                (int) (color.getGreen() * 255),
                (int) (color.getBlue() * 255));
    }
}
