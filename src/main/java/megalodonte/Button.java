package megalodonte;


public class Button extends Component {
    private final javafx.scene.control.Button text;

    public Button(String textContent){
        super(new javafx.scene.control.Button(textContent));
        this.text = (javafx.scene.control.Button) this.node;
    }

    public Button(String textContent, ButtonProps props){
        super(new javafx.scene.control.Button(textContent), props);
        this.text = (javafx.scene.control.Button) this.node;
    }

    public Button(ReadableState<String> state) {
        super(new javafx.scene.control.Button());
        this.text = (javafx.scene.control.Button) this.node;

        state.subscribe(text::setText);
    }

    public Button(ReadableState<String> state, ButtonProps props) {
        super(new javafx.scene.control.Button(), props);
        this.text = (javafx.scene.control.Button) this.node;

        state.subscribe(text::setText);
    }
}
