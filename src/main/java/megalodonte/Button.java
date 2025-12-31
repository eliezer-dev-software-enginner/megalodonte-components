package megalodonte;


public class Button extends Component {
    private final javafx.scene.control.Button btn;

    public Button(String textContent){
        super(new javafx.scene.control.Button(textContent));
        this.btn = (javafx.scene.control.Button) this.node;
    }

    public Button(String textContent, ButtonProps props){
        super(new javafx.scene.control.Button(textContent), props);
        this.btn = (javafx.scene.control.Button) this.node;
    }

    public Button(ReadableState<String> state) {
        super(new javafx.scene.control.Button());
        this.btn = (javafx.scene.control.Button) this.node;

        state.subscribe(btn::setText);
    }

    public Button(ReadableState<String> state, ButtonProps props) {
        super(new javafx.scene.control.Button(), props);
        this.btn = (javafx.scene.control.Button) this.node;

        state.subscribe(btn::setText);
    }
}
