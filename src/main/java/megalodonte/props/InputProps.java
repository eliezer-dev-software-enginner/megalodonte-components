package megalodonte.props;

import javafx.scene.Node;
import javafx.scene.control.TextInputControl;
import javafx.scene.layout.StackPane;
import megalodonte.ReadableState;
import megalodonte.utils.Utils;
import megalodonte.utils.related.TextVariant;

public class InputProps extends Props {
    private Integer fontSize = 14;
    private ReadableState<Integer> fontSizeState;
    private String color;
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

    public InputProps fontSize(int fontSize){
        this.fontSize = fontSize;
        return this;
    }

    public InputProps fontSize(ReadableState<Integer> state) {
        this.fontSizeState = state;
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

    @Override
     public void apply(Node node) {
        //if (!(node instanceof TextInputControl t)) return;
        if (!(node instanceof StackPane stackPane)) return;

        var input = (TextInputControl) stackPane.getChildren().get(0);
        if(input == null) return;

        if (fontSize != null) {
          Utils.updateFontSize(input, fontSize);
        }

        if (fontSizeState != null) {
            fontSizeState.subscribe(v ->  Utils.updateFontSize(input, fontSize));
        }

        if (color != null) {
          Utils.updateTextColor_Input(input, color);
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
