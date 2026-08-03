package megalodonte.props;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import megalodonte.base.scale.ScaleProvider;
import megalodonte.base.theme.ThemeInterface;

import static megalodonte.styles.util.StyleUtils.*;

public class SelectProps extends TextComponentProps<SelectProps> {
    private double minWidth;
    private double maxWidth;
    private double maxHeight;
    private int paddingUnitsDown;
    private int paddingUnitsTop;
    private int paddingUnitsLeft;
    private int paddingUnitsRight;

    private int height;

    private TextTone tone = TextTone.PRIMARY;

    public SelectProps tone(TextTone tone) {
        this.tone = tone;
        return this;
    }

    public TextTone getTone() {
        return tone;
    }

    boolean disabled;

    public SelectProps disable() {
        this.disabled = true;
        return this;
    }

    protected String bgColor;
    protected String borderColor;
    protected int borderWidth;
    protected int borderRadius;

    public SelectProps bgColor(String bgColor) {
        this.bgColor = bgColor;
        return this;
    }

    public SelectProps borderColor(String borderColor) {
        this.borderColor = borderColor;
        return this;
    }

    public SelectProps borderWidth(int borderWidth) {
        this.borderWidth = borderWidth;
        return this;
    }

    public SelectProps borderRadius(int borderRadius) {
        this.borderRadius = borderRadius;
        return this;
    }

    public SelectProps height(int height){
        this.height = height;
        return this;
    }

    public SelectProps maxWidth(double maxWidth){
        this.maxWidth = maxWidth;
        return this;
    }

    public SelectProps minWidth(double minWidth){
        this.minWidth = minWidth;
        return this;
    }

    public SelectProps paddingAll(int units){
       this.paddingUnitsTop = units;
       this.paddingUnitsRight = units;
       this.paddingUnitsDown = units;
       this.paddingUnitsLeft = units;
        return this;
    }

    public SelectProps paddingTop(int units){
        this.paddingUnitsTop = units;
        return this;
    }

    public SelectProps paddingRight(int units){
        this.paddingUnitsRight = units;
        return this;
    }

    public SelectProps paddingDown(int units){
        this.paddingUnitsDown = units;
        return this;
    }

    public SelectProps paddingLeft(int units){
        this.paddingUnitsLeft = units;
        return this;
    }

    @Override
    protected void applyTheme(Node node, Props props, ThemeInterface theme) {
        if (!(node instanceof ComboBox<?> cBox)) return;

        if (minWidth > 0) {
            cBox.setMinWidth(ScaleProvider.scale(minWidth));
        }
        if (maxWidth > 0) {
            cBox.setMaxWidth(ScaleProvider.scale(maxWidth));
        }
        if (maxHeight > 0) {
            cBox.setMaxHeight(ScaleProvider.scale(maxHeight));
        }

        if (height > 0) {
            double scaled = ScaleProvider.scale(height);
            cBox.setPrefHeight(scaled);
            cBox.setMinHeight(scaled);
            cBox.setMaxHeight(scaled);
        }

        cBox.setPadding(new Insets(
                ScaleProvider.scale(paddingUnitsTop),
                ScaleProvider.scale(paddingUnitsRight),
                ScaleProvider.scale(paddingUnitsDown),
                ScaleProvider.scale(paddingUnitsLeft)
        ));

        if (disabled) {
            cBox.setDisable(true);
        }

        if (getFontSize() != null) {
            updateFontSize(cBox, ScaleProvider.scale(getFontSize()));
        } else {
            int fontSize = theme.typography().small();
            updateFontSize(cBox, fontSize);
            updatePlaceholderFontSize(cBox.getEditor(), fontSize);
        }

        // Background
        String finalBgColor = getFinalBackgroundColor(theme, bgColor);
        updateBackgroundColor(cBox, finalBgColor);

        // Border
        String finalBorderColor = getFinalBorderColor(theme, borderColor);
        int finalBorderWidth = getFinalBorderWidth(theme, ScaleProvider.scale(borderWidth));
        int finalBorderRadius = getFinalBorderRadius(theme, ScaleProvider.scale(borderRadius));

        updateBorderColor(cBox, finalBorderColor);
        updateBorderWidth(cBox, finalBorderWidth);
        updateBorderRadius(cBox, finalBorderRadius);

        // Modena renderiza ComboBox com várias camadas de background/border
        // empilhadas (cada uma com seu próprio inset), mesmo problema documentado
        // em InputProps.applyInputBorderStyling/DatePickerProps — sem zerar os
        // insets, a camada padrão do Modena continua visível por baixo da nossa
        // borda, aparentando uma segunda borda escura por dentro da nossa.
        applyStyleProperty(cBox, "0", "-fx-background-insets");
        applyStyleProperty(cBox, "0", "-fx-border-insets");

        // Text color via theme tone or inline
        String finalTextColor = getFinalSelectTextColor(theme);
        if (textColor != null && !textColor.isBlank()) {
            finalTextColor = textColor;
        }
        updateTextColor_Input(cBox, finalTextColor);

        // Placeholder color from theme
        updatePlaceholderColor(cBox, theme.colors().placeholder());

        // O popup (lista de opções) do ComboBox não dá pra estilizar por lookup
        // (roda numa Scene separada, não é descendente de cBox) — e mesmo que desse,
        // Select.displayText() é sempre chamado depois disso na prática e sobrescreve
        // o cellFactory inteiro. Por isso quem estiliza as células é o próprio Select
        // (default cellFactory + displayText compartilham o mesmo helper themado).
    }

    private String getFinalSelectTextColor(ThemeInterface theme) {
        return switch (tone) {
            case PRIMARY -> theme.colors().textPrimary();
            case SECONDARY, DISABLED -> theme.colors().textSecondary();
            case INVERTED -> theme.colors().background();
        };
    }
}
