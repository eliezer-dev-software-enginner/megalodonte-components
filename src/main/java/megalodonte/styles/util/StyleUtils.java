package megalodonte.styles.util;

import javafx.scene.Node;
import megalodonte.base.theme.ThemeInterface;

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
    public static String getFinalBorderColor(ThemeInterface theme, String borderColor) {
        return getFinalColor(borderColor, theme.colors().border());
    }


    /**
     * Gets the final border width with theme fallback.
     */
    public static int getFinalBorderWidth(ThemeInterface theme, int borderWidth) {
        return borderWidth > 0 ? borderWidth : theme.border().width();
    }

    /**
     * Gets the final border radius with theme fallback.
     */
    public static int getFinalBorderRadius(ThemeInterface theme, int borderRadius) {
        return borderRadius > 0 ? borderRadius : theme.border().radiusMd();
    }


    /**
     *
     * @param node
     * @param theme
     * @param bg
     * @param type 0 for color, 1 for image url
     */
    public static void applyBackgroundStyling(Node node, ThemeInterface theme, String bg, byte type) {
        if(type == 0){
            String finalBgColor = getFinalBackgroundColor(theme, bg);
            Utils.updateBackgroundColor(node, finalBgColor);
        }else{
            Utils.updateBgImage(node, bg);
        }
    }

    /**
     * Applies common background styling.
     */
    public static void applyBackgroundStyling(Node node, ThemeInterface theme, String bgColor) {
        String finalBgColor = getFinalBackgroundColor(theme, bgColor);
        Utils.updateBackgroundColor(node, finalBgColor);
    }

    /**
     * Gets the final background color with theme fallback.
     */
    public static String getFinalBackgroundColor(ThemeInterface theme, String bgColor) {
        return getFinalColor(bgColor, theme.colors().background());
    }


    /**
     * Applies common border styling.
     */
    public static void applyBorderStyling(Node node, ThemeInterface theme, String borderColor, int borderWidth, int borderRadius) {
        String finalBorderColor = getFinalBorderColor(theme, borderColor);
        Utils.updateBorderColor(node, finalBorderColor);

        int finalBorderWidth = getFinalBorderWidth(theme, borderWidth);
        if (finalBorderWidth > 0) {
            Utils.updateBorderWidth(node, finalBorderWidth);
        }

        int finalBorderRadius = getFinalBorderRadius(theme, borderRadius);
        Utils.updateBorderRadius(node, finalBorderRadius);
    }
}
