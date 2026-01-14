package megalodonte.styles;

import javafx.scene.Node;
import megalodonte.props.InputProps;
import megalodonte.styles.Estilizador;
import megalodonte.props.Props;
import megalodonte.theme.Theme;
import megalodonte.theme.ThemeManager;
import megalodonte.utils.Utils;

/**
 * Base class for all stylers that provides common functionality for theme-aware styling.
 * Eliminates code duplication and ensures consistent theme usage across all stylers.
 */
public abstract class BaseStyler<T extends Props, S extends BaseStyler<T, S>> extends Estilizador<T> {
    
    // Common styling properties
    protected String bgColor;
    protected String borderColor;
    protected int borderWidth;
    protected int borderRadius;
    protected String textColor;
    protected String placeholderColor;
    
    // Fluent API methods
    @SuppressWarnings("unchecked")
    public S bgColor(String bgColor) {
        this.bgColor = bgColor;
        return (S) this;
    }
    
    @SuppressWarnings("unchecked")
    public S borderColor(String borderColor) {
        this.borderColor = borderColor;
        return (S) this;
    }
    
    @SuppressWarnings("unchecked")
    public S borderWidth(int borderWidth) {
        this.borderWidth = borderWidth;
        return (S) this;
    }
    
    @SuppressWarnings("unchecked")
    public S borderRadius(int borderRadius) {
        this.borderRadius = borderRadius;
        return (S) this;
    }
    
    @SuppressWarnings("unchecked")
    public S textColor(String textColor) {
        this.textColor = textColor;
        return (S) this;
    }
    
    @SuppressWarnings("unchecked")
    public S placeholderColor(String placeholderColor) {
        this.placeholderColor = placeholderColor;
        return (S) this;
    }
    
    /**
     * Template method that handles theme subscription and calls applyTheme.
     * Subclasses should implement applyTheme for their specific styling logic.
     */
    @Override
    public final void apply(Node node, T props) {
        ThemeManager.state().subscribe(theme -> {
            if (theme == null) return;
            applyTheme(node, props, theme);
        });
    }
    
    /**
     * Subclasses must implement this method for their specific styling logic.
     * All theme-related styling should be done here.
     */
    protected abstract void applyTheme(Node node, T props, Theme theme);
    
    // Helper methods for consistent theme usage
    
    /**
     * Gets the final background color with theme fallback.
     */
    protected String getFinalBackgroundColor(Theme theme) {
        return getFinalColor(bgColor, theme.colors().background());
    }
    
    /**
     * Gets the final border color with theme fallback.
     */
    protected String getFinalBorderColor(Theme theme) {
        return getFinalColor(borderColor, theme.colors().border());
    }
    
    /**
     * Gets the final text color based on tone with custom color fallback.
     */
    protected String getFinalTextColor(Theme theme, megalodonte.props.TextProps props) {
        if (textColor != null && !textColor.isBlank()) {
            return textColor;
        }
        
        return switch (props.getTone()) {
            case PRIMARY -> theme.colors().textPrimary();
            case SECONDARY, DISABLED -> theme.colors().textSecondary();
            case INVERTED -> theme.colors().background();
        };
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
    
    /**
     * Gets the final border width with theme fallback.
     */
    protected int getFinalBorderWidth(Theme theme) {
        return borderWidth > 0 ? borderWidth : theme.border().width();
    }
    
    /**
     * Gets the final border radius with theme fallback.
     */
    protected int getFinalBorderRadius(Theme theme) {
        return borderRadius > 0 ? borderRadius : theme.radius().md();
    }
    
    /**
     * Generic method to get final color with fallback.
     */
    protected String getFinalColor(String colorField, String fallbackColor) {
        return (colorField != null && !colorField.isBlank()) ? colorField : fallbackColor;
    }
    
    /**
     * Applies common background styling.
     */
    protected void applyBackgroundStyling(Node node, Theme theme) {
        String finalBgColor = getFinalBackgroundColor(theme);
        Utils.updateBackgroundColor(node, finalBgColor);
    }
    
    /**
     * Applies common border styling.
     */
    protected void applyBorderStyling(Node node, Theme theme) {
        String finalBorderColor = getFinalBorderColor(theme);
        Utils.updateBorderColor(node, finalBorderColor);
        
        int finalBorderWidth = getFinalBorderWidth(theme);
        if (finalBorderWidth > 0) {
            Utils.updateBorderWidth(node, finalBorderWidth);
        }
        
//        int finalBorderRadius = getFinalBorderRadius(theme);
//        if (finalBorderRadius > 0) {
//            Utils.updateBorderRadius(node, finalBorderRadius);
//        }

        Utils.updateBorderRadius(node, 0);
    }
    
    /**
     * Applies text styling for Text components.
     */
    protected void applyTextStyling(javafx.scene.text.Text textNode, Theme theme, megalodonte.props.TextProps props) {
        String finalTextColor = getFinalTextColor(theme, props);
        Utils.updateTextColor(textNode, finalTextColor);
        
        int fontSize = props.getFontSize() != null ? 
            props.getFontSize() : 
            theme.typography().resolve(props.getVariant());
        Utils.updateFontSize(textNode, fontSize);
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
}