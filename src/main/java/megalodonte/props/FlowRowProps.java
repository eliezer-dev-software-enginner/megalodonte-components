package megalodonte.props;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import megalodonte.base.scale.ScaleProvider;
import megalodonte.base.theme.ThemeInterface;
import megalodonte.styles.util.StyleUtils;

public class FlowRowProps extends LayoutProps<FlowRowProps> {
    protected String bgColor;
    protected double vSpacing = -1;
    protected boolean fillWidth = false;

    public FlowRowProps bgColor(String bgColor) {
        this.bgColor = bgColor;
        return this;
    }

    /** Espaçamento vertical entre linhas quando o conteúdo quebra. */
    public FlowRowProps verticalSpacing(double spacing) {
        this.vSpacing = spacing;
        return this;
    }

    /**
     * Faz o FlowRow crescer horizontalmente dentro do Row/HBox pai,
     * ocupando o espaço disponível ao invés de ficar preso ao
     * prefWrapLength padrão do FlowPane (400px).
     */
    public FlowRowProps fillWidth() {
        this.fillWidth = true;
        return this;
    }

    @Override
    protected void applyTheme(Node node, Props props, ThemeInterface theme) {
        if (node instanceof FlowPane flowPane) {
            applyBaseLayout(node);

            flowPane.setMaxWidth(Double.MAX_VALUE); // <- garante que aceita a largura do pai

            // Faz o cálculo de wrap (e, portanto, de altura preferida) usar a largura
            // real do FlowPane em vez do prefWrapLength padrão (400px). Sem isso, quando
            // o pai é uma VBox (HBox.setHgrow abaixo não tem efeito nesse caso), o FlowPane
            // superestima quantas linhas precisa, inflando sua altura preferida.
            flowPane.prefWrapLengthProperty().bind(flowPane.widthProperty());

            if (fillWidth) {
                HBox.setHgrow(flowPane, Priority.ALWAYS);
            }

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