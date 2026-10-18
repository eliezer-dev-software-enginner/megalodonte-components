package megalodonte.components.inputs;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import megalodonte.base.components.Component;
import megalodonte.base.state.State;
import megalodonte.base.theme.ThemeManager;
import org.fxmisc.flowless.VirtualizedScrollPane;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;
import org.fxmisc.richtext.model.StyleSpans;
import org.fxmisc.richtext.model.StyleSpansBuilder;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** A theme-aware Java source editor with line numbers and syntax highlighting. */
public final class JavaCodeEditor extends Component {
    private static final Pattern TOKENS = Pattern.compile(
            "(?<COMMENT>//[^\\n]*|(?s:/\\*.*?\\*/))"
                    + "|(?<STRING>\"(?:\\\\.|[^\"\\\\])*\"|'(?:\\\\.|[^'\\\\])*')"
                    + "|(?<KEYWORD>\\b(?:abstract|assert|boolean|break|byte|case|catch|char|class|const|continue|default|do|double|else|enum|extends|final|finally|float|for|if|implements|import|instanceof|int|interface|long|native|new|package|private|protected|public|record|return|short|static|strictfp|super|switch|synchronized|this|throw|throws|transient|try|var|void|volatile|while|yield|null|true|false)\\b)"
                    + "|(?<NUMBER>\\b\\d+(?:\\.\\d+)?\\b)");

    private final CodeArea area;

    public JavaCodeEditor(State<String> state, double height) {
        super(new StackPane(), null);
        StackPane root = (StackPane) getJavaFxNode();
        area = new CodeArea();
        area.getStyleClass().add("megalodonte-java-editor");
        area.getStylesheets().add(getClass().getResource("/java-code-editor.css").toExternalForm());
        area.setParagraphGraphicFactory(LineNumberFactory.get(area));
        area.setWrapText(false);
        root.getChildren().add(new VirtualizedScrollPane<>(area));
        root.setPrefHeight(height);
        root.setMinHeight(height);
        root.setMaxHeight(height);

        ThemeManager.state().subscribe(theme -> {
            if (theme == null) return;
            root.setStyle("-fx-background-color: " + theme.colors().surface() + ";"
                    + " -fx-border-color: " + theme.colors().border() + ";"
                    + " -fx-border-radius: 6; -fx-background-radius: 6; -fx-border-width: 1;"
                    + " -code-text: " + theme.colors().textPrimary() + ";"
                    + " -code-muted: " + theme.colors().textSecondary() + ";"
                    + " -code-keyword: " + theme.colors().primary() + ";"
                    + " -code-string: " + theme.colors().success() + ";"
                    + " -code-number: " + theme.colors().secondary() + ";");
        });

        area.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.TAB) {
                area.replaceSelection("    ");
                event.consume();
            } else if (event.getCode() == KeyCode.ENTER) {
                String before = area.getText().substring(0, area.getCaretPosition());
                String line = before.substring(before.lastIndexOf('\n') + 1);
                String indent = line.substring(0, line.length() - line.stripLeading().length());
                if (line.stripTrailing().endsWith("{")) indent += "    ";
                area.replaceSelection("\n" + indent);
                event.consume();
            }
        });
        area.textProperty().addListener((ignored, oldText, value) -> {
            if (!value.isEmpty()) area.setStyleSpans(0, highlight(value));
            if (state != null && !value.equals(state.get())) state.set(value);
        });
        if (state != null) {
            state.subscribe(value -> {
                String text = value == null ? "" : value;
                if (!area.getText().equals(text)) area.replaceText(text);
            });
        }
    }

    private static StyleSpans<Collection<String>> highlight(String text) {
        Matcher matcher = TOKENS.matcher(text);
        int end = 0;
        StyleSpansBuilder<Collection<String>> styles = new StyleSpansBuilder<>();
        while (matcher.find()) {
            styles.add(Collections.emptyList(), matcher.start() - end);
            String kind = matcher.group("COMMENT") != null ? "comment"
                    : matcher.group("STRING") != null ? "string"
                    : matcher.group("KEYWORD") != null ? "keyword" : "number";
            styles.add(List.of(kind), matcher.end() - matcher.start());
            end = matcher.end();
        }
        styles.add(Collections.emptyList(), text.length() - end);
        return styles.create();
    }
}
