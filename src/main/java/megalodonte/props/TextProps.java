package megalodonte.props;

import javafx.scene.Node;
import javafx.scene.text.Text;
import megalodonte.base.scale.ScaleProvider;
import megalodonte.base.theme.ThemeInterface;

import static megalodonte.styles.util.StyleUtils.updateFontSize;
import static megalodonte.styles.util.StyleUtils.updateFontWeight;
import static megalodonte.styles.util.StyleUtils.updateTextColor;

public class TextProps extends TextComponentProps<TextProps> {
    private TextTone tone = TextTone.PRIMARY;

    public TextProps tone(TextTone tone) {
        this.tone = tone;
        return this;
    }

    public TextTone getTone() {
        return tone;
    }


    @Override
    protected void applyTheme(Node node, Props props, ThemeInterface theme) {
        if (!(node instanceof Text t)) return;

        if (getFontSize() != null) {
            updateFontSize(t, ScaleProvider.scale(getFontSize()));
        }

        if(getFontWeight() != null){
            updateFontWeight(t, getFontWeight());
        }

        applyTextStyling(t, theme, (TextProps) props);
    }

    /**
     * Applies text styling for Text components.
     */
    protected void applyTextStyling(javafx.scene.text.Text textNode, ThemeInterface theme, megalodonte.props.TextProps props) {
        String finalTextColor = getFinalTextColor(theme, props);
        updateTextColor(textNode, finalTextColor);

        int fontSize = props.getFontSize() != null ?
                ScaleProvider.scale(props.getFontSize()) :
                theme.typography().body();
        updateFontSize(textNode, fontSize);
    }

    /**
     * Gets the final text color based on tone with custom color fallback.
     */
    protected String getFinalTextColor(ThemeInterface theme, TextProps props) {
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
