package megalodonte.props;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import megalodonte.theme.Theme;

import java.util.Objects;

import static megalodonte.styles.util.StyleUtils.applyBackgroundStyling;

public class ContainerProps extends LayoutProps<ContainerProps> {

    protected String bgColor;
    protected String bgImage;

    private boolean fillHeight = false;

    public ContainerProps fillHeight() {
        this.fillHeight = true;
        return this;
    }

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

            if (fillHeight) {
                VBox.setVgrow(node, Priority.ALWAYS);
            }

            if (bgImage != null) {
                var url = getClass().getResource(bgImage);
                if (url != null) {
                    var image = new javafx.scene.image.Image(url.toExternalForm());
                    var bgImg = new javafx.scene.layout.BackgroundImage(
                            image,
                            javafx.scene.layout.BackgroundRepeat.NO_REPEAT,
                            javafx.scene.layout.BackgroundRepeat.NO_REPEAT,
                            javafx.scene.layout.BackgroundPosition.CENTER,
                            new javafx.scene.layout.BackgroundSize(
                                    1.0, 1.0,
                                    true, true,
                                    false, true  // cover
                            )
                    );
                    node_.setBackground(new javafx.scene.layout.Background(bgImg));
                }
            } else if (bgColor != null) {
                applyBackgroundStyling(node, theme, bgColor);
            }
        }
    }

//    @Override
//    protected void applyTheme(Node node, Props props, Theme theme) {
//        if (node instanceof Pane node_) {
//            applyBaseLayout(node);
//
//            if (fillHeight) {
//                VBox.setVgrow(node, Priority.ALWAYS);
//            }
//
//            byte type = (byte) (bgImage != null? 1 : 0);
//            // Apply all common theme-aware styling
//            applyBackgroundStyling(node, theme,  bgImage != null? bgImage : bgColor, type);
//        }
//    }
}
