package megalodonte.styles;

import javafx.scene.Node;
import megalodonte.props.Props;
import megalodonte.theme.Theme;
import megalodonte.theme.ThemeManager;

public abstract class Estilizador<P extends Props> {
    public void apply(Node node, P props){
        ThemeManager.state().subscribe(theme -> {
            if (theme == null) return;
            applyTheme(node, props, theme);
        });
    }
    protected abstract void applyTheme(Node node, P props, Theme theme);
}
