package megalodonte.props;

import javafx.scene.Node;
import javafx.scene.text.Text;
import megalodonte.theme.Theme;
import megalodonte.utils.Utils;
import megalodonte.utils.related.TextVariant;

public class TextProps extends TextComponentProps {
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


    private void applyFontWeight(Text t, String weight) {
        var current = t.getStyle();

        var updated = Utils.UpdateEspecificStyle(
                current,
                Utils.FX_FONT_WEIGHT,
                String.valueOf(weight)
        );
        t.setStyle(updated);
    }

    private void applyColor(Text t, String color) {
        var current = t.getStyle();
        var updated = Utils.UpdateEspecificStyle(
                current,
                Utils.FX_FILL,
                color
        );
        t.setStyle(updated);
    }

    @Override
    protected void applyTheme(Node node, Props props, Theme theme) {
        if (!(node instanceof Text t)) return;

        if (getFontSize() != null) {
            Utils.updateFontSize(t, getFontSize());
        }

        if(getFontWeight() != null){
            applyFontWeight(t, getFontWeight());
        }

        applyTextStyling(t, theme, (TextProps) props);
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
