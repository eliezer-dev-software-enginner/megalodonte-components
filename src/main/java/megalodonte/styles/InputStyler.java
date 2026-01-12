package megalodonte.styles;

import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import megalodonte.props.InputProps;
import megalodonte.theme.Theme;
import megalodonte.utils.Utils;

public class InputStyler extends BaseStyler<InputProps, InputStyler> {

    @Override
    public InputStyler borderRadius(int borderRadius) {
        // Ignore border radius setting for inputs - use theme default only
        return this;
    }

    @Override
    protected void applyTheme(Node node, InputProps props, Theme theme) {
        if (!(node instanceof StackPane stackPane)) return;

        var input = stackPane.getChildren().get(0);
        if(input == null) return;

        // Apply background styling to both stackpane and input
        String finalBgColor = getFinalBackgroundColor(theme);
        Utils.updateBackgroundColor(stackPane, finalBgColor);
        Utils.updateBackgroundColor(input, finalBgColor);

        // Apply border styling without custom radius
        applyInputBorderStyling(stackPane, theme);

        // Apply text styling to input
        applyInputTextStyling(input, theme, props);
    }

    /**
     * Apply border styling for inputs without custom radius, using only theme defaults.
     */
    private void applyInputBorderStyling(StackPane stackPane, Theme theme) {
        String finalBorderColor = getFinalBorderColor(theme);
        Utils.updateBorderColor(stackPane, finalBorderColor);
        
        int finalBorderWidth = theme.border().width();
        if (finalBorderWidth > 0) {
            Utils.updateBorderWidth(stackPane, finalBorderWidth);
        }
        
//        // Use only theme radius, ignore custom borderRadius setting
//        int themeBorderRadius = theme.radius().md();
//        if (themeBorderRadius > 0) {
//            Utils.updateBorderRadius(stackPane, themeBorderRadius);
//        }

        Utils.updateBorderRadius(stackPane, 0);
    }

}
