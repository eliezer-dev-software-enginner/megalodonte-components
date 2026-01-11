package megalodonte.props;

import javafx.geometry.Insets;
import javafx.scene.Node;

public class CommonProps extends Props {

    private Insets padding;
    private Double opacity;
    private Boolean visible;
    private String style;

    public CommonProps padding(double value) {
        this.padding = new Insets(value);
        return this;
    }

    public CommonProps opacity(double opacity) {
        this.opacity = opacity;
        return this;
    }

    public CommonProps visible(boolean visible) {
        this.visible = visible;
        return this;
    }

    public CommonProps style(String style) {
        this.style = style;
        return this;
    }

    @Override
    public void apply(Node node) {

        if (padding != null && node instanceof javafx.scene.layout.Region r) {
            r.setPadding(padding);
        }

        if (opacity != null) {
            node.setOpacity(opacity);
        }

        if (visible != null) {
            node.setVisible(visible);
        }

        if (style != null) {
            node.setStyle(style);
        }
    }
}
