package megalodonte.styles;

import javafx.scene.Node;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import megalodonte.props.CardProps;
import megalodonte.theme.Theme;
import megalodonte.utils.Utils;

import static megalodonte.styles.util.StyleUtils.*;

public class CardStyler extends BaseStyler<CardProps, CardStyler> {

    private final DropShadow normal =
            new DropShadow(8, Color.rgb(0, 0, 0, 0.15));

    private final DropShadow hover =
            new DropShadow(16, Color.rgb(0, 0, 0, 0.25));

    protected String bgColor;
    protected String borderColor;
    protected int borderWidth;

    @Override
    protected void applyTheme(Node node, CardProps props, Theme theme) {
        // Apply background and border styling
        applyBackgroundStyling(node, theme, bgColor);
        applyBorderStyling(node, theme);

        // Apply card-specific effects
        node.setEffect(normal);
        node.setOnMouseEntered(e -> node.setEffect(hover));
        node.setOnMouseExited(e -> node.setEffect(normal));
    }

    /**
     * Applies common border styling.
     */
    protected void applyBorderStyling(Node node, Theme theme) {
        String finalBorderColor = getFinalBorderColor(theme, borderColor);
        Utils.updateBorderColor(node, finalBorderColor);

        int finalBorderWidth = getFinalBorderWidth(theme, borderWidth);
        if (finalBorderWidth > 0) {
            Utils.updateBorderWidth(node, finalBorderWidth);
        }

//        int finalBorderRadius = getFinalBorderRadius(theme);
//        if (finalBorderRadius > 0) {
//            Utils.updateBorderRadius(node, finalBorderRadius);
//        }

        Utils.updateBorderRadius(node, 0);
    }

}
