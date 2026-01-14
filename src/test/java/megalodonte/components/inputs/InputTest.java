package megalodonte.components.inputs;

import megalodonte.State;
import megalodonte.props.InputProps;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InputTest {
    
    @Test
    void testStateCreation() {
        State<String> testState = State.of("initial");
        assertEquals("initial", testState.get());
    }
    
    @Test
    void testStateUpdate() {
        State<String> testState = State.of("initial");
        testState.set("updated");
        assertEquals("updated", testState.get());
    }
    
    @Test
    void testInputPropsCreation() {
        InputProps props = new InputProps()
            .height(45)
            .fontSize(18)
            .placeHolder("Enter text");
        
        assertNotNull(props);
    }
    
    @Test
    void testEmptyState() {
        State<String> emptyState = State.of("");
        assertEquals("", emptyState.get());
    }
}