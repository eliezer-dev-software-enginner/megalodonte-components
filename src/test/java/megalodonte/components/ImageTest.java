package megalodonte.components;

import megalodonte.State;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ImageTest {
    
    @Test
    void testImagePathState() {
        State<String> imageState = State.of("/images/test.png");
        
        assertEquals("/images/test.png", imageState.get());
    }
    
    @Test
    void testImageStateUpdate() {
        State<String> imageState = State.of("/images/initial.png");
        
        assertEquals("/images/initial.png", imageState.get());
        
        // Update image path
        imageState.set("/images/updated.png");
        assertEquals("/images/updated.png", imageState.get());
    }
    
    @Test
    void testImageEmptyPathState() {
        State<String> emptyState = State.of("");
        assertEquals("", emptyState.get());
    }
    
    @Test
    void testImageNullPathState() {
        State<String> nullState = State.of(null);
        assertNull(nullState.get());
    }
}