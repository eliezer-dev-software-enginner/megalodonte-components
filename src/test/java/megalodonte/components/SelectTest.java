package megalodonte.components;

import megalodonte.State;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SelectTest {

    private State<List<String>> itemsState;

    @BeforeEach
    void setUp() {
        itemsState = State.of(Arrays.asList("Option 1", "Option 2", "Option 3"));
    }

    @Test
    void stateList_shouldCreateCorrectly() {
        // Then
        assertEquals(3, itemsState.get().size());
        assertTrue(itemsState.get().contains("Option 1"));
        assertTrue(itemsState.get().contains("Option 2"));
        assertTrue(itemsState.get().contains("Option 3"));
    }

    @Test
    void stateList_shouldUpdateWhenSet() {
        // When
        itemsState.set(Arrays.asList("New Option 1", "New Option 2"));
        
        // Then
        assertEquals(2, itemsState.get().size());
        assertTrue(itemsState.get().contains("New Option 1"));
        assertTrue(itemsState.get().contains("New Option 2"));
        assertFalse(itemsState.get().contains("Option 1"));
    }

    @Test
    void stateList_shouldHandleEmptyList() {
        // Given
        assertEquals(3, itemsState.get().size());
        
        // When
        itemsState.set(List.of());
        
        // Then
        assertEquals(0, itemsState.get().size());
    }

    @Test
    void stateList_shouldHandleNullList() {
        // Given
        assertEquals(3, itemsState.get().size());
        
        // When
        itemsState.set(null);
        
        // Then
        assertNull(itemsState.get());
    }

    @Test
    void stateList_shouldWorkWithListOperations() {
        // Given
        assertEquals(3, itemsState.get().size());
        
        // When - add item
        itemsState.add("Option 4");
        
        // Then
        assertEquals(4, itemsState.get().size());
        assertTrue(itemsState.get().contains("Option 4"));
        
        // When - remove item
        itemsState.remove("Option 2");
        
        // Then
        assertEquals(3, itemsState.get().size());
        assertFalse(itemsState.get().contains("Option 2"));
        
        // When - clear list
        itemsState.clear();
        
        // Then
        assertEquals(0, itemsState.get().size());
    }

    @Test
    void stateList_shouldSubscribeToChanges() {
        // Given
        final List<List<String>> capturedValues = new ArrayList<>();
        itemsState.subscribe(capturedValues::add);
        
        // When
        List<String> newList = Arrays.asList("New Item 1", "New Item 2");
        itemsState.set(newList);
        
        // Then
        assertEquals(2, capturedValues.size()); // Initial value + new value
        assertEquals(newList, capturedValues.get(1));
        assertTrue(capturedValues.get(1).contains("New Item 1"));
        assertTrue(capturedValues.get(1).contains("New Item 2"));
    }
}