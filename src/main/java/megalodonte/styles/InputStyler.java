package megalodonte.styles;

import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import megalodonte.components.inputs.Input;
import megalodonte.props.InputProps;
import megalodonte.props.Props;
import megalodonte.theme.Theme;
import megalodonte.utils.Utils;

import static megalodonte.styles.util.StyleUtils.*;

public class InputStyler extends TextComponentStyler<InputProps, InputStyler> {
    protected String bgColor;
    protected String borderColor;
    protected int borderWidth;
    protected int borderRadius;

    // Fluent API methods
    public InputStyler bgColor(String bgColor) {
        this.bgColor = bgColor;
        return  this;
    }

    public InputStyler borderColor(String borderColor) {
        this.borderColor = borderColor;
        return this;
    }

    public InputStyler borderWidth(int borderWidth) {
        this.borderWidth = borderWidth;
        return this;
    }

    public InputStyler borderRadius(int borderRadius) {
        this.borderRadius = borderRadius;
        return this;
    }

    protected String placeholderColor;


    @Override
    protected void applyTheme(Node node, InputProps props, Theme theme) {
        if (!(node instanceof StackPane stackPane)) return;

        var input = stackPane.getChildren().get(0);
        if(input == null) return;

        // Apply background styling to both stackpane and input
        String finalBgColor = getFinalBackgroundColor(theme, bgColor);
        Utils.updateBackgroundColor(stackPane, finalBgColor);
        Utils.updateBackgroundColor(input, finalBgColor);

        // Apply border styling without custom radius
        applyInputBorderStyling(stackPane, theme);

        // Apply text styling to input
        applyInputTextStyling(input, theme, props);
    }

    /**
     * Apply border styling for inputs without custom radius, using only theme defaults.
     */
    private void applyInputBorderStyling(StackPane stackPane, Theme theme) {
        String finalBorderColor = getFinalBorderColor(theme, borderColor);
        Utils.updateBorderColor(stackPane, finalBorderColor);
        
        int finalBorderWidth = theme.border().width();
        if (finalBorderWidth > 0) {
            Utils.updateBorderWidth(stackPane, finalBorderWidth);
        }
        
//        // Use only theme radius, ignore custom borderRadius setting
//        int themeBorderRadius = theme.radius().md();
//        if (themeBorderRadius > 0) {
//            Utils.updateBorderRadius(stackPane, themeBorderRadius);
//        }

        Utils.updateBorderRadius(stackPane, 0);
    }


    /**
     * Applies text styling for Input components.
     */
    protected void applyInputTextStyling(Node inputNode, Theme theme, InputProps props) {
        String finalTextColor = getFinalInputTextColor(theme, props);
        Utils.updateTextColor_Input(inputNode, finalTextColor);

        String finalPlaceholderColor = placeholderColor != null && !placeholderColor.isBlank() ?
                placeholderColor : getFinalInputTextColor(theme, props);
        Utils.updatePlaceholderColor(inputNode, finalPlaceholderColor);

        int fontSize = theme.typography().resolve(props.getVariant());
        Utils.updateFontSize(inputNode, fontSize);

        // Explicitly set placeholder font size to match input font size
        var currentStyle = inputNode.getStyle();
        var updatedStyle = Utils.UpdateEspecificStyle(currentStyle, "-fx-prompt-font-size", fontSize + "px");
        inputNode.setStyle(updatedStyle);
    }

    /**
     * Gets the final text color for input components with custom color fallback.
     */
    protected String getFinalInputTextColor(Theme theme, InputProps props) {
        if (textColor != null && !textColor.isBlank()) {
            return textColor;
        }

        return switch (props.getTone()) {
            case PRIMARY -> theme.colors().textPrimary();
            case SECONDARY, DISABLED -> theme.colors().textSecondary();
            case INVERTED -> theme.colors().background();
        };
    }

}
