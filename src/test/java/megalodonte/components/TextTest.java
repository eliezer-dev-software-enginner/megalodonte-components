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
}