package megalodonte.props;

import javafx.scene.Node;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextInputControl;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
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
    private int minHeight;
    private int maxHeight;

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

    /**
     * Só tem efeito em {@link megalodonte.components.inputs.TextAreaInput} (TextArea
     * de várias linhas) e só quando {@link #height(int)} NÃO for usado — os dois são
     * modos alternativos: {@code height} trava um tamanho fixo, {@code minHeight}/
     * {@link #maxHeight(int)} deixam a caixa crescer com o conteúdo dentro desse
     * intervalo (rola por dentro só depois de passar do máximo).
     */
    public InputProps minHeight(int minHeight){
        this.minHeight = minHeight;
        return this;
    }

    /** Ver {@link #minHeight(int)}. */
    public InputProps maxHeight(int maxHeight){
        this.maxHeight = maxHeight;
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

    private boolean editable = true;

    /** {@code false} deixa o texto só-leitura (selecionável/copiável, mas não editável). */
    public InputProps editable(boolean editable) {
        this.editable = editable;
        return this;
    }

    private boolean fillHeight;

    /**
     * Só tem efeito em {@link megalodonte.components.inputs.TextAreaInput} — estica pra
     * preencher a altura que o pai oferecer (VBox.setVgrow ALWAYS + sem teto de altura),
     * em vez de travar na altura preferida do conteúdo. Alternativa a {@link #height(int)}/
     * {@link #maxHeight(int)}, pro caso de um viewer que deve ocupar o espaço disponível da
     * tela (ex.: um log inteiro) em vez de crescer/rolar com um teto fixo.
     */
    public InputProps fillHeight() {
        this.fillHeight = true;
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

        input.setEditable(editable);

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

        applyFontFamily(textArea);
        textArea.setEditable(editable);

        if (height > 0) {
            double scaled = ScaleProvider.scale(height);
            textArea.setPrefHeight(scaled);
            textArea.setMinHeight(scaled);
            textArea.setMaxHeight(scaled);
        } else if (maxHeight > 0) {
            growWithContentUpToMax(textArea, minHeight, maxHeight);
        } else if (fillHeight) {
            textArea.setMinHeight(Region.USE_COMPUTED_SIZE);
            textArea.setMaxHeight(Double.MAX_VALUE);
            VBox.setVgrow(textArea, Priority.ALWAYS);
        } else {
            // Same reasoning as Input's height branch above.
            textArea.setMaxHeight(Region.USE_PREF_SIZE);
        }

        if(width > 0){
            double scaled = ScaleProvider.scale(width);
            textArea.setPrefWidth(scaled);
            textArea.setMinWidth(scaled);
            textArea.setMaxWidth(scaled);
        } else if (fillHeight) {
            // Ecoa o mesmo raciocínio do teto de altura acima: sem isso, um viewer em
            // fillHeight ainda fica travado na largura do conteúdo em vez de esticar
            // pra ocupar a largura real da tela.
            textArea.setMaxWidth(Double.MAX_VALUE);
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
     * TextArea nativo não cresce sozinho com o conteúdo — sem {@code prefRowCount}
     * explícito, a altura preferida fica travada em ~2 linhas pra sempre, não importa
     * quanto texto tenha dentro (só rola por dentro). Pra imitar um textarea que
     * cresce até um teto, medimos o texto atual com um {@link Text} auxiliar (mesma
     * técnica já usada em {@code v2.Input} pro caret/scroll horizontal) do mesmo jeito
     * que ele vai quebrar linha dentro do TextArea (largura igual, wrapping ligado), e
     * recalculamos {@code prefHeight} a cada mudança de texto ou de largura,
     * limitado entre {@code minHeight} e {@code maxHeight}.
     */
    private void growWithContentUpToMax(TextArea textArea, int minHeightRaw, int maxHeightRaw) {
        double minH = ScaleProvider.scale(minHeightRaw > 0 ? minHeightRaw : 60);
        double maxH = ScaleProvider.scale(maxHeightRaw);
        double vPadding = 24; // borda + padding interno do TextArea (ver -fx-padding acima)

        textArea.setMinHeight(minH);
        textArea.setMaxHeight(maxH);

        var measurer = new javafx.scene.text.Text();
        measurer.setFont(textArea.getFont());
        measurer.setWrappingWidth(Math.max(0, textArea.getWidth() - 12));

        Runnable recalc = () -> {
            String value = textArea.getText();
            measurer.setText(value == null || value.isEmpty() ? " " : value);
            double needed = measurer.getLayoutBounds().getHeight() + vPadding;
            textArea.setPrefHeight(Math.max(minH, Math.min(maxH, needed)));
        };

        textArea.fontProperty().addListener((obs, old, font) -> {
            measurer.setFont(font);
            recalc.run();
        });
        textArea.textProperty().addListener((obs, old, v) -> recalc.run());
        textArea.widthProperty().addListener((obs, old, w) -> {
            measurer.setWrappingWidth(Math.max(0, w.doubleValue() - 12));
            recalc.run();
        });

        // Na primeira aplicação a largura real do TextArea ainda não existe (layout
        // do pai só roda depois) — adia o primeiro cálculo pro próximo pulse.
        javafx.application.Platform.runLater(recalc);
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
