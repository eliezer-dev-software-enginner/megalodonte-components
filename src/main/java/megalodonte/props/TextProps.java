package megalodonte.props;

import javafx.scene.Node;
import javafx.scene.text.Text;
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

    @Override
     public void apply(Node node) {
        if (!(node instanceof Text t)) return;

        if (getFontSize() != null) {
            Utils.updateFontSize(t, getFontSize());
        }

        if (getFontSizeState() != null) {
            getFontSizeState().subscribe(v -> Utils.updateFontSize(t, v));
        }

        if(getFontWeight() != null){
            applyFontWeight(t, getFontWeight());
        }
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
}
