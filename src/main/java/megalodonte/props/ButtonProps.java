package megalodonte.props;

import javafx.scene.Node;
import javafx.scene.control.Button;
import megalodonte.theme.ThemeManager;
import megalodonte.utils.Utils;

public class ButtonProps extends TextComponentProps {
    private String bgColor;
    private String textColor = "black";

    private int height;
    private boolean fillWidth;
    
    private String variant = "primary";

    private Runnable runnable_onClick;

    public ButtonProps fillWidth() {
        this.fillWidth = true;
        return this;
    }

    public ButtonProps height(int height) {
        this.height = height;
        return this;
    }

    @Deprecated(forRemoval = true)
    public ButtonProps textColor(String color) {
        this.textColor = color;
        return this;
    }

    public ButtonProps bgColor(String color) {
        this.bgColor = color;
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

    public ButtonProps onClick(Runnable callback) {
        this.runnable_onClick = callback;
        return this;
    }

    @Override
    public void apply(Node node) {
        if (!(node instanceof Button t)) return;

        if (fillWidth) {
            t.setMaxWidth(Double.MAX_VALUE);
        }

        if (getFontSize() != null) {
            Utils.updateFontSize(t, getFontSize());
        }

        if (getFontSizeState() != null) {
            getFontSizeState().subscribe(v -> Utils.updateFontSize(t, v));
        }

        String finalBgColor = getButtonColor();
        String finalTextColor = getButtonTextColor();

        if (textColor != null) {
            applyColor(t, textColor, Utils.FX_TEXT_FILL);
        } else {
            applyColor(t, finalTextColor, Utils.FX_TEXT_FILL);
        }

        if (bgColor != null) {
            applyColor(t, bgColor, Utils.FX_BG_COLOR);
        } else {
            applyColor(t, finalBgColor, Utils.FX_BG_COLOR);
        }

        if (height > 0) {
            t.setPrefHeight(height);
            t.setMinHeight(height);
            t.setMaxHeight(height);
        }

        if(runnable_onClick != null){
            t.setOnMouseClicked(e-> runnable_onClick.run());
        }
    }

    private String getButtonColor() {
        return switch (variant) {
            case "secondary" -> ThemeManager.buttonSecondary();
            case "success" -> ThemeManager.buttonSuccess();
            case "warning" -> ThemeManager.buttonWarning();
            case "danger" -> ThemeManager.buttonDanger();
            case "ghost" -> ThemeManager.buttonGhost();
            case "disabled" -> ThemeManager.buttonDisabled();
            default -> ThemeManager.buttonPrimary();
        };
    }

    private String getButtonTextColor() {
        return switch (variant) {
            case "ghost", "disabled" -> ThemeManager.theme().colors().textSecondary();
            default -> "white";
        };
    }

    private void applyColor(Button t, String color, String fxField) {
        var current = t.getStyle();
        var updated = Utils.UpdateEspecificStyle(current, fxField, color);
        t.setStyle(updated);
    }
}
