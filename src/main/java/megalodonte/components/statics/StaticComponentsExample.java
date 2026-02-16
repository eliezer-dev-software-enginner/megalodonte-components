package megalodonte.components.statics;

import megalodonte.components.*;
import megalodonte.components.inputs.*;
import megalodonte.props.*;
import megalodonte.State;
import static megalodonte.components.statics.Components.*;

/**
 * Example demonstrating the static factory methods usage.
 * This shows how to create components without using the 'new' keyword.
 * 
 * @author Eliezer
 * @since 1.0.0
 */
public class StaticComponentsExample {

    public static void main(String[] args) {
        // Example 1: Basic usage without 'new'
        Component basicExample = Column()
            .c_child(Text("Hello World"))
            .c_child(Button("Click me"))
            .c_child(SpacerVertical())
            .c_child(Text("Bottom text"));

        // Example 3: With properties
        ColumnProps columnProps = new ColumnProps();
        TextProps titleProps = new TextProps();
        titleProps.bold();
        ButtonProps primaryProps = new ButtonProps();
        
        Component withPropsExample = Column(columnProps)
            .c_child(Text("Title", titleProps))
            .c_child(Button("Submit", primaryProps));

        // Example 4: With state
        State<String> nameState = State.of("John Doe");
        State<String> buttonText = State.of("Click me!");
        
        Component withStateExample = Column()
            .c_child(Text(nameState))
            .c_child(Button(buttonText));

        // Example 5: Form with inputs
        State<String> emailState = State.of("");
        State<String> passwordState = State.of("");
        ButtonProps loginButtonProps = new ButtonProps();
        
        Component formExample = Column()
            .c_child(Text("Login Form"))
            .c_child(Input(emailState))
            .c_child(PasswordInput(passwordState))
            .c_child(Button("Login", loginButtonProps));

        // Example 6: Complex layout
        Component complexExample = Column()
            .c_child(Row()
                .r_child(Text("Username:"))
                .r_child(SpacerHorizontal())
                .r_child(Input(State.of("user123"))))
            .c_child(Row()
                .r_child(Text("Password:"))
                .r_child(SpacerHorizontal())
                .r_child(PasswordInput(State.of(""))))
            .c_child(Row()
                .r_child(SpacerHorizontal())
                .r_child(Button("Login"))
                .r_child(SpacerHorizontal())
                .r_child(Button("Cancel")));

        System.out.println("Static Components Examples created successfully!");
        System.out.println("All examples demonstrate the fluent syntax without 'new' keyword.");
        
        // You can now use these components in your JavaFX application
        // For example: scene.setRoot(basicExample.getNode());
    }
}