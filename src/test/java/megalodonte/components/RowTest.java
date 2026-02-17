package megalodonte.components;

import megalodonte.props.RowProps;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RowTest {
    
    @Test
    void testEmptyRowCreation() {
        Row row = new Row();
        
        assertNotNull(row);
    }
    
    @Test
    void testRowWithProps() {
        RowProps props = new RowProps()
            .spacingOf(15)
            .bottomVertically();
        
        Row row = new Row(props);
        
        assertNotNull(row);
        assertNotNull(props);
    }
    
    @Test
    void testRowWithPropsAndStyler() {
        RowProps props = new RowProps().spacingOf(10);
        RowStyler styler = new RowStyler().bgColor("#f0f0f0");
        
        Row row = new Row(props, styler);
        
        assertNotNull(row);
        assertNotNull(styler);
    }
}