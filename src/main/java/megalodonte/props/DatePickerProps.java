package megalodonte.props;

import javafx.scene.Node;
import javafx.scene.control.DatePicker;
import megalodonte.ReadableState;
import megalodonte.State;
import megalodonte.utils.Utils;
import megalodonte.utils.related.TextVariant;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

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



    @Override
    public void apply(Node node) {
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
    }
}