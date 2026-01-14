package megalodonte.styles;

import javafx.scene.Node;
import javafx.scene.control.DatePicker;
import megalodonte.props.DatePickerProps;
import megalodonte.theme.Theme;
import megalodonte.utils.Utils;

public class DatePickerStyler extends BaseStyler<DatePickerProps, DatePickerStyler> {

    @Override
    protected void applyTheme(Node node, DatePickerProps props, Theme theme) {
        if (!(node instanceof DatePicker datePicker)) return;

        // Apply background styling
        applyBackgroundStyling(datePicker, theme);

        // Apply border styling
        applyBorderStyling(datePicker, theme);

        // Apply text styling for the date picker
        applyDateInputTextStyling(datePicker, theme);
    }

    /**
     * Applies text styling specific to DatePicker input field.
     */
    private void applyDateInputTextStyling(DatePicker datePicker, Theme theme) {
        String finalTextColor = textColor != null && !textColor.isBlank() ? 
            textColor : theme.colors().textPrimary();
        
        Utils.updateTextColor_Input(datePicker.getEditor(), finalTextColor);
        
        String finalPlaceholderColor = placeholderColor != null && !placeholderColor.isBlank() ? 
            placeholderColor : theme.colors().textSecondary();
        
        Utils.updatePlaceholderColor(datePicker.getEditor(), finalPlaceholderColor);
        
        // Set font size using theme's body variant
        int fontSize = theme.typography().body();
        Utils.updateFontSize(datePicker.getEditor(), fontSize);
        
        // Set placeholder font size to match
        var currentStyle = datePicker.getEditor().getStyle();
        var updatedStyle = Utils.UpdateEspecificStyle(currentStyle, "-fx-prompt-font-size", fontSize + "px");
        datePicker.getEditor().setStyle(updatedStyle);
    }
}