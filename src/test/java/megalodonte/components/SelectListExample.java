package megalodonte.components;

import megalodonte.State;
import java.util.Arrays;
import java.util.List;

/**
 * Example demonstrating the new State<List<T>> support in Select component.
 * 
 * This example shows how to bind a Select component to a reactive State<List<T>>,
 * allowing automatic updates when the list changes.
 */
public class SelectListExample {
    
    public static void main(String[] args) {
        // Example 1: Basic usage with State<List<String>>
        State<List<String>> options = State.of(Arrays.asList(
            "Option 1", "Option 2", "Option 3"
        ));
        
        Select<String> select = new Select<>();
        select.items(options); // Bind to reactive state
        
        // When the state changes, the Select component updates automatically
        options.add("Option 4");
        options.remove("Option 2");
        
        // Example 2: With custom display text
        State<List<User>> users = State.of(Arrays.asList(
            new User(1, "John Doe"),
            new User(2, "Jane Smith"),
            new User(3, "Bob Johnson")
        ));
        
        Select<User> userSelect = new Select<>();
        userSelect.items(users)
                .displayText(User::getName)
                .value(State.of(users.get().get(0)));
        
        // Example 3: Dynamic list updates
        State<List<String>> dynamicOptions = State.of(Arrays.asList());
        
        Select<String> dynamicSelect = new Select<>();
        dynamicSelect.items(dynamicOptions);
        
        // Simulate dynamic loading
        dynamicOptions.set(Arrays.asList("Loaded Option 1", "Loaded Option 2"));
        dynamicOptions.add("Loaded Option 3");
    }
    
    static class User {
        private final int id;
        private final String name;
        
        public User(int id, String name) {
            this.id = id;
            this.name = name;
        }
        
        public String getName() {
            return name;
        }
        
        public int getId() {
            return id;
        }
        
        @Override
        public String toString() {
            return "User{id=" + id + ", name='" + name + "'}";
        }
    }
}