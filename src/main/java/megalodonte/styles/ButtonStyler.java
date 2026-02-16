package megalodonte.styles;

import javafx.scene.Node;
import javafx.scene.control.Button;
import megalodonte.props.ButtonProps;
import megalodonte.theme.Theme;

public class ButtonStyler extends TextComponentStyler<ButtonProps, ButtonStyler> {

    @Override
    protected void applyTheme(Node node, ButtonProps props, Theme theme) {
        if (!(node instanceof Button button)) return;
        
        applyBackgroundStyling(button, theme);
        applyBorderStyling(button, theme);
    }
}
