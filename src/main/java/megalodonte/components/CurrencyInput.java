// Solução temporária: wrapper para inputs monetários que aplica formatação ao carregar dados
package megalodonte.components;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.TextInputControl;
import javafx.scene.layout.StackPane;
import megalodonte.base.state.State;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;
import megalodonte.base.components.Component;

import static megalodonte.styles.util.StyleUtils.updateBorderRadius;
import static megalodonte.styles.util.StyleUtils.updateFontSize;

public class CurrencyInput extends Component  {

    protected final TextInputControl field;
    protected final StackPane container;

    public CurrencyInput(State<String> state) {
        super(new StackPane());
        this.field = new javafx.scene.control.TextField();
        this.container = (StackPane) getJavaFxNode();

        container.getChildren().add(field);

        // Configuração básica
        field.setPadding(new Insets(8));
        updateFontSize(field, 14);
        updateBorderRadius(field, 4);

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
    public Node getJavaFxNode() {
        return container;
    }

    public void setText(String value) {
        field.setText(value);
    }

    public String getText() {
        return field.getText();
    }
}