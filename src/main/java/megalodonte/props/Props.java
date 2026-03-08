package megalodonte.props;

import javafx.geometry.Insets;
import javafx.scene.Node;
import megalodonte.theme.Theme;
import megalodonte.theme.ThemeManager;

public abstract class Props {
    //public abstract void apply(Node node);

    /**
     * Template method that handles theme subscription and calls applyTheme.
     * Subclasses should implement applyTheme for their specific styling logic.
     */
    final public void apply(Node node){
        ThemeManager.state().subscribe(theme -> {
            if (theme == null) return;
            applyTheme(node, this, theme);
        });
    }

    protected abstract void applyTheme(Node node, Props props, Theme theme);
}
