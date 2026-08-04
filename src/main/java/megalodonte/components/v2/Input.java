package megalodonte.components.v2;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Duration;
import megalodonte.base.UI;
import megalodonte.base.components.Component;
import megalodonte.base.state.State;
import megalodonte.components.FocusableFieldInterface;
import megalodonte.components.inputs.OnChangeResult;
import megalodonte.props.v2.InputProps;

import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Campo de texto de uma linha construído do zero — sem {@code TextField},
 * {@code TextArea} ou qualquer outro {@code Control}/{@code TextInputControl}
 * nativo do JavaFX por baixo. Texto, caret, seleção e navegação por
 * teclado/mouse são desenhados e tratados manualmente aqui com
 * {@link Text}/{@link Rectangle} sobre um {@link Pane} puro — como nenhum
 * Control nativo entra em cena, nenhuma regra de CSS do Modena (nem
 * ".text-field", nem nenhuma outra skin) chega a se aplicar a esse node; não
 * há nada pra neutralizar, a aparência é 100% definida pela aplicação via
 * {@link InputProps}.
 * <p>
 * Limitações conhecidas (fora do escopo desta primeira versão): sem
 * drag-select do mouse, sem IME/composição de texto acentuado complexo além do
 * suportado por {@code KeyEvent.getCharacter()}, sem menu de contexto
 * (clique-direito). Copiar/colar/recortar e seleção via teclado (Shift+setas,
 * Ctrl+A) funcionam.
 */
public class Input extends Component implements FocusableFieldInterface<Input> {

    private final StackPane outer;
    private final Pane surface;
    private final Text textNode;
    private final Text placeholderNode;
    private final Text measurer;
    private final Rectangle caret;
    private final Rectangle selectionRect;
    private final Timeline caretBlink;

    private Node left;
    private Node right;

    private String value = "";
    private int caretIndex = 0;
    private int selectionAnchor = -1;
    private double scrollOffset = 0;

    private boolean internalChange = false;
    private boolean lockCursorToEnd = false;

    private State<String> state;
    private Function<String, OnChangeResult> onChange;
    private Function<String, OnChangeResult> onInitialize;
    private Runnable onEnter;

    private double leftPad = 8;
    private double rightPad = 8;

    public Input(State<String> state) {
        this(state, new InputProps());
    }

    public Input(State<String> state, InputProps props) {
        super(new StackPane());
        this.outer = (StackPane) node;

        this.surface = new Pane();
        surface.setFocusTraversable(true);
        surface.setCursor(Cursor.TEXT);

        this.textNode = new Text();
        textNode.setId("megalodonte-input-text");
        textNode.setTextOrigin(javafx.geometry.VPos.CENTER);

        this.placeholderNode = new Text();
        placeholderNode.setId("megalodonte-input-placeholder");
        placeholderNode.setTextOrigin(javafx.geometry.VPos.CENTER);
        placeholderNode.setMouseTransparent(true);

        this.measurer = new Text();
        measurer.setManaged(false);
        measurer.setVisible(false);

        this.caret = new Rectangle(1, 14);
        caret.setId("megalodonte-input-caret");
        caret.setVisible(false);
        caret.setMouseTransparent(true);

        this.selectionRect = new Rectangle(0, 16);
        selectionRect.setId("megalodonte-input-selection");
        selectionRect.setVisible(false);
        selectionRect.setOpacity(0.35);
        selectionRect.setMouseTransparent(true);

        surface.getChildren().addAll(placeholderNode, selectionRect, textNode, caret, measurer);
        outer.getChildren().add(surface);

        // Pane não corta filhos que ultrapassam seus próprios limites por
        // padrão — sem isso, texto/caret que passam da largura fixada do
        // campo vazam visualmente pra fora da borda.
        var clip = new Rectangle();
        clip.widthProperty().bind(surface.widthProperty());
        clip.heightProperty().bind(surface.heightProperty());
        surface.setClip(clip);

        caretBlink = new Timeline(new KeyFrame(Duration.millis(530), e -> caret.setVisible(!caret.isVisible())));
        caretBlink.setCycleCount(Timeline.INDEFINITE);

        setupFocusBehavior();
        setupMouseHandling();
        setupKeyHandling();
        surface.heightProperty().addListener((obs, old, h) -> layoutContent());
        surface.widthProperty().addListener((obs, old, w) -> layoutContent());

        if (props != null) props.apply(node);

        textNode.fontProperty().addListener((obs, old, font) -> {
            measurer.setFont(font);
            placeholderNode.setFont(font);
            layoutContent();
        });
        measurer.setFont(textNode.getFont());

        bind(state);
        layoutContent();
    }

