package megalodonte.styles;

import javafx.scene.Node;
import megalodonte.props.TextProps;
import megalodonte.theme.Theme;

public class TextStyler extends TextComponentStyler<TextProps, TextStyler> {

    @Override
    protected void applyTheme(Node node, TextProps props, Theme theme) {
        if (!(node instanceof javafx.scene.text.Text textNode)) return;
        
        applyTextStyling(textNode, theme, props);
    }
}
