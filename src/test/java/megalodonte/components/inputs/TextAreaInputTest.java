package megalodonte.components.inputs;

import megalodonte.props.InputProps;
import megalodonte.State;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TextAreaInputTest {
    
    @Test
    void testTextAreaStateBinding() {
        State<String> testState = State.of("initial");
        
        assertEquals("initial", testState.get());
        
        // Change state programmatically
        testState.set("updated text");
        assertEquals("updated text", testState.get());
    }
    
    @Test
    void testTextAreaMultilineState() {
        State<String> multilineState = State.of("Line 1\nLine 2");
        
        assertEquals("Line 1\nLine 2", multilineState.get());
    }
    
    @Test
    void testTextAreaInputProps() {
        InputProps props = new InputProps()
            .height(100)
            .fontSize(14)
            .placeHolder("Enter multiline text");
        
        assertNotNull(props);
    }
}