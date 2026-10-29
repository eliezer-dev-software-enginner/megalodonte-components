package megalodonte.components;

import javafx.scene.layout.VBox;
import megalodonte.base.components.Component;
import megalodonte.base.state.ReadableState;

import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;

/** Reactive vertical list that preserves components for unchanged item identities. */
public final class StateList<T> extends Component {
    public StateList(ReadableState<List<T>> state, Function<T, ? extends Component> render, double spacing) {
        super(new VBox(spacing));
        Objects.requireNonNull(state, "state");
        Objects.requireNonNull(render, "render");
        VBox host = (VBox) node;
        Map<T, Component> cache = new IdentityHashMap<>();
        state.subscribe(items -> {
            Set<T> present = Collections.newSetFromMap(new IdentityHashMap<>());
            present.addAll(items);
            cache.keySet().retainAll(present);
            host.getChildren().setAll(items.stream().map(item ->
                    cache.computeIfAbsent(item, key -> Objects.requireNonNull(render.apply(key), "rendered item"))
                            .getJavaFxNode()).toList());
        });
    }
}
