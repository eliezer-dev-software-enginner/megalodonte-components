package megalodonte.styles;

import javafx.scene.Node;
import megalodonte.props.InputProps;
import megalodonte.styles.Estilizador;
import megalodonte.props.Props;
import megalodonte.theme.Theme;
import megalodonte.theme.ThemeManager;
import megalodonte.utils.Utils;

/**
 * Base class for all stylers that provides common functionality for theme-aware styling.
 * Eliminates code duplication and ensures consistent theme usage across all stylers.
 */
public abstract class BaseStyler<T extends Props, S extends BaseStyler<T, S>> extends Estilizador<T> {
    
    // Common styling properties
    protected String bgColor;
    protected String borderColor;
    protected int borderWidth;
    protected int borderRadius;
    
    // Fluent API methods
    @SuppressWarnings("unchecked")
    public S bgColor(String bgColor) {
        this.bgColor = bgColor;
        return (S) this;
    }
    
    @SuppressWarnings("unchecked")
    public S borderColor(String borderColor) {
        this.borderColor = borderColor;
        return (S) this;
    }
    
    @SuppressWarnings("unchecked")
    public S borderWidth(int borderWidth) {
        this.borderWidth = borderWidth;
        return (S) this;
    }
    
    @SuppressWarnings("unchecked")
    public S borderRadius(int borderRadius) {
        this.borderRadius = borderRadius;
        return (S) this;
    }

    /**
     * Template method that handles theme subscription and calls applyTheme.
     * Subclasses should implement applyTheme for their specific styling logic.
     */
    @Override
    public final void apply(Node node, T props) {
        ThemeManager.state().subscribe(theme -> {
            if (theme == null) return;
            applyTheme(node, props, theme);
        });
    }
    
    /**
     * Subclasses must implement this method for their specific styling logic.
     * All theme-related styling should be done here.
     */
    protected abstract void applyTheme(Node node, T props, Theme theme);
    
    // Helper methods for consistent theme usage
    


    

}