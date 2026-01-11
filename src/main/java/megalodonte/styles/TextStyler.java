package megalodonte.styles;

import javafx.scene.Node;
import megalodonte.props.TextProps;
import megalodonte.theme.Theme;

public class TextStyler extends BaseStyler<TextProps, TextStyler> {

    public TextStyler color(String color){
        this.textColor = color;
        return this;
    }

    @Override
    protected void applyTheme(Node node, TextProps props, Theme theme) {
        if (!(node instanceof javafx.scene.text.Text textNode)) return;
        
        // Apply text-specific styling
        applyTextStyling(textNode, theme, props);
    }
}
