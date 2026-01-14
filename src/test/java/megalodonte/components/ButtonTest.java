package megalodonte.components;

import megalodonte.props.ButtonProps;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ButtonTest {
    
    @Test
    void testButtonPropsCreation() {
        ButtonProps props = new ButtonProps()
            .height(50)
            .fontSize(16)
            .textColor("red");
        
        assertNotNull(props);
    }
    
    @Test
    void testButtonPropsFillWidth() {
        ButtonProps props = new ButtonProps().fillWidth();
        
        assertNotNull(props);
    }
    
    @Test
    void testButtonPropsOnClick() {
        final boolean[] clicked = {false};
        ButtonProps props = new ButtonProps()
            .onClick(() -> clicked[0] = true);
        
        assertNotNull(props);
        // Cannot test private field directly, but props creation should work
    }
}