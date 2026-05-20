package megalodonte.components.inputs;

import javafx.scene.control.TextArea;
import megalodonte.props.InputProps;
import megalodonte.State;

import megalodonte.base.components.Component;

public class TextAreaInput extends Component {

    private final TextArea textArea;

    public TextAreaInput(State<String> state, InputProps props) {
        super(new TextArea(), props);
        this.textArea = (TextArea) node;

        // Fix dos cantos arredondados via stylesheet
        textArea.getStylesheets().add(
                getClass().getResource("/text-area.css").toExternalForm()
        );

        if (props != null) props.apply(textArea);

        // Bind bidirecional
        state.subscribe(v -> {
            if (!textArea.getText().equals(v)) {
                textArea.setText(v == null ? "" : v);
            }
        });

        textArea.textProperty().addListener((obs, old, v) -> {
            if (!v.equals(state.get())) {
                state.set(v);
            }
        });
    }
}
