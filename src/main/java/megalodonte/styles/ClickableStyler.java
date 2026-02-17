package megalodonte.styles;

import javafx.scene.Node;
import megalodonte.props.ClickableProps;
import megalodonte.props.Props;
import megalodonte.theme.Theme;

public class ClickableStyler extends Estilizador<Props> {

    @Override
    public void apply(Node node, Props props) {
        if (!(node instanceof javafx.scene.layout.Region region) || !(props instanceof ClickableProps clickableProps)) {
            return;
        }

        // Adicionar efeitos de hover
        region.setOnMouseEntered(event -> {
            if (!region.isDisabled()) {
                String currentStyle = region.getStyle() != null ? region.getStyle() : "";
                String hoverStyle = currentStyle + 
                    String.format("-fx-background-color: %s;", clickableProps.getHoverColor());
                region.setStyle(hoverStyle);
            }
        });

        region.setOnMouseExited(event -> {
            String currentStyle = region.getStyle() != null ? region.getStyle() : "";
            String normalStyle = currentStyle
                .replace(String.format("-fx-background-color: %s;", clickableProps.getHoverColor()), "")
                .replace(String.format("-fx-background-color: %s;", clickableProps.getActiveColor()), "");
            
            // Restaurar cor de fundo original
            String finalStyle = normalStyle + 
                String.format("-fx-background-color: %s;", clickableProps.getBackgroundColor());
            region.setStyle(finalStyle);
        });

        // Adicionar efeito de pressão contínua
        region.setOnMousePressed(event -> {
            if (!region.isDisabled()) {
                String currentStyle = region.getStyle() != null ? region.getStyle() : "";
                String activeStyle = currentStyle + 
                    String.format("-fx-background-color: %s;", clickableProps.getActiveColor());
                region.setStyle(activeStyle);
            }
        });
    }

    @Override
    protected void applyTheme(Node node, Props props, Theme theme) {}
}