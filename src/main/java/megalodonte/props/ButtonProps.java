package megalodonte.props;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import megalodonte.base.scale.ScaleProvider;
import megalodonte.base.state.ReadableState;
import megalodonte.base.theme.ThemeInterface;

import static megalodonte.props.ButtonVariant.DISABLED;
import static megalodonte.props.ButtonVariant.GHOST;
import static megalodonte.styles.util.StyleUtils.*;

public class ButtonProps extends TextComponentProps<ButtonProps> implements Paddable<ButtonProps> {
    private int height;
    private boolean fillWidth;
    protected String bgColor;
    protected String borderColor;
    protected int borderWidth;
    protected int borderRadius;
    protected boolean iconOnRight;

    protected int paddingUnitsTop = UNSET;
    protected int paddingUnitsRight = UNSET;
    protected int paddingUnitsDown = UNSET;
    protected int paddingUnitsLeft = UNSET;

    private ButtonStyle style = ButtonStyle.FILLED;
    private ButtonVariant variant = ButtonVariant.PRIMARY;

    //----------------States
    private ReadableState<String> bgColorState;
    private ReadableState<String> textColorState;


    // Fluent API methods — padding inherited from Paddable<ButtonProps>


    @Override
    public int getPaddingUnitsTop() { return paddingUnitsTop; }

    @Override
    public int getPaddingUnitsRight() { return paddingUnitsRight; }

    @Override
    public int getPaddingUnitsDown() { return paddingUnitsDown; }

    @Override
    public int getPaddingUnitsLeft() { return paddingUnitsLeft; }

    @Override
    public void setPaddingUnitsTop(int units) { this.paddingUnitsTop = units; }

    @Override
    public void setPaddingUnitsRight(int units) { this.paddingUnitsRight = units; }

    @Override
    public void setPaddingUnitsDown(int units) { this.paddingUnitsDown = units; }

    @Override
    public void setPaddingUnitsLeft(int units) { this.paddingUnitsLeft = units; }

    public ButtonProps bgColor(String bgColor) {
        this.bgColor = bgColor;
        return this;
    }

    public ButtonProps iconOnRight() {
        this.iconOnRight = true;
        return this;
    }

    public ButtonProps bgColor(ReadableState<String> bgColorState) {
        this.bgColorState = bgColorState;

        return this;
    }


    public ButtonProps borderColor(String borderColor) {
        this.borderColor = borderColor;
        return this;
    }

    public ButtonProps borderWidth(int borderWidth) {
        this.borderWidth = borderWidth;
        return this;
    }

    public ButtonProps borderRadius(int borderRadius) {
        this.borderRadius = borderRadius;
        return this;
    }

    public ButtonProps textColor(ReadableState<String> textColorState) {
        this.textColorState = textColorState;
        return this;
    }

    public ButtonProps fillWidth() {
        this.fillWidth = true;
        return this;
    }

    public ButtonProps height(int height) {
        this.height = height;
        return this;
    }


    public ButtonProps outlined() {
        this.style = ButtonStyle.OUTLINED;
        return this;
    }

    public ButtonProps text() {
        this.style = ButtonStyle.TEXT;
        return this;
    }

    public ButtonProps filled() {
        this.style = ButtonStyle.FILLED;
        return this;
    }

    public ButtonProps style(ButtonStyle style) {
        this.style = style;
        return this;
    }

    public ButtonStyle getStyle() {
        return style;
    }

    public ButtonProps variant(ButtonVariant variant) {
        this.variant = variant;
        return this;
    }

    /** @deprecated use {@link #variant(ButtonVariant)}. Mantido para compatibilidade. */
    @Deprecated(forRemoval = true)
    public ButtonProps variant(String variant) {
        this.variant = ButtonVariant.valueOf(variant.toUpperCase());
        return this;
    }

    public ButtonProps primary() {
        this.variant = ButtonVariant.PRIMARY;
        return this;
    }

    public ButtonProps secondary() {
        this.variant = ButtonVariant.SECONDARY;
        return this;
    }

    public ButtonProps success() {
        this.variant = ButtonVariant.SUCCESS;
        return this;
    }

    public ButtonProps warning() {
        this.variant = ButtonVariant.WARNING;
        return this;
    }

    public ButtonProps danger() {
        this.variant = ButtonVariant.DANGER;
        return this;
    }

    public ButtonProps ghost() {
        this.variant = GHOST;
        return this;
    }

