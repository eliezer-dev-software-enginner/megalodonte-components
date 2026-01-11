package megalodonte.props;

import javafx.scene.Node;
import javafx.scene.text.Text;
import megalodonte.ReadableState;
import megalodonte.utils.Utils;
import megalodonte.utils.related.TextVariant;

public class TextProps extends Props {
    private Integer fontSize;
    private ReadableState<Integer> fontSizeState;
    private String espessura;

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

    public Integer getFontSize() {
        return fontSize;
    }

    public TextVariant getVariant() {
        return variant;
    }

    public TextTone getTone() {
        return tone;
    }

    public TextProps fontSize(int fontSize){
        this.fontSize = fontSize;
        return this;
    }

    public TextProps fontSize(ReadableState<Integer> state) {
        this.fontSizeState = state;
        return this;
    }

    public TextProps bold(){
        this.espessura = "bold";
        return this;
    }

    @Override
     public void apply(Node node) {
        if (!(node instanceof Text t)) return;

        if (fontSize != null) {
            Utils.updateFontSize(t, fontSize);
        }

        if (fontSizeState != null) {
            fontSizeState.subscribe(v -> Utils.updateFontSize(t, v));
        }

        if(espessura != null){
            applyFontWeight(t,espessura);
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
