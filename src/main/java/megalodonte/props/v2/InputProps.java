package megalodonte.props.v2;

import javafx.scene.Node;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import megalodonte.base.scale.ScaleProvider;
import megalodonte.base.theme.ThemeInterface;
import megalodonte.props.Props;
import megalodonte.props.TextComponentProps;
import megalodonte.props.TextTone;

import static megalodonte.styles.util.StyleUtils.*;

/**
 * Props para {@link megalodonte.components.v2.Input} — separado de
 * {@link megalodonte.props.InputProps} porque o node estilizado aqui não é um
 * {@code TextInputControl} (não existe nenhum Control nativo por baixo), então
 * não faz sentido reaproveitar uma classe cuja API pressupõe isso.
 */
public class InputProps extends TextComponentProps<InputProps> {

    private String placeholder;
    private int height;
    private int width;
    private TextTone tone = TextTone.PRIMARY;
    private boolean disabled;

    protected String bgColor;
    protected String borderColor;
    protected int borderWidth;
    protected int borderRadius;
    protected String placeholderColor;

    public InputProps tone(TextTone tone) {
        this.tone = tone;
        return this;
    }

    public TextTone getTone() {
        return tone;
    }

    public InputProps height(int height) {
        this.height = height;
        return this;
    }

    public InputProps width(int width) {
        this.width = width;
        return this;
    }

    public InputProps placeHolder(String text) {
        this.placeholder = text;
        return this;
    }

    public InputProps disable() {
        this.disabled = true;
        return this;
    }

    public InputProps bgColor(String bgColor) {
        this.bgColor = bgColor;
        return this;
    }

    public InputProps borderColor(String borderColor) {
        this.borderColor = borderColor;
        return this;
    }

    public InputProps borderWidth(int borderWidth) {
        this.borderWidth = borderWidth;
        return this;
    }

    public InputProps borderRadius(int borderRadius) {
        this.borderRadius = borderRadius;
        return this;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public String getPlaceholder() {
        return placeholder;
    }

    @Override
    protected void applyTheme(Node node, Props props, ThemeInterface theme) {
        if (!(node instanceof StackPane outer)) return;
        if (!(outer.getChildren().get(0) instanceof Region surface)) return;

        var text = (Text) surface.lookup("#megalodonte-input-text");
        var placeholderNode = (Text) surface.lookup("#megalodonte-input-placeholder");
        var caret = surface.lookup("#megalodonte-input-caret");
        var selectionRect = surface.lookup("#megalodonte-input-selection");
        if (text == null || placeholderNode == null) return;

        // O tamanho vai no StackPane externo (outer), não no Pane interno
        // (surface) — StackPane sempre redimensiona o filho gerenciado pra
        // preencher a si mesmo, então constranger só o outer já basta, e
        // constranger o surface por engano deixa o outer sem limite de
        // tamanho (herda Double.MAX_VALUE do Region por padrão), fazendo o
        // campo "escapar" do layout do pai quando ele tenta esticar/centralizar.
        if (height > 0) {
            double scaled = ScaleProvider.scale(height);
            outer.setPrefHeight(scaled);
            outer.setMinHeight(scaled);
            outer.setMaxHeight(scaled);
        } else {
            outer.setMaxHeight(Region.USE_PREF_SIZE);
        }

        if (width > 0) {
            double scaled = ScaleProvider.scale(width);
            outer.setPrefWidth(scaled);
            outer.setMinWidth(scaled);
            outer.setMaxWidth(scaled);
        } else {
            outer.setMaxWidth(Region.USE_PREF_SIZE);
        }

        if (disabled) {
            surface.setDisable(true);
        }

        if (placeholder != null) {
            placeholderNode.setText(placeholder);
        }

        int fontSize = getFontSize() != null ? ScaleProvider.scale(getFontSize()) : theme.typography().body();
        updateFontSize(text, fontSize);
        updateFontSize(placeholderNode, fontSize);

        String finalTextColor = getFinalInputTextColor(theme);
        updateTextColor(text, finalTextColor);
        if (caret != null) updateTextColor(caret, finalTextColor);
        if (selectionRect != null) updateTextColor(selectionRect, theme.colors().selection());

        String finalPlaceholderColor = placeholderColor != null && !placeholderColor.isBlank()
                ? placeholderColor : theme.colors().placeholder();
        updateTextColor(placeholderNode, finalPlaceholderColor);

        String finalBgColor = getFinalBackgroundColor(theme, bgColor);
        updateBackgroundColor(surface, finalBgColor);

        String finalBorderColor = getFinalBorderColor(theme, borderColor);
        updateBorderColor(surface, finalBorderColor);

        int finalBorderWidth = borderWidth > 0 ? ScaleProvider.scale(borderWidth) : theme.border().width();
        if (finalBorderWidth > 0) {
            updateBorderWidth(surface, finalBorderWidth);
        }

        int finalRadius = borderRadius > 0 ? ScaleProvider.scale(borderRadius) : theme.border().radiusMd();
        updateBorderRadius(surface, finalRadius);
        updateBorderRadius(outer, finalRadius);
        updateBorderColor(outer, "transparent");
    }

    private String getFinalInputTextColor(ThemeInterface theme) {
        if (textColor != null && !textColor.isBlank()) {
            return textColor;
        }
        return switch (tone) {
            case PRIMARY -> theme.colors().textPrimary();
            case SECONDARY, DISABLED -> theme.colors().textSecondary();
            case INVERTED -> theme.colors().background();
        };
    }
}
