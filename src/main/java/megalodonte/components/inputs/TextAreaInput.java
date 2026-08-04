package megalodonte.components.inputs;

import javafx.scene.control.TextArea;
import megalodonte.components.FocusableFieldInterface;
import megalodonte.components.Scroll;
import megalodonte.props.InputProps;
import megalodonte.base.state.State;

import megalodonte.base.components.Component;

import java.util.function.Consumer;

public class TextAreaInput extends Component implements FocusableFieldInterface<TextAreaInput> {

    private final TextArea textArea;

    public TextAreaInput(State<String> state, InputProps props) {
        super(new TextArea(), props);
        this.textArea = (TextArea) node;

        // Fix dos cantos arredondados via stylesheet
        textArea.getStylesheets().add(
                getClass().getResource("/text-area.css").toExternalForm()
        );

        if (props != null) props.apply(textArea);

        // Sem isso, rolar o mouse dentro do TextArea depois que ele já rolou até o
        // limite do próprio conteúdo continua propagando o evento pro ScrollPane da
        // tela (todo CRUD screen já fica dentro de um, ver Components.ScrollPaneDefault)
        // — a página inteira "pula" junto com o texto. addEventHandler roda depois do
        // TextArea já ter processado o scroll normalmente, então só precisamos consumir
        // o evento aqui pra travar a propagação, sem reimplementar rolagem nenhuma.
        Scroll.confineScrollEvents(textArea);

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

    @Override
    public void requestFocus() {
        megalodonte.base.UI.runOnUi(textArea::requestFocus);
    }

    @Override
    public TextAreaInput onChangeFocus(Consumer<Boolean> handler) {
        textArea.focusedProperty().addListener((obs, old, isFocused) -> handler.accept(isFocused));
        return this;
    }
}
