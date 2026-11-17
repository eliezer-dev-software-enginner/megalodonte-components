package megalodonte.components;

import javafx.scene.layout.HBox;
import megalodonte.base.state.ForEachState;
import megalodonte.base.state.ReadableState;
import megalodonte.components.layout_components.Row;
import megalodonte.props.RowProps;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
        
        Row row = new Row(props);
        
        assertNotNull(row);
    }

    @Test
    @SuppressWarnings("unchecked")
    void reactiveItemsAcceptSpacing() {
        ForEachState<String, Text> items = mock(ForEachState.class);
        ReadableState<List<String>> state = mock(ReadableState.class);
        when(items.getComponents()).thenReturn(List.of(new Text("A"), new Text("B")));
        when(items.getState()).thenReturn(state);

        Row row = new Row().items(items, 10);

        HBox outer = (HBox) row.getJavaFxNode();
        HBox reactiveItems = (HBox) outer.getChildren().getFirst();
        assertEquals(10, reactiveItems.getSpacing());
        assertEquals(2, reactiveItems.getChildren().size());
    }
}
