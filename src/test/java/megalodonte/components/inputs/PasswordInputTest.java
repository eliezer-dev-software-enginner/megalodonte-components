package megalodonte.components.inputs;

import megalodonte.State;
import megalodonte.props.InputProps;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PasswordInputTest {
    
    @Test
    void testPasswordStateBinding() {
        State<String> testState = State.of("initial");
        
        assertEquals("initial", testState.get());
        
        // Change state programmatically
        testState.set("newpassword");
        assertEquals("newpassword", testState.get());
    }
    
    @Test
    void testPasswordInputProps() {
        InputProps props = new InputProps()
            .height(45)
            .fontSize(16)
            .placeHolder("Enter password");
        
        assertNotNull(props);
    }
}