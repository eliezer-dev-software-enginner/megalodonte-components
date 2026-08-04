package megalodonte.components;

import java.util.function.Consumer;

/**
 * Contrato comum entre componentes de campo de texto que não compartilham
 * implementação (ex: {@link megalodonte.components.inputs.TextAreaInput}, que
 * envolve um {@code TextArea} nativo, e {@link megalodonte.components.v2.Input},
 * que não usa nenhum {@code Control} nativo por baixo) — garante a mesma API
 * de foco em ambos, mesmo com internals completamente diferentes.
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
