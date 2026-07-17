package megalodonte.props;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.FlowPane;
import megalodonte.base.scale.ScaleProvider;
import megalodonte.base.theme.ThemeInterface;
import megalodonte.styles.util.StyleUtils;

public class FlowRowProps extends LayoutProps<FlowRowProps> {
    protected String bgColor;
    protected double vSpacing = -1;

    public FlowRowProps bgColor(String bgColor) {
        this.bgColor = bgColor;
        return this;
    }

    /** Espaçamento vertical entre linhas quando o conteúdo quebra. */
    public FlowRowProps verticalSpacing(double spacing) {
        this.vSpacing = spacing;
        return this;
    }

    @Override
    protected void applyTheme(Node node, Props props, ThemeInterface theme) {
        if (node instanceof FlowPane flowPane) {
            applyBaseLayout(node);

            flowPane.setMaxWidth(Double.MAX_VALUE); // <- garante que aceita a largura do pai


            if (spacingUnits > 0) {
                flowPane.setHgap(ScaleProvider.scale(spacingUnits));
            }
            if (vSpacing >= 0) {
                flowPane.setVgap(ScaleProvider.scale(vSpacing));
            } else if (spacingUnits > 0) {
                flowPane.setVgap(ScaleProvider.scale(spacingUnits)); // usa o mesmo valor por padrão
            }

            if (alignment == Alignment.CENTER_HORIZONTALLY) {
                flowPane.setAlignment(Pos.CENTER);
            }

            if (bgColor != null) {
                StyleUtils.applyBackgroundStyling(node, theme, bgColor);
            }
        }
    }

    private enum Alignment {CENTER_HORIZONTALLY}
    private Alignment alignment;

    public FlowRowProps centerHorizontally() {
        alignment = Alignment.CENTER_HORIZONTALLY;
        return this;
    }
}