    public ButtonProps disabled() {
        this.variant = DISABLED;
        return this;
    }

    public ButtonVariant getVariant() {
        return variant;
    }

    private String getButtonColorFromVariant(ButtonProps props, ThemeInterface theme) {
        return switch (props.getVariant()) {
            case SECONDARY -> theme.colors().secondary();
            case SUCCESS -> theme.colors().success();
            case WARNING -> theme.colors().warning();
            case DANGER -> theme.colors().danger();
            case GHOST -> "transparent";
            case DISABLED -> theme.colors().textSecondary();
            case PRIMARY -> theme.colors().primary();
        };
    }

    private String getButtonTextColor(ButtonProps props, ThemeInterface theme) {
        return switch (props.getVariant()) {
            case GHOST, DISABLED -> theme.colors().textSecondary();
            default -> "white";
        };
    }

    /**
     * Applies common border styling.
     */
    protected void applyBorderStyling(Node node, ThemeInterface theme) {
        if (borderWidth > 0) {
            String finalBorderColor = getFinalBorderColor(theme, borderColor);
            updateBorderColor(node, finalBorderColor);
            updateBorderWidth(node, ScaleProvider.scale(borderWidth));
        } else {
            // Sem borda definida, só zera para não herdar do CSS padrão
            updateBorderColor(node, "transparent");
            updateBorderWidth(node, 0);
        }

        int finalRadius = borderRadius > 0 ? ScaleProvider.scale(borderRadius) : theme.border().radiusMd();
        updateBorderRadius(node, finalRadius);
    }

    // Adicione este método dentro de ButtonProps.java
    public String resolveTextColor(ThemeInterface theme) {
        if (this.textColor != null) {
            return this.textColor;
        }
        // Caso use o estilo padrão, aplica a mesma regra do applyTheme
        return switch (this.style) {
            case FILLED -> getButtonTextColor(this, theme);
            case OUTLINED, TEXT -> bgColor != null ? bgColor : getButtonColorFromVariant(this, theme);
        };
    }
    @Override
    protected void applyTheme(Node node, Props props, ThemeInterface theme) {
        if (!(node instanceof Button button)) return;

        if (fillWidth) {
            button.setMaxWidth(Double.MAX_VALUE);
        }

        if (getFontSize() != null) {
            updateFontSize(button, ScaleProvider.scale(getFontSize()));
        }

        applyFontStyling(button);

        if (height > 0) {
            double scaled = ScaleProvider.scale(height);
            button.setPrefHeight(scaled);
            button.setMinHeight(scaled);
            button.setMaxHeight(scaled);
        }

        // Cor semântica resolvida uma vez, reaproveitada nos 3 estilos abaixo.
        String accentColor = bgColor != null ? bgColor : getButtonColorFromVariant((ButtonProps) props, theme);

        // bgColor/textColor explícitos continuam tendo prioridade total — só entram
        // aqui quando NÃO há state reativo controlando (mesmo guard de antes).
        if (bgColorState == null) {
            String finalBg = switch (style) {
                case FILLED -> accentColor;
                case OUTLINED, TEXT -> "transparent";
            };
            applyColor(node, finalBg, FX_BG_COLOR);
        }

        // textColor só aplica estaticamente se não há state reativo controlando
        if (textColorState == null) {
            String finalTextColor = textColor != null ? textColor : switch (style) {
                case FILLED -> getButtonTextColor((ButtonProps) props, theme);
                case OUTLINED, TEXT -> accentColor;
            };
            applyColor(node, finalTextColor, FX_TEXT_FILL);
        }

        if(iconOnRight){
            button.setContentDisplay(ContentDisplay.RIGHT);
        }

        // Borda: OUTLINED usa a cor semântica como borda por padrão, a menos que
        // borderColor/borderWidth tenham sido setados manualmente (prioridade
        // já tratada dentro de applyBorderStyling via borderWidth>0).
        if (style == ButtonStyle.OUTLINED && borderWidth == 0) {
            borderWidth = theme.border().width();
            if (borderColor == null) borderColor = accentColor;
        }

        applyBorderStyling(button, theme);

        button.setPadding(resolvePadding(theme));
    }

    @Override
    protected void bindStates(Node node) {
        bind(node, bgColorState, color -> applyColor(node, color, FX_BG_COLOR));
        bind(node, textColorState, color -> applyColor(node, color, FX_TEXT_FILL));
    }
}
