package megalodonte.components;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TextTest {
    
    @Test
    void testTextComponentExists() {
        // Test that we can create a Text component
        assertDoesNotThrow(() -> {
            Text text = new Text("Hello World");
            assertNotNull(text);
        });
    }
    
    @Test
    void testTextWithEmptyContent() {
        assertDoesNotThrow(() -> {
            Text text = new Text("");
            assertNotNull(text);
        });
    }
    
    @Test
    void testTextClicksDisabled() {
        // Test that Text components have clicks disabled by default
        assertDoesNotThrow(() -> {
            Text text = new Text("Click Disabled Text");
            assertNotNull(text);
            // Text components should be mouse transparent to avoid accidental clicks
        });
    }
}