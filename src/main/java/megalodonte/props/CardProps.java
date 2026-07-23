package megalodonte.props;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import megalodonte.base.scale.ScaleProvider;
import megalodonte.base.theme.ThemeInterface;
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
    private boolean fillWidth = false;
    private boolean fillHeight = false;

    public CardProps fillHeight() {
        this.fillHeight = true;
        return this;
    }

    public boolean hasFillHeight() {
        return fillHeight;
    }

    public CardProps fillWidth() {
        this.fillWidth = true;
        return this;
    }

    public boolean hasFillWidth() {
        return fillWidth;
    }

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
    protected void applyTheme(Node node, Props props, ThemeInterface theme) {
        if (!(node instanceof Region r)) return;

        r.setPadding(new Insets(ScaleProvider.scale(padding)));

        if(height > 0){
            double scaled = ScaleProvider.scale(height);
            r.setPrefHeight(scaled);
            r.setMinHeight(scaled);
            r.setMaxHeight(scaled);
        }
        // width fixo só é aplicado se fillWidth NÃO estiver ativo
        if (width > 0 && !fillWidth) {
            double scaled = ScaleProvider.scale(width);
            r.setPrefWidth(scaled);
            r.setMinWidth(scaled);
            r.setMaxWidth(scaled);
        }

        int finalRadius = borderRadius > 0 ? ScaleProvider.scale(borderRadius) : theme.border().radiusMd();

        // Neutraliza qualquer borda padrão do JavaFX no estilo inline
        r.setStyle(
                "-fx-background-radius: " + finalRadius + ";" +
                        "-fx-background-color: white;" +
                        "-fx-border-color: transparent;" +  // ← isso elimina a borda do hover
                        "-fx-border-width: 0;"
        );

        applyBackgroundStyling(node, theme, bgColor);
        applyBorderStyling(node, theme);

        // Hover via hoverProperty
        node.setOnMouseEntered(null);
        node.setOnMouseExited(null);
        r.hoverProperty().addListener((obs, wasHover, isHover) ->
                node.setEffect(isHover ? hover : normal)
        );
        node.setEffect(normal);
    }

    /**
     * Applies common border styling.
     */
    protected void applyBorderStyling(Node node, ThemeInterface theme) {
        if (borderWidth == 0) {
            return;
        }

        String finalBorderColor = getFinalBorderColor(theme, borderColor);
        Utils.updateBorderColor(node, finalBorderColor);

        int finalBorderWidth = getFinalBorderWidth(theme, ScaleProvider.scale(borderWidth));
        if (finalBorderWidth > 0) {
            Utils.updateBorderWidth(node, finalBorderWidth);
        }

        Utils.updateBorderRadius(node, 0);
    }

    public boolean hasFixedWidth() { return width > 0 && !fillWidth; }
    public boolean hasFixedHeight() { return height > 0; }
}