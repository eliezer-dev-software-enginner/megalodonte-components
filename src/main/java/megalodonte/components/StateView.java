package megalodonte.components;

import javafx.scene.layout.Priority;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import megalodonte.base.components.Component;
import megalodonte.base.state.ReadableState;

import java.util.Objects;
import java.util.function.Function;

/** Renders a component from state, replacing it whenever the value changes. */
public final class StateView<T> extends Component {
    public StateView(ReadableState<T> state, Function<T, ? extends Component> render) {
        super(new VBox());
        Objects.requireNonNull(state, "state");
        Objects.requireNonNull(render, "render");
        VBox host = (VBox) node;
        host.setMinWidth(0);
        host.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(host, Priority.ALWAYS);
        host.setMaxHeight(Double.MAX_VALUE);
        VBox.setVgrow(host, Priority.ALWAYS);
        state.subscribe(value -> {
            Component content = Objects.requireNonNull(render.apply(value), "rendered component");
            host.getChildren().setAll(content.getJavaFxNode());
            VBox.setVgrow(content.getJavaFxNode(), Priority.ALWAYS);
        });
    }
}
