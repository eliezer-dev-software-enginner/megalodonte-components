package megalodonte.props;

import javafx.scene.Node;
import javafx.scene.control.TextInputControl;
import javafx.scene.layout.StackPane;
import megalodonte.utils.Utils;
import megalodonte.utils.related.TextVariant;

//TODO: criar placeholderSize
public class InputProps extends TextComponentProps {
    @Deprecated(forRemoval = true)
    private String color;

    //TODO: mover para inputStyler
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

    public InputProps color(String color){
        this.color = color;
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

    @Override
     public void apply(Node node) {
        if (!(node instanceof StackPane stackPane)) return;

        var input = (TextInputControl) stackPane.getChildren().get(0);
        if(input == null) return;

        if (getFontSize() != null) {
          Utils.updateFontSize(input, getFontSize());
        }

        if (getFontSizeState() != null) {
            getFontSizeState().subscribe(v ->  Utils.updateFontSize(input, v));
        }

        if (color != null) {
          Utils.updateTextColor_Input(input, color);
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
    }
}
