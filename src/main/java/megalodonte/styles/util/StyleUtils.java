package megalodonte.styles.util;

import javafx.scene.Node;
import megalodonte.theme.Theme;
import megalodonte.utils.Utils;

public class StyleUtils {
    /**
     * Generic method to get final color with fallback.
     */
    public static String getFinalColor(String colorField, String fallbackColor) {
        return (colorField != null && !colorField.isBlank()) ? colorField : fallbackColor;
    }


    /**
     * Gets the final border color with theme fallback.
     */
    public static String getFinalBorderColor(Theme theme, String borderColor) {
        return getFinalColor(borderColor, theme.colors().border());
    }

    /**
     * Gets the final background color with theme fallback.
     */
    public static String getFinalBackgroundColor(Theme theme, String bgColor) {
        return getFinalColor(bgColor, theme.colors().background());
    }

    /**
     * Gets the final border width with theme fallback.
     */
    public static int getFinalBorderWidth(Theme theme, int borderWidth) {
        return borderWidth > 0 ? borderWidth : theme.border().width();
    }

    /**
     * Gets the final border radius with theme fallback.
     */
    public static int getFinalBorderRadius(Theme theme, int borderRadius) {
        return borderRadius > 0 ? borderRadius : theme.radius().md();
    }

    /**
     * Applies common background styling.
     */
    public static void applyBackgroundStyling(Node node, Theme theme, String bgColor) {
        String finalBgColor = getFinalBackgroundColor(theme, bgColor);
        Utils.updateBackgroundColor(node, finalBgColor);
    }

    /**
     * Applies common border styling.
     */
    public static void applyBorderStyling(Node node, Theme theme, String borderColor, int borderWidth) {
        String finalBorderColor = getFinalBorderColor(theme, borderColor);
        Utils.updateBorderColor(node, finalBorderColor);

        int finalBorderWidth = getFinalBorderWidth(theme, borderWidth);
        if (finalBorderWidth > 0) {
            Utils.updateBorderWidth(node, finalBorderWidth);
        }

//        int finalBorderRadius = getFinalBorderRadius(theme);
//        if (finalBorderRadius > 0) {
//            Utils.updateBorderRadius(node, finalBorderRadius);
//        }

        Utils.updateBorderRadius(node, 0);
    }
}
