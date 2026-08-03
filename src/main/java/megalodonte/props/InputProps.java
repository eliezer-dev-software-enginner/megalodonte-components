package megalodonte.props;

import javafx.scene.Node;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextInputControl;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import megalodonte.base.scale.ScaleProvider;
import megalodonte.base.theme.ThemeInterface;

import static megalodonte.styles.util.StyleUtils.*;

//TODO: criar placeholderSize
public class InputProps extends TextComponentProps<InputProps> {

    //TODO: mover para InputProps
    @Deprecated(forRemoval = true)
    private String placeholder;
    private int height;
    private int width;

    private TextTone tone = TextTone.PRIMARY;

    public InputProps tone(TextTone tone) {
        this.tone = tone;
        return this;
    }

    public TextTone getTone() {
        return tone;
    }

    public InputProps height(int height){
        this.height = height;
        return this;
    }

    public InputProps width(int width){
        this.width = width;
        return this;
    }

    public InputProps placeHolder(String text){
        this.placeholder = text;
        return this;
    }

    boolean disabled;
    public InputProps disable(){
        this.disabled = true;
        return this;
    }

    protected String bgColor;
    protected String borderColor;
    protected int borderWidth;
    protected int borderRadius;

    // Fluent API methods
    public InputProps bgColor(String bgColor) {
        this.bgColor = bgColor;
        return  this;
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

    protected String placeholderColor;

    @Override
    protected void applyTheme(Node node, Props props, ThemeInterface theme) {

        if (node instanceof TextArea textArea) {
            applyTextAreaTheme(textArea, theme, (InputProps) props);
            return;
        }

        if (!(node instanceof StackPane stackPane)) return;

        var input = (TextInputControl) stackPane.getChildren().get(0);
        if(input == null) return;

        if (getFontSize() != null) {
            updateFontSize(input, ScaleProvider.scale(getFontSize()));
        }

        if (getFontSizeState() != null) {
            getFontSizeState().subscribe(v -> updateFontSize(input, ScaleProvider.scale(v)));
        }

        if(disabled){
            input.setDisable(true);
        }

        if (placeholder != null) {
            input.setPromptText(placeholder);
        }

        if(height > 0){
            double scaled = ScaleProvider.scale(height);
            input.setPrefHeight(scaled);
            input.setMinHeight(scaled);
            input.setMaxHeight(scaled);
        } else {
            // Same reasoning as the width branch below: without a cap, a fitToHeight
            // ScrollPane (e.g. Components.ScrollPaneDefault, used by every CRUD screen)
            // asks this input's wrapping StackPane to grow to the viewport height, and
            // an uncapped TextInputControl inside it can drive JavaFX's layout pass into
            // unbounded recursion — StackOverflowError — instead of just hugging its
            // natural single-line height.
            input.setMaxHeight(Region.USE_PREF_SIZE);
            stackPane.setMaxHeight(Region.USE_PREF_SIZE);
        }

        if(width > 0){
            double scaled = ScaleProvider.scale(width);
            stackPane.setPrefWidth(scaled);
            stackPane.setMinWidth(scaled);
            stackPane.setMaxWidth(scaled);
        } else {
            // TextField's own max width is unbounded by default, so without this a
            // parent VBox/HBox with fillWidth (e.g. Container) stretches the input to
            // fill all available space instead of hugging its natural/content width —
            // same "don't grow unless asked to" default Container already follows.
            stackPane.setMaxWidth(Region.USE_PREF_SIZE);
        }

        // Apply background styling to both stackpane and input
        String finalBgColor = getFinalBackgroundColor(theme, bgColor);
        updateBackgroundColor(stackPane, finalBgColor);
        updateBackgroundColor(input, finalBgColor);

        // Apply border styling without custom radius
        applyInputBorderStyling(stackPane, theme);

        // Apply text styling to input
        applyInputTextStyling(input, theme, (InputProps) props);
    }

    private void applyTextAreaTheme(TextArea textArea, ThemeInterface theme, InputProps props) {
        int finalRadius = borderRadius > 0 ? ScaleProvider.scale(borderRadius) : theme.border().radiusMd();
        int finalBorderWidth = borderWidth > 0 ? ScaleProvider.scale(borderWidth) : theme.border().width();
        String finalBorderColor = getFinalBorderColor(theme, borderColor);
        String finalBgColor = getFinalBackgroundColor(theme, bgColor);

        // No SW pipeline, background-color precisa de um layer explícito
        // e background-radius precisa corresponder a cada layer
        applyStyleProperty(textArea, finalBgColor, "-fx-control-inner-background");
        updateBackgroundColor(textArea, "-fx-control-inner-background");
        updateBorderRadius(textArea, finalRadius); // seta -fx-border-radius e -fx-background-radius
        updateBorderColor(textArea, finalBorderColor);
        updateBorderWidth(textArea, finalBorderWidth);
        applyStyleProperty(textArea, "4px", "-fx-padding");
        // O bevel de 3 camadas do Modena em .text-area é achatado via
        // text-area.css (author stylesheet), não aqui — inline setStyle() não
        // sobrescreve as 3 camadas de -fx-background-color/-fx-background-insets
        // de forma confiável (limitação documentada do JavaFX CSS pra
        // propriedades multi-camada; ver TextAreaInput/text-area.css).

        if (getFontSize() != null) {
            updateFontSize(textArea, ScaleProvider.scale(getFontSize()));
        }

        if (height > 0) {
            double scaled = ScaleProvider.scale(height);
            textArea.setPrefHeight(scaled);
            textArea.setMinHeight(scaled);
            textArea.setMaxHeight(scaled);
        } else {
            // Same reasoning as Input's height branch above.
            textArea.setMaxHeight(Region.USE_PREF_SIZE);
        }

        if(width > 0){
            double scaled = ScaleProvider.scale(width);
            textArea.setPrefWidth(scaled);
            textArea.setMinWidth(scaled);
            textArea.setMaxWidth(scaled);
        }

        if(disabled){
            textArea.setDisable(true);
        }

        if (placeholder != null) {
            textArea.setPromptText(placeholder);
        }

        applyInputTextStyling(textArea, theme, props);
    }

    /**
     * Apply border styling for inputs without custom radius, using only theme defaults.
     */
    private void applyInputBorderStyling(StackPane stackPane, ThemeInterface theme) {
        var input = (TextInputControl) stackPane.getChildren().get(0);

        String finalBorderColor = getFinalBorderColor(theme, borderColor);
        updateBorderColor(input, finalBorderColor);

        int finalBorderWidth = theme.border().width();
        if (finalBorderWidth > 0) {
            updateBorderWidth(input, finalBorderWidth);
        }

        int finalRadius = borderRadius > 0 ? ScaleProvider.scale(borderRadius) : theme.border().radiusMd();
        updateBorderRadius(input, finalRadius);

        // StackPane também precisa do mesmo radius para não clipar as bordas do filho
        updateBorderRadius(stackPane, finalRadius);
        updateBorderColor(stackPane, "transparent");

        // Modena define .text-field com 3 camadas de background empilhadas
        // (-fx-shadow-highlight-color, -fx-text-box-border, -fx-control-inner-background,
        // cada uma com seu próprio -fx-background-insets) pra simular um bevel.
        // setStyle() aqui não sobrescreve essas 3 camadas de forma confiável — é uma
        // limitação documentada do JavaFX CSS pra propriedades multi-camada (o valor
        // inline não "cicla" pelas 3 camadas do jeito que cicla numa regra de
        // stylesheet). Por isso o fix real está em text-field.css (author stylesheet,
        // achata pra 1 camada só), anexado ao próprio TextField em Input — aqui só
        // a borda visível (cor/largura/raio) mesmo, que ele complementa.
    }

    /**
     * Applies text styling for Input components.
     */
    protected void applyInputTextStyling(Node inputNode, ThemeInterface theme, InputProps props) {
        String finalTextColor = getFinalInputTextColor(theme, props);
        updateTextColor_Input(inputNode, finalTextColor);

        String finalPlaceholderColor = placeholderColor != null && !placeholderColor.isBlank() ?
                placeholderColor : theme.colors().placeholder();
        updatePlaceholderColor(inputNode, finalPlaceholderColor);

        int fontSize = props.getFontSize() != null ?
                ScaleProvider.scale(props.getFontSize()) :
                theme.typography().body();
        updateFontSize(inputNode, fontSize);

        // Explicitly set placeholder font size to match input font size
        updatePlaceholderFontSize(inputNode, fontSize);
    }

    /**
     * Gets the final text color for input components with custom color fallback.
     */
    protected String getFinalInputTextColor(ThemeInterface theme, InputProps props) {
        if (textColor != null && !textColor.isBlank()) {
            return textColor;
        }

        return switch (props.getTone()) {
            case PRIMARY -> theme.colors().textPrimary();
            case SECONDARY, DISABLED -> theme.colors().textSecondary();
            case INVERTED -> theme.colors().background();
        };
    }

}
