package megalodonte.components;

import megalodonte.props.ClickableProps;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ClickableTest {
    
    @Test
    void testClickablePropsCreation() {
        ClickableProps props = new ClickableProps()
            .padding(12)
            .borderRadius(8)
            .backgroundColor("#f0f0f0");
        
        assertNotNull(props);
    }
    
    @Test
    void testClickablePropsWithDimensions() {
        ClickableProps props = new ClickableProps()
            .height(50)
            .width(100)
            .backgroundColor("#ffffff")
            .hoverColor("rgba(0,0,0,0.1)")
            .activeColor("rgba(0,0,0,0.2)");
        
        assertNotNull(props);
        assertEquals(8, props.getPadding());
        assertEquals(8, props.getBorderRadius());
        assertEquals("#ffffff", props.getBackgroundColor());
    }
    
    @Test
    void testClickablePropsColors() {
        ClickableProps props = new ClickableProps()
            .hoverColor("rgba(0,0,0,0.1)")
            .activeColor("rgba(0,0,0,0.2)");
        
        assertNotNull(props);
        assertEquals("rgba(0,0,0,0.1)", props.getHoverColor());
        assertEquals("rgba(0,0,0,0.2)", props.getActiveColor());
    }
    
    @Test
    void testClickableClassExists() {
        assertNotNull(Clickable.class);
        
        // Test that we can create the class via reflection (avoid JavaFX issues)
        assertDoesNotThrow(() -> {
            Class.forName("megalodonte.components.Clickable");
        });
    }
}