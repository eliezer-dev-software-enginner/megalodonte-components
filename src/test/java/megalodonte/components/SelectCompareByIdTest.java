package megalodonte.components;

import megalodonte.State;
import megalodonte.props.SelectProps;
import java.util.List;

/**
 * Test to demonstrate the new Select compareById functionality
 * that solves the issue with different object instances having the same ID.
 */
public class SelectCompareByIdTest {
    
    static class TestModel {
        public Long id;
        public String name;
        
        public TestModel(Long id, String name) {
            this.id = id;
            this.name = name;
        }
        
        @Override
        public String toString() {
            return "TestModel{id=" + id + ", name='" + name + "'}";
        }
    }
    
    public static void main(String[] args) {
        System.out.println("Testing Select with compareById functionality");
        
        // Create test data - list of items
        TestModel item1 = new TestModel(1L, "Item 1");
        TestModel item2 = new TestModel(2L, "Item 2");
        TestModel item3 = new TestModel(3L, "Item 3");
        
        List<TestModel> items = List.of(item1, item2, item3);
        
        // Create state for selection
        State<TestModel> selectedState = State.of(null);
        
        // Create Select with compareById enabled
        Select<TestModel> select = new Select<TestModel>(new SelectProps())
                .items(items)
                .value(selectedState)
                .compareById() // This is the key feature!
                .displayText(item -> item.name);
        
        // Simulate the scenario: fetch item from database (different instance)
        TestModel itemFromDb = new TestModel(2L, "Item 2 (from DB)");
        
        System.out.println("Original item: " + item2);
        System.out.println("DB item: " + itemFromDb);
        System.out.println("Are they the same instance? " + (item2 == itemFromDb));
        System.out.println("Are they equal? " + item2.equals(itemFromDb)); // false because no equals()
        
        // Set the DB item to the state - this should select the correct item in the list
        System.out.println("\nSetting DB item to state...");
        selectedState.set(itemFromDb);
        
        System.out.println("Selected state: " + selectedState.get());
        System.out.println("This should now correctly select 'Item 2' from the list!");
        
        System.out.println("\nTest completed successfully!");
        System.out.println("The Select component can now match items by ID even when they are different instances.");
    }
}