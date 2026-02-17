package megalodonte.components;


import megalodonte.ReadableState;
import megalodonte.props.TextProps;

public class Text extends TextComponent {
    private final javafx.scene.text.Text text;

    public Text(String textContent){
        super(new javafx.scene.text.Text(textContent), new TextProps());
        this.text = (javafx.scene.text.Text) this.node;
        disableClicks();
    }

    public Text(String textContent, TextProps props){
        super(new javafx.scene.text.Text(textContent), props);
        this.text = (javafx.scene.text.Text) this.node;
        disableClicks();
    }


    public Text(ReadableState<String> state) {
        super(new javafx.scene.text.Text(), new TextProps());
        this.text = (javafx.scene.text.Text) this.node;

        state.subscribe(text::setText);
        disableClicks();
    }

    public Text(ReadableState<String> state, TextProps props) {
        super(new javafx.scene.text.Text(), props);
        this.text = (javafx.scene.text.Text) this.node;

        state.subscribe(text::setText);
        disableClicks();
    }

    private void disableClicks() {
        text.setMouseTransparent(true);
    }
}