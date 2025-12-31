package megalodonte;

@FunctionalInterface
public interface Renderer<T> {
    Component render(T item);
}

