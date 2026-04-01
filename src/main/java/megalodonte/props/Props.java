package megalodonte.props;

import javafx.scene.Node;
import megalodonte.ReadableState;
import megalodonte.base.components.PropsInterface;
import megalodonte.base.theme.ThemeInterface;
import megalodonte.theme.Theme;
import megalodonte.theme.ThemeManager;

import java.util.function.Consumer;

public abstract class Props implements PropsInterface {
    //public abstract void apply(Node node);

    final public void apply(Node node) {
        // bindings reativos — executados uma única vez
        bindStates(node);

        // tema — re-executa a cada mudança
        ThemeManager.state().subscribe(theme -> {
            if (theme == null) return;
            applyTheme(node, this, theme);
        });
    }

    /**
     * Sobrescreva para registrar bindings de State.
     * Chamado uma única vez, independente de trocas de tema.
     */
    protected void bindStates(Node node) {}

    protected <T> void bind(Node node, ReadableState<T> state, Consumer<T> handler) {
        if (state != null) state.subscribe(handler);
    }


    //implementado nos filhos e  chamado pelo apply
    @Override
    public void applyTheme(Node node, PropsInterface propsInterface, ThemeInterface themeInterface) {

    }


//
//    /**
//     * Template method that handles theme subscription and calls applyTheme.
//     * Subclasses should implement applyTheme for their specific styling logic.
//     */
//    final public void apply(Node node){
//        ThemeManager.state().subscribe(theme -> {
//            if (theme == null) return;
//            applyTheme(node, this, theme);
//        });
//    }

    protected abstract void applyTheme(Node node, Props props, Theme theme);
}
