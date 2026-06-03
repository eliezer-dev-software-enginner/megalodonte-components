package megalodonte.props;

import javafx.scene.Node;
import javafx.scene.control.Button;
import megalodonte.ReadableState;
import megalodonte.theme.Theme;
import megalodonte.utils.Utils;

import static megalodonte.styles.util.StyleUtils.*;

public class ButtonProps extends TextComponentProps<ButtonProps> {
    private int height;
    private boolean fillWidth;
    protected String bgColor;
    protected String borderColor;
    protected int borderWidth;
    protected int borderRadius;

    //----------------States
    private ReadableState<String> bgColorState;

    // Fluent API methods
    public ButtonProps bgColor(String bgColor) {
        this.bgColor = bgColor;
        return this;
    }

    public ButtonProps bgColor(ReadableState<String> bgColorState) {
        this.bgColorState = bgColorState;

        return this;
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

    @Override
    public ButtonProps textColor(String color) {
        return super.textColor(color);
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

    private static final String
            BTN_PRIMARY = "#2563eb",
            BTN_SECONDARY = "#6b7280",
            BTN_SUCCESS = "#10b981",
            BTN_WARNING = "#f59e0b",
            BTN_DANGER = "#ef4444",
            BTN_GHOST = "transparent",
            BTN_DISABLED = "#94a3b8";

    private String getButtonColorFromVariant(ButtonProps props) {
        return switch (props.getVariant()) {
            case "secondary" -> BTN_SECONDARY;
            case "success" -> BTN_SUCCESS;
            case "warning" -> BTN_WARNING;
            case "danger" -> BTN_DANGER;
            case "ghost" -> BTN_GHOST;
            case "disabled" -> BTN_DISABLED;
            default -> BTN_PRIMARY;
        };
    }

    private String getButtonTextColor(ButtonProps props, Theme theme) {
        return switch (props.getVariant()) {
            case "ghost", "disabled" -> theme.colors().textSecondary();
            default -> "white";
        };
    }

    /**
     * Applies common border styling.
     */
    protected void applyBorderStyling(Node node, Theme theme) {
        if (borderWidth > 0) {
            String finalBorderColor = getFinalBorderColor(theme, borderColor);
            Utils.updateBorderColor(node, finalBorderColor);
            Utils.updateBorderWidth(node, borderWidth);
        } else {
            // Sem borda definida, só zera para não herdar do CSS padrão
            Utils.updateBorderColor(node, "transparent");
            Utils.updateBorderWidth(node, 0);
        }

        int finalRadius = borderRadius > 0 ? borderRadius : theme.border().radiusMd();
        Utils.updateBorderRadius(node, finalRadius);
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

        String finalTextColor = getButtonTextColor((ButtonProps) props, theme);

        if (textColor != null) {
            applyColor(node, textColor, Utils.FX_TEXT_FILL);
        } else {
            applyColor(node, finalTextColor, Utils.FX_TEXT_FILL);
        }

        // bgColor só aplica se não há state reativo controlando
        if (bgColorState == null) {
            String finalBgColor = bgColor != null ? bgColor : getButtonColorFromVariant((ButtonProps) props);
            applyColor(node, finalBgColor, Utils.FX_BG_COLOR);
        }


        applyBorderStyling(button, theme);
    }

    @Override
    protected void bindStates(Node node) {
        bind(node, bgColorState, color ->
                applyColor(node, color, Utils.FX_BG_COLOR)
        );
    }
}
