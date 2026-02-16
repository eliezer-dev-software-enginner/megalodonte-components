package megalodonte.components;


import megalodonte.ReadableState;
import megalodonte.props.TextProps;
import megalodonte.styles.TextStyler;

public class Text extends TextComponent<TextProps, TextStyler> {
    private final javafx.scene.text.Text text;

    public Text(String textContent){
        super(new javafx.scene.text.Text(textContent), new TextProps(), new TextStyler());
        this.text = (javafx.scene.text.Text) this.node;
        disableClicks();
    }

    public Text(String textContent, TextProps props){
        super(new javafx.scene.text.Text(textContent), props, new TextStyler());
        this.text = (javafx.scene.text.Text) this.node;
        disableClicks();
    }

    public Text(String textContent, TextProps props, TextStyler styler){
        super(new javafx.scene.text.Text(textContent), props, styler);
        this.text = (javafx.scene.text.Text) this.node;
        disableClicks();
    }

    public Text(ReadableState<String> state) {
        super(new javafx.scene.text.Text(), new TextProps(), new TextStyler());
        this.text = (javafx.scene.text.Text) this.node;

        state.subscribe(text::setText);
        disableClicks();
    }

    public Text(ReadableState<String> state, TextProps props) {
        super(new javafx.scene.text.Text(), props, new TextStyler());
        this.text = (javafx.scene.text.Text) this.node;

        state.subscribe(text::setText);
        disableClicks();
    }

    private void disableClicks() {
        text.setMouseTransparent(true);
    }

    public static Text of(String textContent) {
        return new Text(textContent);
    }

    public static Text of(String textContent, TextProps props) {
        return new Text(textContent, props);
    }

    public static Text of(String textContent, TextProps props, TextStyler styler) {
        return new Text(textContent, props, styler);
    }

    public static Text of(ReadableState<String> state) {
        return new Text(state);
    }

    public static Text of(ReadableState<String> state, TextProps props) {
        return new Text(state, props);
    }
}