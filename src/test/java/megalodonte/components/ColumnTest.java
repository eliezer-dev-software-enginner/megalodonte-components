package megalodonte.components;

import megalodonte.ColumnProps;
import megalodonte.ColumnStyler;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ColumnTest {
    
    @Test
    void testEmptyColumnCreation() {
        Column column = new Column();
        
        assertNotNull(column);
    }
    
    @Test
    void testColumnWithProps() {
        ColumnProps props = new ColumnProps()
            .spacingOf(10)
            .paddingAll(20)
            .centerHorizontally();
        
        Column column = new Column(props);
        
        assertNotNull(column);
    }
    
    @Test
    void testColumnWithPropsAndStyler() {
        ColumnProps props = new ColumnProps().spacingOf(5);
        ColumnStyler styler = new ColumnStyler().bgColor("#ffffff");
        
        Column column = new Column(props, styler);
        
        assertNotNull(column);
        assertNotNull(props);
        assertNotNull(styler);
    }
}