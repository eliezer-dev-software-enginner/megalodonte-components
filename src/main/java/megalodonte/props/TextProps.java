package megalodonte.props;

import javafx.scene.Node;
import javafx.scene.text.Text;
import megalodonte.base.scale.ScaleProvider;
import megalodonte.base.theme.ThemeInterface;
import megalodonte.styles.util.Utils;
import megalodonte.utils.related.TextVariant;

public class TextProps extends TextComponentProps<TextProps> {
    private TextTone tone = TextTone.PRIMARY;

    public TextProps tone(TextTone tone) {
        this.tone = tone;
        return this;
    }

    private TextVariant variant = TextVariant.BODY;

    public TextProps variant(TextVariant variant) {
        this.variant = variant;
        return this;
    }

    public TextVariant getVariant() {
        return variant;
    }

    public TextTone getTone() {
        return tone;
    }


    @Override
    protected void applyTheme(Node node, Props props, ThemeInterface theme) {
        if (!(node instanceof Text t)) return;

        if (getFontSize() != null) {
            Utils.updateFontSize(t, ScaleProvider.scale(getFontSize()));
        }

        if(getFontWeight() != null){
            Utils.updateFontWeight(t, getFontWeight());
        }

        applyTextStyling(t, theme, (TextProps) props);
    }

    /**
     * Applies text styling for Text components.
     */
    protected void applyTextStyling(javafx.scene.text.Text textNode, ThemeInterface theme, megalodonte.props.TextProps props) {
        String finalTextColor = getFinalTextColor(theme, props);
        Utils.updateTextColor(textNode, finalTextColor);

        int fontSize = props.getFontSize() != null ?
                ScaleProvider.scale(props.getFontSize()) :
                theme.typography().resolve(props.getVariant());
        Utils.updateFontSize(textNode, fontSize);
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
