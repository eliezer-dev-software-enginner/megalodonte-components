package megalodonte.components.inputs;

import megalodonte.State;
import megalodonte.props.InputProps;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InputTest {
    
    private State<String> testState;

    @BeforeEach
    void setUp() {
        testState = State.of("initial");
    }
    
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

    @Test
    void onChangeResult_shouldCreateWithDisplayAndStateValues() {
        OnChangeResult result = OnChangeResult.of("R$ 1,23", "123");
        
        assertEquals("R$ 1,23", result.getDisplayValue());
        assertEquals("123", result.getStateValue());
    }

    @Test
    void onChangeResult_shouldCreateSameValue() {
        OnChangeResult result = OnChangeResult.same("test");
        
        assertEquals("test", result.getDisplayValue());
        assertEquals("test", result.getStateValue());
    }

    @Test
    void lockCursorToEnd_shouldEnableCursorLock() {
        Input input = new Input(testState);
        
        // Testa se o método chain funciona
        InputBase lockedInput = input.lockCursorToEnd();
        
        // Verifica se é a mesma instância (chain pattern)
        assertSame(input, lockedInput);
        
        // TODO: Implementar método isCursorLockedToEnd() no Input
        // assertTrue(input.isCursorLockedToEnd());
    }
}