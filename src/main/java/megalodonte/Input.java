package megalodonte;

import javafx.scene.control.TextField;

public class Input extends Component {
    private final TextField tf;

    public Input(String textContent){
        super(new TextField(textContent));
        this.tf = (TextField) this.node;
    }

    public Input(String textContent, InputProps props){
        super(new TextField(textContent), props);
        this.tf = (TextField) this.node;
    }

    public Input(State<String> state) {
        super(new TextField());
        this.tf = (TextField) this.node;

        // State → Input
        state.subscribe(tf::setText);

        // Input → State
        tf.textProperty().addListener((obs, old, val) -> {
            state.set(val);
        });
    }


    public Input(State<String> state, InputProps props) {
        super(new TextField(), props);
        this.tf = (TextField) this.node;

        state.subscribe(tf::setText);

        tf.textProperty().addListener((obs, old, val) -> {
            state.set(val);
        });
    }

}
