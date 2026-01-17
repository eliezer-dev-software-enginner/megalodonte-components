package megalodonte.components;


import megalodonte.ReadableState;
import megalodonte.props.TextProps;
import megalodonte.styles.TextStyler;

public class Text extends Component {
    private final javafx.scene.text.Text text;

    public Text(String textContent){
        super(new javafx.scene.text.Text(textContent));
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

    /**
     * Desativa cliques no componente Text para evitar interações indesejadas.
     * Text components should not be interactive by default.
     */
    private void disableClicks() {
        text.setMouseTransparent(true);
    }

    /**
     * Creates a new Text component with the specified content.
     * 
     * @param textContent the text content
     * @return a new Text instance
     */
    public static Text of(String textContent) {
        return new Text(textContent);
    }

    /**
     * Creates a new Text component with the specified content and properties.
     * 
     * @param textContent the text content
     * @param props the text properties
     * @return a new Text instance
     */
    public static Text of(String textContent, TextProps props) {
        return new Text(textContent, props);
    }

    /**
     * Creates a new Text component with the specified content, properties, and styler.
     * 
     * @param textContent the text content
     * @param props the text properties
     * @param styler the text styler
     * @return a new Text instance
     */
    public static Text of(String textContent, TextProps props, TextStyler styler) {
        return new Text(textContent, props, styler);
    }

    /**
     * Creates a new Text component bound to the specified state.
     * 
     * @param state the readable state containing text content
     * @return a new Text instance
     */
    public static Text of(ReadableState<String> state) {
        return new Text(state);
    }

    /**
     * Creates a new Text component bound to the specified state with properties.
     * 
     * @param state the readable state containing text content
     * @param props the text properties
     * @return a new Text instance
     */
    public static Text of(ReadableState<String> state, TextProps props) {
        return new Text(state, props);
    }
}