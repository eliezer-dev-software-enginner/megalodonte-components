package megalodonte.styles;

import javafx.scene.Node;
import megalodonte.props.TextProps;
import megalodonte.theme.Theme;
import megalodonte.utils.Utils;

public class TextStyler extends TextComponentStyler<TextProps, TextStyler> {

    @Override
    protected void applyTheme(Node node, TextProps props, Theme theme) {
        if (!(node instanceof javafx.scene.text.Text textNode)) return;
        
        applyTextStyling(textNode, theme, props);
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
     * Gets the final text color based on tone with custom color fallback.
     */
    protected String getFinalTextColor(Theme theme, TextProps props) {
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
