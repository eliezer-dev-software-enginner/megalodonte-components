package megalodonte.props;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import megalodonte.theme.Theme;

import java.util.Objects;

import static megalodonte.styles.util.StyleUtils.applyBackgroundStyling;

public class ContainerProps extends LayoutProps<ContainerProps> {

    protected String bgColor;
    protected String bgImage;

    // Fluent API methods
    @SuppressWarnings("unchecked")
    public ContainerProps bgColor(String bgColor) {
        Objects.requireNonNull(bgColor);
        this.bgColor = bgColor;
        return this;
    }

    /**
     * Has priority over bgColor
     * @param url
     * @return
     */
    public ContainerProps bgImage(String url) {
        Objects.requireNonNull(url);
        this.bgImage = url;
        return this;
    }

    @Override
    protected void applyTheme(Node node, Props props, Theme theme) {
        if (node instanceof Pane node_) {
            applyBaseLayout(node);

            byte type = (byte) (bgImage != null? 1 : 0);
            // Apply all common theme-aware styling
            applyBackgroundStyling(node, theme,  bgImage != null? bgImage : bgColor, type);
        }
    }
}
