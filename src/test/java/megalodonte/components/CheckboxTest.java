package megalodonte.components;

import megalodonte.props.CheckboxProps;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CheckboxTest {

    @Test
    void testCheckboxPropsCreation() {
        CheckboxProps props = new CheckboxProps()
                .fontSize(14)
                .textColor("red");

        assertNotNull(props);
    }

    @Test
    void testCheckboxPropsBold() {
        CheckboxProps props = new CheckboxProps().bold();

        assertNotNull(props);
    }
}
