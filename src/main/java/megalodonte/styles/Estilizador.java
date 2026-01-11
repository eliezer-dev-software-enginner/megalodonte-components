package megalodonte.styles;

import javafx.scene.Node;
import megalodonte.props.Props;

public abstract class Estilizador<P extends Props> {
    public abstract void apply(Node node, P props);
}
