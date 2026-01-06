package megalodonte.components;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SpacerTest {
    
    @Test
    void testSpacerVertical() {
        int size = 10;
        SpacerVertical spacer = new SpacerVertical(size);
        
        assertNotNull(spacer);
    }
    
    @Test
    void testSpacerHorizontal() {
        int size = 15;
        SpacerHorizontal spacer = new SpacerHorizontal(size);
        
        assertNotNull(spacer);
    }
    
    @Test
    void testSpacerZeroSize() {
        SpacerVertical spacer = new SpacerVertical(0);
        assertNotNull(spacer);
        
        SpacerHorizontal spacerH = new SpacerHorizontal(0);
        assertNotNull(spacerH);
    }
}