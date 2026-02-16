// Solução temporária: wrapper para inputs monetários que aplica formatação ao carregar dados
package megalodonte.components;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.TextInputControl;
import javafx.scene.layout.StackPane;
import megalodonte.components.Component;
import megalodonte.props.InputProps;
import megalodonte.State;
import megalodonte.styles.InputStyler;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

public class CurrencyInput extends Component {

    protected final TextInputControl field;
    protected final StackPane container;

    public CurrencyInput(State<String> state) {
        super(new StackPane());
        this.field = new javafx.scene.control.TextField();
        this.container = (StackPane) getNode();

        container.getChildren().add(field);

        // Configuração básica
        field.setPadding(new Insets(8));
        field.setStyle("-fx-font-size: 14px; -fx-border-radius: 4px;");

        // Aplica formatação inicial se for valor monetário
        String initialValue = state.get();
        if (initialValue != null && !initialValue.isEmpty() && initialValue.matches("\\d+")) {
            try {
                BigDecimal realValue = new BigDecimal(initialValue).movePointLeft(2);
                String formattedValue = NumberFormat.getCurrencyInstance(new Locale("pt", "BR")).format(realValue);
                if (!formattedValue.equals(initialValue)) {
                    field.setText(formattedValue);
                }
            } catch (NumberFormatException e) {
                // Mantém valor original se falhar conversão
            }
        } else {
            field.setText(initialValue != null ? initialValue : "");
        }
    }

    @Override
    public Node getNode() {
        return container;
    }

    public void setText(String value) {
        field.setText(value);
    }

    public String getText() {
        return field.getText();
    }
}