    private void setupFocusBehavior() {
        surface.focusedProperty().addListener((obs, old, focused) -> {
            if (focused) {
                caret.setVisible(true);
                caretBlink.playFromStart();
            } else {
                caretBlink.stop();
                caret.setVisible(false);
                selectionAnchor = -1;
                layoutContent();
            }
            if (onChangeFocusHandler != null) onChangeFocusHandler.accept(focused);
        });
    }

    private void setupMouseHandling() {
        surface.addEventHandler(MouseEvent.MOUSE_PRESSED, e -> {
            surface.requestFocus();
            int idx = indexAt(e.getX());
            setCaret(idx, e.isShiftDown());
            e.consume();
        });
    }

    private void setupKeyHandling() {
        surface.addEventHandler(KeyEvent.KEY_TYPED, e -> {
            String ch = e.getCharacter();
            if (ch == null || ch.isEmpty()) return;
            char c = ch.charAt(0);
            if (Character.isISOControl(c) || e.isControlDown() || e.isMetaDown()) return;
            insertAtCaret(ch);
            e.consume();
        });

        surface.addEventHandler(KeyEvent.KEY_PRESSED, e -> {
            boolean shortcut = e.isControlDown() || e.isMetaDown();
            switch (e.getCode()) {
                case BACK_SPACE -> {
                    if (hasSelection()) deleteSelection();
                    else if (caretIndex > 0) {
                        value = value.substring(0, caretIndex - 1) + value.substring(caretIndex);
                        caretIndex--;
                    }
                    notifyChange();
                }
                case DELETE -> {
                    if (hasSelection()) deleteSelection();
                    else if (caretIndex < value.length()) {
                        value = value.substring(0, caretIndex) + value.substring(caretIndex + 1);
                    }
                    notifyChange();
                }
                case LEFT -> setCaret(Math.max(0, caretIndex - 1), e.isShiftDown());
                case RIGHT -> setCaret(Math.min(value.length(), caretIndex + 1), e.isShiftDown());
                case HOME -> setCaret(0, e.isShiftDown());
                case END -> setCaret(value.length(), e.isShiftDown());
                case ENTER -> { if (onEnter != null) onEnter.run(); }
                case A -> { if (shortcut) { selectionAnchor = 0; setCaret(value.length(), true); } }
                case C -> { if (shortcut) copySelection(); }
                case X -> { if (shortcut) { copySelection(); if (hasSelection()) { deleteSelection(); notifyChange(); } } }
                case V -> { if (shortcut) pasteFromClipboard(); }
                default -> { }
            }
            if (e.getCode().isNavigationKey() || shortcut) e.consume();
        });
    }

    private boolean hasSelection() {
        return selectionAnchor != -1 && selectionAnchor != caretIndex;
    }

    private int selectionStart() {
        return Math.min(selectionAnchor, caretIndex);
    }

    private int selectionEnd() {
        return Math.max(selectionAnchor, caretIndex);
    }

    private void deleteSelection() {
        int start = selectionStart();
        int end = selectionEnd();
        value = value.substring(0, start) + value.substring(end);
        caretIndex = start;
        selectionAnchor = -1;
    }

