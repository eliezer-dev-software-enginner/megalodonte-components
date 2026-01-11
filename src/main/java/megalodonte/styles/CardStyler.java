package megalodonte.styles;

import javafx.scene.Node;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import megalodonte.props.CardProps;
import megalodonte.theme.Theme;

public class CardStyler extends BaseStyler<CardProps, CardStyler> {

    private final DropShadow normal =
            new DropShadow(8, Color.rgb(0, 0, 0, 0.15));

    private final DropShadow hover =
            new DropShadow(16, Color.rgb(0, 0, 0, 0.25));

    @Override
    protected void applyTheme(Node node, CardProps props, Theme theme) {
        // Apply background and border styling
        applyBackgroundStyling(node, theme);
        applyBorderStyling(node, theme);

        // Apply card-specific effects
        node.setEffect(normal);
        node.setOnMouseEntered(e -> node.setEffect(hover));
        node.setOnMouseExited(e -> node.setEffect(normal));
    }
}
