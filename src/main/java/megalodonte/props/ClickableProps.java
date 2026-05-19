package megalodonte.props;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.layout.Region;
import megalodonte.styles.util.Utils;
import megalodonte.theme.Theme;

public class ClickableProps extends Props {

    private String backgroundColor = "transparent";
    private String hoverColor = "rgba(0,0,0,0.05)";

    @Override
    protected void applyTheme(Node node, Props props, Theme theme) {
        Utils.UpdateCursor(node,"hand");

        if (!(node instanceof javafx.scene.layout.Region region) || !(props instanceof ClickableProps clickableProps)) {
            return;
        }

        // Adicionar efeitos de hover
        region.setOnMouseEntered(event -> {

        });

        // Adicionar efeito de pressão contínua
        region.setOnMousePressed(event -> {
            if (!region.isDisabled()) {
                String currentStyle = region.getStyle() != null ? region.getStyle() : "";
                String activeStyle = currentStyle +
                        String.format("-fx-background-color: %s;", hoverColor);
                region.setStyle(activeStyle);
            }
        });
    }
}