    private void insertAtCaret(String text) {
        if (hasSelection()) deleteSelection();
        value = value.substring(0, caretIndex) + text + value.substring(caretIndex);
        caretIndex += text.length();
        selectionAnchor = -1;
        notifyChange();
    }

    private void setCaret(int index, boolean extendSelection) {
        if (extendSelection) {
            if (selectionAnchor == -1) selectionAnchor = caretIndex;
        } else {
            selectionAnchor = -1;
        }
        caretIndex = Math.max(0, Math.min(index, value.length()));
        layoutContent();
    }

    private void copySelection() {
        if (!hasSelection()) return;
        var content = new ClipboardContent();
        content.putString(value.substring(selectionStart(), selectionEnd()));
        Clipboard.getSystemClipboard().setContent(content);
    }

    private void pasteFromClipboard() {
        String clip = Clipboard.getSystemClipboard().getString();
        if (clip == null) return;
        clip = clip.replaceAll("[\\r\\n]", "");
        insertAtCaret(clip);
    }

    private int indexAt(double clickX) {
        double x = clickX - leftPad + scrollOffset;
        if (x <= 0) return 0;
        for (int i = 0; i <= value.length(); i++) {
            double w = widthOf(value.substring(0, i));
            if (w >= x) {
                // decide entre a posição i-1 e i, o que estiver mais perto do clique
                double prevW = i > 0 ? widthOf(value.substring(0, i - 1)) : 0;
                return (x - prevW) < (w - x) ? Math.max(0, i - 1) : i;
            }
        }
        return value.length();
    }

    private double widthOf(String s) {
        measurer.setText(s);
        return measurer.getLayoutBounds().getWidth();
    }

    private void notifyChange() {
        if (internalChange) return;
        internalChange = true;
        try {
            if (onChange != null) {
                OnChangeResult result = onChange.apply(value);
                if (result != null) {
                    if (result.getStateValue() != null && state != null && !result.getStateValue().equals(state.get())) {
                        state.set(result.getStateValue());
                    }
                    if (result.getDisplayValue() != null && !result.getDisplayValue().equals(value)) {
                        value = result.getDisplayValue();
                        if (lockCursorToEnd) caretIndex = value.length();
                        else caretIndex = Math.min(caretIndex, value.length());
                    }
                }
            } else if (state != null) {
                state.set(value);
            }
        } finally {
            internalChange = false;
        }
        layoutContent();
    }

    private void layoutContent() {
        textNode.setText(value);
        placeholderNode.setVisible(value.isEmpty());
        placeholderNode.setLayoutX(leftPad);

        double centerY = surface.getHeight() > 0 ? surface.getHeight() / 2.0 : 15;
        placeholderNode.setLayoutY(centerY);
        textNode.setLayoutY(centerY);
        caret.setY(centerY - caret.getHeight() / 2.0);
        selectionRect.setY(centerY - selectionRect.getHeight() / 2.0);

        // Rola o texto horizontalmente pra manter o caret sempre visível
        // dentro da área útil (largura do campo menos o padding) — sem isso,
        // digitar além da largura fixada deixa o texto/caret escondidos atrás
        // do clip em vez de "empurrar" o conteúdo visível, como um TextField
        // de verdade faz.
        double caretXUnscrolled = widthOf(value.substring(0, Math.min(caretIndex, value.length())));
        double visibleWidth = Math.max(0, surface.getWidth() - leftPad - rightPad);

        if (caretXUnscrolled - scrollOffset > visibleWidth) {
            scrollOffset = caretXUnscrolled - visibleWidth;
        }
        if (caretXUnscrolled - scrollOffset < 0) {
            scrollOffset = caretXUnscrolled;
        }

        // Trava de segurança independente do rastreamento do caret acima:
        // scrollOffset nunca pode passar do necessário pro comprimento ATUAL
        // do texto. Sem isso, um scrollOffset "herdado" de um valor bem mais
        // longo (ex: colar uma string gigante e depois apagar tudo de uma vez
        // via seleção) podia deixar layoutX = leftPad - scrollOffset negativo,
        // empurrando o texto pra trás do ícone à esquerda.
        double maxScroll = Math.max(0, widthOf(value) - visibleWidth);
        scrollOffset = Math.min(scrollOffset, maxScroll);
        scrollOffset = Math.max(0, scrollOffset);

        textNode.setLayoutX(leftPad - scrollOffset);
        caret.setX(leftPad + caretXUnscrolled - scrollOffset);

        if (hasSelection()) {
            double startX = leftPad + widthOf(value.substring(0, selectionStart())) - scrollOffset;
            double endX = leftPad + widthOf(value.substring(0, selectionEnd())) - scrollOffset;
            selectionRect.setX(startX);
            selectionRect.setWidth(endX - startX);
            selectionRect.setVisible(true);
        } else {
            selectionRect.setVisible(false);
        }
    }

