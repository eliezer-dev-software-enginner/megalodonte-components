package megalodonte.components;

import java.util.function.Consumer;

/**
 * Contrato comum de foco para campos de texto com implementações distintas,
 * como {@link megalodonte.components.inputs.TextAreaInput} e
 * {@link megalodonte.components.inputs.Input}.
 * <p>
 * Só cobre o subconjunto de métodos que faz sentido pros dois: focar
 * programaticamente e reagir a mudança de foco. Métodos de formatação de valor
 * (onChange/onInitialize) e slots de ícone (left/right) ficam de fora — o
 * primeiro par é sobre máscara de valor (não faz sentido pra um campo de texto
 * livre como um TextArea), o segundo exigiria reestruturar o node raiz do
 * TextAreaInput (hoje é o próprio TextArea, sem StackPane em volta).
 */
public interface FocusableFieldInterface<T extends FocusableFieldInterface<T>> {

    void requestFocus();

    T onChangeFocus(Consumer<Boolean> handler);
}
