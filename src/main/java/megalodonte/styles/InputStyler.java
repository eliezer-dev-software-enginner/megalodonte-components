package megalodonte.styles;

import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import megalodonte.InputProps;
import megalodonte.theme.Theme;
import megalodonte.utils.Utils;

public class InputStyler extends BaseStyler<InputProps, InputStyler> {

    @Override
    protected void applyTheme(Node node, InputProps props, Theme theme) {
        if (!(node instanceof StackPane stackPane)) return;

        var input = stackPane.getChildren().get(0);
        if(input == null) return;

        // Apply background styling to both stackpane and input
        String finalBgColor = getFinalBackgroundColor(theme);
        Utils.updateBackgroundColor(stackPane, finalBgColor);
        Utils.updateBackgroundColor(input, finalBgColor);

        // Apply border styling to stackpane
        applyBorderStyling(stackPane, theme);

        // Apply text styling to input
        applyInputTextStyling(input, theme, props);


    }
}