    // ---- API pública (espelha megalodonte.components.inputs.Input) ----

    public Input onChange(Function<String, OnChangeResult> handler) {
        this.onChange = handler;
        return this;
    }

    public Input onInitialize(Function<String, OnChangeResult> handler) {
        this.onInitialize = handler;
        if (state != null && state.get() != null) applyInitialFormat(state.get());
        return this;
    }

    public Input onEnter(Runnable handler) {
        this.onEnter = handler;
        return this;
    }

    private Consumer<Boolean> onChangeFocusHandler;

    @Override
    public Input onChangeFocus(Consumer<Boolean> eventHandler) {
        this.onChangeFocusHandler = eventHandler;
        return this;
    }

    public Input lockCursorToEnd() {
        this.lockCursorToEnd = true;
        return this;
    }

    public Input left(Node iconNode) {
        this.left = iconNode;
        StackPane.setMargin(iconNode, new Insets(0, 0, 0, 8));
        StackPane.setAlignment(iconNode, Pos.CENTER_LEFT);
        outer.getChildren().add(iconNode);
        adjustPadding();
        return this;
    }

    public Input right(Node iconNode) {
        this.right = iconNode;
        StackPane.setMargin(iconNode, new Insets(0, 8, 0, 0));
        StackPane.setAlignment(iconNode, Pos.CENTER_RIGHT);
        outer.getChildren().add(iconNode);
        adjustPadding();
        return this;
    }

    private void adjustPadding() {
        leftPad = left != null ? 32 : 8;
        rightPad = right != null ? 32 : 8;
        layoutContent();
    }

    @Override
    public void requestFocus() {
        UI.runOnUi(surface::requestFocus);
    }

    private void bind(State<String> state) {
        this.state = state;

        state.subscribe(v -> {
            if (internalChange) return;
            // Guard de internalChange acima já filtra a auto-notificação de
            // uma digitação (state.set(...) disparado de dentro de
            // notifyChange() com internalChange=true) — então esse listener só
            // roda mesmo pra mudanças externas de state (valor inicial, reset
            // de formulário, carregar registro pra edição). Nesses casos o
            // caret sempre vai pro final, não pro início.
            if (onInitialize != null) {
                applyInitialFormat(v);
            } else {
                internalChange = true;
                value = v == null ? "" : v;
                caretIndex = value.length();
                internalChange = false;
            }
            layoutContent();
        });
    }

    private void applyInitialFormat(String v) {
        if (onInitialize == null) return;
        OnChangeResult result = onInitialize.apply(v);
        if (result != null) {
            internalChange = true;
            value = result.getDisplayValue() != null ? result.getDisplayValue() : "";
            caretIndex = value.length();
            if (result.getStateValue() != null && !result.getStateValue().equals(v) && state != null) {
                state.set(result.getStateValue());
            }
            internalChange = false;
        }
    }
}
