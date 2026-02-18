package megalodonte.props;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import megalodonte.theme.Theme;
import megalodonte.utils.Utils;

import static megalodonte.styles.util.StyleUtils.*;

public class CardProps extends Props {

    private final DropShadow normal =
            new DropShadow(8, Color.rgb(0, 0, 0, 0.15));

    private final DropShadow hover =
            new DropShadow(16, Color.rgb(0, 0, 0, 0.25));

    protected String bgColor;
    protected String borderColor;
    protected int borderWidth;
    protected int borderRadius;

    private int padding = 12;

    private int height;
    private int width;

    public CardProps height(int height){
        this.height = height;
        return this;
    }

    public CardProps width(int width){
        this.width = width;
        return this;
    }

    public CardProps width(double width){
        this.width = (int) width;
        return this;
    }

    // Fluent API methods
    public CardProps bgColor(String bgColor) {
        this.bgColor = bgColor;
        return  this;
    }

    public CardProps borderColor(String borderColor) {
        this.borderColor = borderColor;
        return this;
    }

    public CardProps borderWidth(int borderWidth) {
        this.borderWidth = borderWidth;
        return this;
    }

    public CardProps borderRadius(int borderRadius) {
        this.borderRadius = borderRadius;
        return this;
    }


    public CardProps padding(int value) {
        this.padding = value;
        return this;
    }

    @Override
    protected void applyTheme(Node node, Props props, Theme theme) {
        if (!(node instanceof Region r)) return;

        r.setPadding(new Insets(padding));

        if(height > 0){
            r.setPrefHeight(height);
            r.setMinHeight(height);
            r.setMaxHeight(height);
        }
        if(width > 0){
            r.setPrefWidth(width);
            r.setMinWidth(width);
            r.setMaxWidth(width);
        }
        int finalRadius = borderRadius > 0 ? borderRadius : theme.radius().md();
        r.setStyle("-fx-background-radius: " + finalRadius + ";" + "-fx-background-color: white;");

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
        if (borderWidth == 0) {
            return;
        }

        String finalBorderColor = getFinalBorderColor(theme, borderColor);
        Utils.updateBorderColor(node, finalBorderColor);

        int finalBorderWidth = getFinalBorderWidth(theme, borderWidth);
        if (finalBorderWidth > 0) {
            Utils.updateBorderWidth(node, finalBorderWidth);
        }

        Utils.updateBorderRadius(node, 0);
    }
}