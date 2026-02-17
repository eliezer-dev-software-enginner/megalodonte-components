package megalodonte.styles;

import javafx.scene.Node;
import javafx.scene.control.Button;
import megalodonte.props.ButtonProps;
import megalodonte.theme.Theme;
import megalodonte.theme.ThemeManager;
import megalodonte.utils.Utils;

import static megalodonte.styles.util.StyleUtils.*;

public class ButtonStyler extends TextComponentStyler<ButtonProps, ButtonStyler> {
    protected String bgColor;
    protected String borderColor;
    protected int borderWidth;
    protected int borderRadius;

    // Fluent API methods
    public ButtonStyler bgColor(String bgColor) {
        this.bgColor = bgColor;
        return  this;
    }

    public ButtonStyler borderColor(String borderColor) {
        this.borderColor = borderColor;
        return this;
    }

    public ButtonStyler borderWidth(int borderWidth) {
        this.borderWidth = borderWidth;
        return this;
    }

    public ButtonStyler borderRadius(int borderRadius) {
        this.borderRadius = borderRadius;
        return this;
    }

    private String getButtonColor(ButtonProps props) {
        return switch (props.getVariant()) {
            case "secondary" -> ThemeManager.buttonSecondary();
            case "success" -> ThemeManager.buttonSuccess();
            case "warning" -> ThemeManager.buttonWarning();
            case "danger" -> ThemeManager.buttonDanger();
            case "ghost" -> ThemeManager.buttonGhost();
            case "disabled" -> ThemeManager.buttonDisabled();
            default -> ThemeManager.buttonPrimary();
        };
    }

    private String getButtonTextColor(ButtonProps props) {
        return switch (props.getVariant()) {
            case "ghost", "disabled" -> ThemeManager.theme().colors().textSecondary();
            default -> "white";
        };
    }


    @Override
    protected void applyTheme(Node node, ButtonProps props, Theme theme) {
        if (!(node instanceof Button button)) return;

        String finalBgColor = getButtonColor(props);
        String finalTextColor = getButtonTextColor(props);

        if (textColor != null) {
            applyColor(node, textColor, Utils.FX_TEXT_FILL);
        } else {
            applyColor(node, finalTextColor, Utils.FX_TEXT_FILL);
        }

        if (bgColor != null) {
            applyColor(node, bgColor, Utils.FX_BG_COLOR);
        } else {
            applyColor(node, finalBgColor, Utils.FX_BG_COLOR);
        }
        
        applyBackgroundStyling(button, theme);
        applyBorderStyling(button, theme);
    }

    /**
     * Applies common background styling.
     */
    protected void applyBackgroundStyling(Node node, Theme theme) {
        String finalBgColor = getFinalBackgroundColor(theme, bgColor);
        Utils.updateBackgroundColor(node, finalBgColor);
    }

    /**
     * Applies common border styling.
     */
    protected void applyBorderStyling(Node node, Theme theme) {
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
