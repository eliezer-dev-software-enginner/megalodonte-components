package megalodonte.components.inputs;

import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class JavaCodeEditorHighlightTest {
    @Test void customIdentifiersAreHighlightedOnlyAsWholeCodeTokens() {
        String text = "return ProjectEnvironment.get(\"ProjectEnvironment\"); // ProjectEnvironment\nProjectEnvironmentExtra";
        var spans = JavaCodeEditor.highlight(text, JavaCodeEditor.tokenPattern(List.of("ProjectEnvironment")), java.util.Set.of());
        int position = 0;
        int keywordCount = 0;
        for (var span : spans) {
            String segment = text.substring(position, position + span.getLength());
            if (span.getStyle().contains("keyword")) {
                assertTrue(segment.equals("return") || segment.equals("ProjectEnvironment"));
                keywordCount++;
            }
            if (segment.contains("ProjectEnvironment") && segment.startsWith("\""))
                assertTrue(span.getStyle().contains("string"));
            if (segment.startsWith("//")) assertTrue(span.getStyle().contains("comment"));
            position += span.getLength();
        }
        assertEquals(2, keywordCount);
        assertEquals(text.length(), position);
    }

    @Test void highlightsContextAndDirectMembersWithoutColoringOtherObjectsOrStrings() {
        String text = "context.userId() + context . chatId() + other.userId() + \"context.userId\" // context.chatId";
        var spans = JavaCodeEditor.highlight(text, JavaCodeEditor.tokenPattern(List.of("context")), java.util.Set.of("context"));
        var highlighted = new java.util.ArrayList<String>();
        int offset = 0;
        for (var span : spans) {
            String segment = text.substring(offset, offset + span.getLength());
            if (span.getStyle().contains("keyword")) highlighted.add(segment.trim());
            if (segment.startsWith("\"")) assertTrue(span.getStyle().contains("string"));
            if (segment.startsWith("//")) assertTrue(span.getStyle().contains("comment"));
            offset += span.getLength();
        }
        assertEquals(List.of("context", "userId", "context", "chatId"), highlighted);
    }
}
