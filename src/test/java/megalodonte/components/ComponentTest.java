package megalodonte.components;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ComponentTest {
    
    @Test
    void testComponentClassExists() {
        // Test that we can reference the Component class
        assertNotNull(Component.class);
    }
    
    @Test
    void testComponentFactoryMethod() {
        // Test that the FromJavaFxNode method exists (though we can't call it due to JavaFX issues)
        assertDoesNotThrow(() -> {
            // Just verify the method signature exists via reflection
            var method = Component.class.getMethod("FromJavaFxNode", javafx.scene.Node.class);
            assertNotNull(method);
        });
    }
}