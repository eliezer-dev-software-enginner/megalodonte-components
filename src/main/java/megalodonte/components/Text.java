package megalodonte.components;


import megalodonte.ReadableState;
import megalodonte.TextStyler;
import megalodonte.props.TextProps;

public class Text extends Component {
    private final javafx.scene.text.Text text;

    public Text(String textContent){
        super(new javafx.scene.text.Text(textContent));
        this.text = (javafx.scene.text.Text) this.node;
    }

    public Text(String textContent, TextProps props){
        super(new javafx.scene.text.Text(textContent), props, new TextStyler());
        this.text = (javafx.scene.text.Text) this.node;
    }

    public Text(String textContent, TextProps props, TextStyler styler){
        super(new javafx.scene.text.Text(textContent), props, styler);
        this.text = (javafx.scene.text.Text) this.node;
    }

    public Text(ReadableState<String> state) {
        super(new javafx.scene.text.Text(), new TextProps(), new TextStyler());
        this.text = (javafx.scene.text.Text) this.node;

        state.subscribe(text::setText);
    }

    public Text(ReadableState<String> state, TextProps props) {
        super(new javafx.scene.text.Text(), props, new TextStyler());
        this.text = (javafx.scene.text.Text) this.node;

        state.subscribe(text::setText);
    }
}
