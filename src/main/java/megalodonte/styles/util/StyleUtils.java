package megalodonte.styles.util;

import javafx.scene.Node;
import javafx.scene.paint.Color;
import megalodonte.base.theme.ThemeInterface;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Single, centralized place for styling JavaFX nodes across megalodonte-components:
 * reading/writing inline styles ({@code Node.setStyle(String)}) and resolving a
 * prop value against the current theme's fallback.
 * <p>
 * JavaFX has no API to set a single CSS property on a node's inline style without
 * touching the rest of the style string, so every property update has to read the
 * current style, merge the new value in, and write the whole string back. This
 * class owns that merge logic ({@link #setStyleProperty}) and exposes named,
 * semantic helpers (background color, border, text color, font size, ...) on top
 * of it, plus the {@code getFinalXxx}/{@code applyXxxStyling} helpers that resolve
 * an explicit prop value against a theme fallback. Nothing else in the library
 * should call {@code node.getStyle()}/{@code node.setStyle()} directly — go
 * through the helpers here instead, even for one-off properties that don't have a
 * named helper yet (use {@link #applyStyleProperty} directly in that case).
 * <p>
 * This used to be two classes ({@code Utils} and {@code StyleUtils}); merged into
 * one so there's a single place to look for any style-related helper.
 */
public class StyleUtils {

    // ---------------------------------------------------------------------
    // Inline-style CSS property names
    // ---------------------------------------------------------------------

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

    // ---------------------------------------------------------------------
    // Named inline-style helpers — one per CSS property
    // ---------------------------------------------------------------------

    /** Sets the node's background image, stretched to cover and centered. */
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

    /** Sets both {@code -fx-border-radius} and {@code -fx-background-radius} to the same value. */
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

    // ---------------------------------------------------------------------
    // Inline-style primitives — the merge logic everything above builds on
    // ---------------------------------------------------------------------

    /**
     * Reads {@code node.getStyle()}, merges {@code property: value;} into it via
     * {@link #setStyleProperty}, and writes the result back. This is the one place
     * in the library allowed to do a getStyle()/setStyle() round trip — everything
     * else should call this (or one of the named {@code updateXxx} helpers above)
     * instead of touching {@code getStyle()}/{@code setStyle()} directly. Also the
     * right call to make directly for a one-off property that has no named helper.
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
     * Reads back the value of a single property from an inline style string, or an
     * empty string if the property isn't set.
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

    // ---------------------------------------------------------------------
    // Theme-fallback resolution — "use the explicit value, or fall back to theme"
    // ---------------------------------------------------------------------

    /** Generic method to get final color with fallback. */
    public static String getFinalColor(String colorField, String fallbackColor) {
        return (colorField != null && !colorField.isBlank()) ? colorField : fallbackColor;
    }

    /** Gets the final border color with theme fallback. */
    public static String getFinalBorderColor(ThemeInterface theme, String borderColor) {
        return getFinalColor(borderColor, theme.colors().border());
    }

    /** Gets the final border width with theme fallback. */
    public static int getFinalBorderWidth(ThemeInterface theme, int borderWidth) {
        return borderWidth > 0 ? borderWidth : theme.border().width();
    }

    /** Gets the final border radius with theme fallback. */
    public static int getFinalBorderRadius(ThemeInterface theme, int borderRadius) {
        return borderRadius > 0 ? borderRadius : theme.border().radiusMd();
    }

    /** Gets the final background color with theme fallback. */
    public static String getFinalBackgroundColor(ThemeInterface theme, String bgColor) {
        return getFinalColor(bgColor, theme.colors().background());
    }

    // ---------------------------------------------------------------------
    // Compound styling — resolve + apply in one call
    // ---------------------------------------------------------------------

    /**
     * Applies background color or background image, theme-resolved.
     *
     * @param type 0 for color, 1 for image url
     */
    public static void applyBackgroundStyling(Node node, ThemeInterface theme, String bg, byte type) {
        if (type == 0) {
            applyBackgroundStyling(node, theme, bg);
        } else {
            updateBgImage(node, bg);
        }
    }

    /** Applies common background styling (color only, theme-resolved). */
    public static void applyBackgroundStyling(Node node, ThemeInterface theme, String bgColor) {
        String finalBgColor = getFinalBackgroundColor(theme, bgColor);
        updateBackgroundColor(node, finalBgColor);
    }

    /** Applies common border styling (color, width, radius — all theme-resolved). */
    public static void applyBorderStyling(Node node, ThemeInterface theme, String borderColor, int borderWidth, int borderRadius) {
        String finalBorderColor = getFinalBorderColor(theme, borderColor);
        updateBorderColor(node, finalBorderColor);

        int finalBorderWidth = getFinalBorderWidth(theme, borderWidth);
        if (finalBorderWidth > 0) {
            updateBorderWidth(node, finalBorderWidth);
        }

        int finalBorderRadius = getFinalBorderRadius(theme, borderRadius);
        updateBorderRadius(node, finalBorderRadius);
    }
}
