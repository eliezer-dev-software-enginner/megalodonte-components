package megalodonte.components;

@FunctionalInterface
public interface Renderer<T> {
    Component render(T item);
}

