package megalodonte.components;
import megalodonte.base.components.Component;
@FunctionalInterface
public interface Renderer<T> {
    Component render(T item);
}

