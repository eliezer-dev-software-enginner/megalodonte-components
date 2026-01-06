package megalodonte.components;

/**
 * Interface para renderizar itens em uma lista ou tabela.
 * Permite personalização completa da aparência de cada item.
 *
 * @param <T> Tipo dos itens a serem renderizados
 */
@FunctionalInterface
public interface ListItemRenderer<T> {
    
    /**
     * Renderiza um item da lista.
     *
     * @param item O item a ser renderizado
     * @param index O índice do item na lista
     * @param props Props do componente List para acessar callbacks
     * @return Component JavaFX representando o item
     */
    Component render(T item, int index, megalodonte.props.ListProps<T> props);
    
    /**
     * Renderiza o cabeçalho da tabela (opcional).
     *
     * @return Component representando o cabeçalho, ou null se não tiver cabeçalho
     */
    default Component renderHeader() {
        return null;
    }
}