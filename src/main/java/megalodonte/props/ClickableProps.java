package megalodonte.props;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.layout.Region;

public class ClickableProps extends Props {

    private int padding = 8;
    private int borderRadius = 8;
    private String backgroundColor = "transparent";
    private String hoverColor = "rgba(0,0,0,0.05)";
    private String activeColor = "rgba(0,0,0,0.1)";
    private int height;
    private int width;

    public ClickableProps padding(int value) {
        this.padding = value;
        return this;
    }

    public ClickableProps borderRadius(int value) {
        this.borderRadius = value;
        return this;
    }

    public ClickableProps backgroundColor(String color) {
        this.backgroundColor = color;
        return this;
    }

    public ClickableProps hoverColor(String color) {
        this.hoverColor = color;
        return this;
    }

    public ClickableProps activeColor(String color) {
        this.activeColor = color;
        return this;
    }

    public ClickableProps height(int height) {
        this.height = height;
        return this;
    }

    public ClickableProps width(int width) {
        this.width = width;
        return this;
    }

    public int getPadding() {
        return padding;
    }

    public int getBorderRadius() {
        return borderRadius;
    }

    public String getBackgroundColor() {
        return backgroundColor;
    }

    public String getHoverColor() {
        return hoverColor;
    }

    public String getActiveColor() {
        return activeColor;
    }

    @Override
    public void apply(Node node) {
        if (!(node instanceof Region r)) return;

        r.setPadding(new Insets(padding));

        // Aplicar dimensões se definidas
        if (height > 0) {
            r.setPrefHeight(height);
            r.setMinHeight(height);
            r.setMaxHeight(height);
        }

        if (width > 0) {
            r.setPrefWidth(width);
            r.setMinWidth(width);
            r.setMaxWidth(width);
        }

        // Aplicar estilo base
        String baseStyle = String.format(
            "-fx-background-radius: %dpx; " +
            "-fx-background-color: %s; " +
            "-fx-cursor: hand;",
            borderRadius, backgroundColor
        );

        r.setStyle(baseStyle);
    }
}