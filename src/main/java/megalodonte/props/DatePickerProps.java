package megalodonte.props;

import javafx.scene.Node;
import javafx.scene.control.DatePicker;
import megalodonte.ReadableState;
import megalodonte.State;
import megalodonte.theme.Theme;
import megalodonte.utils.Utils;
import megalodonte.utils.related.TextVariant;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import static megalodonte.styles.util.StyleUtils.*;

public class DatePickerProps extends Props {
    private DateTimeFormatter formatter;
    private Locale locale;
    private String promptText;
    private Boolean editable;
    private ReadableState<LocalDate> valueState;
    private String color;

    public DatePickerProps formatter(DateTimeFormatter formatter) {
        this.formatter = formatter;
        return this;
    }
    
    public DatePickerProps locale(Locale locale) {
        this.locale = locale;
        return this;
    }
    
    public DatePickerProps placeHolder(String placeHolder) {
        this.promptText = placeHolder;
        return this;
    }
    
    public DatePickerProps editable(Boolean editable) {
        this.editable = editable;
        return this;
    }
    
    public DatePickerProps editable(boolean editable) {
        this.editable = editable;
        return this;
    }
    
    public DatePickerProps value(State<LocalDate> state) {
        this.valueState = state;
        return this;
    }
    
    public DatePickerProps pattern(String pattern) {
        this.formatter = DateTimeFormatter.ofPattern(pattern, locale != null ? locale : Locale.getDefault());
        return this;
    }
    
    public DatePickerProps pattern(String pattern, Locale locale) {
        this.formatter = DateTimeFormatter.ofPattern(pattern, locale);
        this.locale = locale;
        return this;
    }

    private Integer fontSize = 14;
    public DatePickerProps fontSize(int fontSize){
        this.fontSize = fontSize;
        return this;
    }

    private TextTone tone = TextTone.PRIMARY;

    public DatePickerProps tone(TextTone tone) {
        this.tone = tone;
        return this;
    }

    private TextVariant variant = TextVariant.BODY;

    public DatePickerProps variant(TextVariant variant) {
        this.variant = variant;
        return this;
    }

    public TextVariant getVariant() {
        return variant;
    }

    public TextTone getTone() {
        return tone;
    }

    private int height;
    private int width;

    public DatePickerProps height(int height){
        this.height = height;
        return this;
    }
    public DatePickerProps width(int width){
        this.width = width;
        return this;
    }

    public DatePickerProps color(String color){
        this.color = color;
        return this;
    }

    protected String textColor;
    protected String placeholderColor;

    // Common styling properties
    protected String bgColor;
    protected String borderColor;
    protected int borderWidth;
    protected int borderRadius;

    // Fluent API methods
    @SuppressWarnings("unchecked")
    public DatePickerProps  bgColor(String bgColor) {
        this.bgColor = bgColor;
        return this;
    }

    @SuppressWarnings("unchecked")
    public DatePickerProps borderColor(String borderColor) {
        this.borderColor = borderColor;
        return this;
    }

    @SuppressWarnings("unchecked")
    public DatePickerProps borderWidth(int borderWidth) {
        this.borderWidth = borderWidth;
        return this;
    }

    @SuppressWarnings("unchecked")
    public DatePickerProps borderRadius(int borderRadius) {
        this.borderRadius = borderRadius;
        return this;
    }

    @Override
    protected void applyTheme(Node node, Props props, Theme theme) {
        if (!(node instanceof DatePicker datePicker)) return;

        if (formatter != null) {
            datePicker.setConverter(new javafx.util.StringConverter<LocalDate>() {
                @Override
                public String toString(LocalDate date) {
                    return date != null ? formatter.format(date) : "";
                }

                @Override
                public LocalDate fromString(String string) {
                    return (string != null && !string.isBlank())
                            ? LocalDate.parse(string, formatter)
                            : null;
                }
            });
        }

        if (promptText != null) {
            datePicker.setPromptText(promptText);
        }

        if (editable != null) {
            datePicker.setEditable(editable);
        }

        if (valueState != null) {
            valueState.subscribe(date -> {
                if (date != null) {
                    datePicker.setValue(date);
                }
            });

            datePicker.valueProperty().addListener((obs, oldVal, newVal) -> {
                if (newVal != null && valueState instanceof State) {
                    ((State<LocalDate>) valueState).set(newVal);
                }
            });
        }

        if (fontSize != null) {
            Utils.updateFontSize(datePicker, fontSize);
        }

        if (color != null) {
            Utils.updateTextColor_Input(datePicker, color);
        }


        if(height > 0){
            datePicker.setPrefHeight(height);
            datePicker.setMinHeight(height);
            datePicker.setMaxHeight(height);
        }

        if(width > 0){
            datePicker.setPrefWidth(width);
            datePicker.setMinWidth(width);
            datePicker.setMaxWidth(width);
        }

        // Apply background styling
        applyBackgroundStyling(datePicker, theme, bgColor);

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

    /**
     * Applies common border styling.
     */
    protected void applyBorderStyling(Node node, Theme theme) {
        String finalBorderColor = getFinalBorderColor(theme, borderColor);
        Utils.updateBorderColor(node, finalBorderColor);

        int finalBorderWidth = getFinalBorderWidth(theme, borderWidth);
        if (finalBorderWidth > 0) {
            Utils.updateBorderWidth(node, finalBorderWidth);
        }

//        int finalBorderRadius = getFinalBorderRadius(theme);
//        if (finalBorderRadius > 0) {
//            Utils.updateBorderRadius(node, finalBorderRadius);
//        }

        Utils.updateBorderRadius(node, 0);
    }
}