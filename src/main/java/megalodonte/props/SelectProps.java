package megalodonte.props;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import megalodonte.base.scale.ScaleProvider;
import megalodonte.base.theme.ThemeInterface;
import megalodonte.utils.Utils;
import megalodonte.utils.related.TextVariant;

import static megalodonte.styles.util.StyleUtils.getFinalBackgroundColor;
import static megalodonte.styles.util.StyleUtils.getFinalBorderColor;
import static megalodonte.styles.util.StyleUtils.getFinalBorderRadius;
import static megalodonte.styles.util.StyleUtils.getFinalBorderWidth;

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

    private TextVariant variant = TextVariant.BODY;

    public SelectProps variant(TextVariant variant) {
        this.variant = variant;
        return this;
    }

    public TextVariant getVariant() {
        return variant;
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
            Utils.updateFontSize(cBox, ScaleProvider.scale(getFontSize()));
        }

        // Background
        String finalBgColor = getFinalBackgroundColor(theme, bgColor);
        Utils.updateBackgroundColor(cBox, finalBgColor);

        // Border
        String finalBorderColor = getFinalBorderColor(theme, borderColor);
        int finalBorderWidth = getFinalBorderWidth(theme, ScaleProvider.scale(borderWidth));
        int finalBorderRadius = getFinalBorderRadius(theme, ScaleProvider.scale(borderRadius));

        Utils.updateBorderColor(cBox, finalBorderColor);
        Utils.updateBorderWidth(cBox, finalBorderWidth);
        Utils.updateBorderRadius(cBox, finalBorderRadius);

        // Text color via theme tone or inline
        String finalTextColor = getFinalSelectTextColor(theme);
        if (textColor != null && !textColor.isBlank()) {
            finalTextColor = textColor;
        }
        Utils.updateTextColor_Input(cBox, finalTextColor);
    }

    private String getFinalSelectTextColor(ThemeInterface theme) {
        return switch (tone) {
            case PRIMARY -> theme.colors().textPrimary();
            case SECONDARY, DISABLED -> theme.colors().textSecondary();
            case INVERTED -> theme.colors().background();
        };
    }
}
