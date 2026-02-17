package megalodonte.props;

import javafx.scene.Node;
import javafx.scene.control.Button;
import megalodonte.theme.Theme;
import megalodonte.theme.ThemeManager;
import megalodonte.utils.Utils;

import static megalodonte.styles.util.StyleUtils.*;

public class ButtonProps extends TextComponentProps<ButtonProps> {
    private int height;
    private boolean fillWidth;
    protected String bgColor;
    protected String borderColor;
    protected int borderWidth;
    protected int borderRadius;

    // Fluent API methods
    public ButtonProps bgColor(String bgColor) {
        this.bgColor = bgColor;
        return  this;
    }

    public ButtonProps borderColor(String borderColor) {
        this.borderColor = borderColor;
        return this;
    }

    public ButtonProps borderWidth(int borderWidth) {
        this.borderWidth = borderWidth;
        return this;
    }

    public ButtonProps borderRadius(int borderRadius) {
        this.borderRadius = borderRadius;
        return this;
    }

    private String variant = "primary";

    public ButtonProps fillWidth() {
        this.fillWidth = true;
        return this;
    }

    public ButtonProps height(int height) {
        this.height = height;
        return this;
    }

    //TODO: criar enum ButtonVariant e usá-la
    public ButtonProps variant(String variant) {
        this.variant = variant;
        return this;
    }

    public ButtonProps primary() {
        this.variant = "primary";
        return this;
    }

    public ButtonProps secondary() {
        this.variant = "secondary";
        return this;
    }

    public ButtonProps success() {
        this.variant = "success";
        return this;
    }

    public ButtonProps warning() {
        this.variant = "warning";
        return this;
    }

    public ButtonProps danger() {
        this.variant = "danger";
        return this;
    }

    public ButtonProps ghost() {
        this.variant = "ghost";
        return this;
    }

    public ButtonProps disabled() {
        this.variant = "disabled";
        return this;
    }

    public String getVariant() {
        return variant;
    }

    private String getButtonColorFromVariant(ButtonProps props) {
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

    @Override
    protected void applyTheme(Node node, Props props, Theme theme) {
        if (!(node instanceof Button button)) return;

        if (fillWidth) {
            button.setMaxWidth(Double.MAX_VALUE);
        }

        if (getFontSize() != null) {
            Utils.updateFontSize(button, getFontSize());
        }

        if (height > 0) {
            button.setPrefHeight(height);
            button.setMinHeight(height);
            button.setMaxHeight(height);
        }

        String finalBgColor = getButtonColorFromVariant((ButtonProps) props);
        String finalTextColor = getButtonTextColor((ButtonProps) props);

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
        applyBorderStyling(button, theme);
    }
}
