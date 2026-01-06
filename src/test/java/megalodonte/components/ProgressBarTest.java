package megalodonte.components;

import megalodonte.State;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProgressBarTest {
    
    @Test
    void testProgressState() {
        State<Integer> progressState = State.of(50);
        
        assertEquals(50, progressState.get());
    }
    
    @Test
    void testProgressStateUpdate() {
        State<Integer> progressState = State.of(25);
        
        assertEquals(25, progressState.get());
        
        // Update progress
        progressState.set(75);
        assertEquals(75, progressState.get());
    }
    
    @Test
    void testProgressFullState() {
        State<Integer> fullState = State.of(100);
        assertEquals(100, fullState.get());
    }
}