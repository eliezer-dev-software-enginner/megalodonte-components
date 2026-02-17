package megalodonte.props;

import javafx.scene.Node;
import javafx.scene.control.TextInputControl;
import javafx.scene.layout.StackPane;
import megalodonte.theme.Theme;
import megalodonte.utils.Utils;
import megalodonte.utils.related.TextVariant;

import static megalodonte.styles.util.StyleUtils.getFinalBackgroundColor;
import static megalodonte.styles.util.StyleUtils.getFinalBorderColor;

//TODO: criar placeholderSize
public class InputProps extends TextComponentProps<InputProps> {

    //TODO: mover para InputProps
    @Deprecated(forRemoval = true)
    private String placeholder;
    private int height;
    private int width;

    private TextTone tone = TextTone.PRIMARY;

    public InputProps tone(TextTone tone) {
        this.tone = tone;
        return this;
    }

    private TextVariant variant = TextVariant.BODY;

    public InputProps variant(TextVariant variant) {
        this.variant = variant;
        return this;
    }

    public TextVariant getVariant() {
        return variant;
    }

    public TextTone getTone() {
        return tone;
    }

    public InputProps height(int height){
        this.height = height;
        return this;
    }

    public InputProps width(int width){
        this.width = width;
        return this;
    }

    public InputProps placeHolder(String text){
        this.placeholder = text;
        return this;
    }

    boolean disabled;
    public InputProps disable(){
        this.disabled = true;
        return this;
    }

    protected String bgColor;
    protected String borderColor;
    protected int borderWidth;
    protected int borderRadius;

    // Fluent API methods
    public InputProps bgColor(String bgColor) {
        this.bgColor = bgColor;
        return  this;
    }

    public InputProps borderColor(String borderColor) {
        this.borderColor = borderColor;
        return this;
    }

    public InputProps borderWidth(int borderWidth) {
        this.borderWidth = borderWidth;
        return this;
    }

    public InputProps borderRadius(int borderRadius) {
        this.borderRadius = borderRadius;
        return this;
    }

    protected String placeholderColor;

    @Override
    protected void applyTheme(Node node, Props props, Theme theme) {
        if (!(node instanceof StackPane stackPane)) return;

        var input = (TextInputControl) stackPane.getChildren().get(0);
        if(input == null) return;

        if (getFontSize() != null) {
            Utils.updateFontSize(input, getFontSize());
        }

        if (getFontSizeState() != null) {
            getFontSizeState().subscribe(v ->  Utils.updateFontSize(input, v));
        }

        if(disabled){
            input.setDisable(true);
        }

        if (placeholder != null) {
            input.setPromptText(placeholder);
        }

        if(height > 0){
            input.setPrefHeight(height);
            input.setMinHeight(height);
            input.setMaxHeight(height);
        }

        if(width > 0){
            stackPane.setPrefWidth(width);
            stackPane.setMinWidth(width);
            stackPane.setMaxWidth(width);
        }

        // Apply background styling to both stackpane and input
        String finalBgColor = getFinalBackgroundColor(theme, bgColor);
        Utils.updateBackgroundColor(stackPane, finalBgColor);
        Utils.updateBackgroundColor(input, finalBgColor);

        // Apply border styling without custom radius
        applyInputBorderStyling(stackPane, theme);

        // Apply text styling to input
        applyInputTextStyling(input, theme, (InputProps) props);
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
