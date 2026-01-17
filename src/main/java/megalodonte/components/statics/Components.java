package megalodonte.components.statics;

import megalodonte.components.*;
import megalodonte.components.inputs.*;
import megalodonte.props.*;
import megalodonte.styles.*;
import megalodonte.State;

/**
 * Static factory methods for creating components without using 'new' keyword.
 * This class provides convenient methods to instantiate common components
 * with a more fluid syntax.
 * 
 * <h2>Example Usage:</h2>
 * <pre>{@code
 * import static megalodonte.components.statics.Components.*;
 * 
 * // Instead of: new Column().c_child(new Button("Click me"));
 * // You can write:
 * Column().c_child(Button("Click me"));
 * 
 * // Or with text:
 * Column().c_child(Text("Hello World"));
 * 
 * // With properties:
 * Column(ColumnProps.of().spacing(10))
 *     .c_child(Button("Submit", ButtonProps.of().primary()));
 * }</pre>
 * 
 * @author Eliezer
 * @since 1.0.0
 */
public final class Components {

    private Components() {
        // Utility class - prevent instantiation
    }

    /**
     * Creates a new Text component.
     * 
     * @param textContent the text content
     * @return a new Text instance
     */
    public static Text Text(String textContent) {
        return new Text(textContent);
    }

    /**
     * Creates a new Text component with properties.
     * 
     * @param textContent the text content
     * @param props the text properties
     * @return a new Text instance
     */
    public static Text Text(String textContent, TextProps props) {
        return new Text(textContent, props);
    }

    /**
     * Creates a new Text component bound to state.
     * 
     * @param state the readable state containing text content
     * @return a new Text instance
     */
    public static Text Text(megalodonte.ReadableState<String> state) {
        return new Text(state);
    }

    /**
     * Creates a new Button component.
     * 
     * @param textContent the button text
     * @return a new Button instance
     */
    public static Button Button(String textContent) {
        return new Button(textContent);
    }

    /**
     * Creates a new Button component with properties.
     * 
     * @param textContent the button text
     * @param props the button properties
     * @return a new Button instance
     */
    public static Button Button(String textContent, ButtonProps props) {
        return new Button(textContent, props);
    }

    /**
     * Creates a new Button component bound to state.
     * 
     * @param state the readable state containing button text
     * @return a new Button instance
     */
    public static Button Button(megalodonte.ReadableState<String> state) {
        return new Button(state);
    }

    /**
     * Creates a new Column component.
     * 
     * @return a new Column instance
     */
    public static Column Column() {
        return new Column();
    }

    /**
     * Creates a new Column component with properties.
     * 
     * @param props the column properties
     * @return a new Column instance
     */
    public static Column Column(ColumnProps props) {
        return new Column(props);
    }

    /**
     * Creates a new Row component.
     * 
     * @return a new Row instance
     */
    public static Row Row() {
        return new Row();
    }

    /**
     * Creates a new Row component with properties.
     * 
     * @param props the row properties
     * @return a new Row instance
     */
    public static Row Row(RowProps props) {
        return new Row(props);
    }

    /**
     * Creates a new SpacerVertical component.
     * 
     * @return a new SpacerVertical instance
     */
    public static SpacerVertical SpacerVertical() {
        return new SpacerVertical();
    }

    /**
     * Creates a new SpacerHorizontal component.
     * 
     * @return a new SpacerHorizontal instance
     */
    public static SpacerHorizontal SpacerHorizontal() {
        return new SpacerHorizontal();
    }

    /**
     * Creates a new Card component.
     * 
     * @param content the component to wrap in the card
     * @return a new Card instance
     */
    public static Card Card(Component content) {
        return new Card(content);
    }

    /**
     * Creates a new Card component with properties.
     * 
     * @param content the component to wrap in the card
     * @param props the card properties
     * @return a new Card instance
     */
    public static Card Card(Component content, CardProps props) {
        return new Card(content, props);
    }

    /**
     * Creates a new Image component.
     * 
     * @param imagePath the path to the image file
     * @return a new Image instance
     */
    public static Image Image(String imagePath) {
        return new Image(imagePath);
    }

    /**
     * Creates a new Image component with properties.
     * 
     * @param imagePath the path to the image file
     * @param props the image properties
     * @return a new Image instance
     */
    public static Image Image(String imagePath, ImageProps props) {
        return new Image(imagePath, props);
    }

    /**
     * Creates a new Input component.
     * 
     * @param value the initial input value
     * @return a new Input instance
     */
    public static Input Input(String value) {
        return new Input(value);
    }

    /**
     * Creates a new Input component with state.
     * 
     * @param state the state to bind to the input
     * @return a new Input instance
     */
    public static Input Input(State<String> state) {
        return new Input(state);
    }

    /**
     * Creates a new Input component with state and properties.
     * 
     * @param state the state to bind to the input
     * @param props the input properties
     * @return a new Input instance
     */
    public static Input Input(State<String> state, InputProps props) {
        return new Input(state, props);
    }

    /**
     * Creates a new PasswordInput component with state.
     * 
     * @param state the state to bind to the password input
     * @return a new PasswordInput instance
     */
    public static PasswordInput PasswordInput(State<String> state) {
        return new PasswordInput(state, new InputProps());
    }

    /**
     * Creates a new PasswordInput component with state and properties.
     * 
     * @param state the state to bind to the password input
     * @param props the input properties
     * @return a new PasswordInput instance
     */
    public static PasswordInput PasswordInput(State<String> state, InputProps props) {
        return new PasswordInput(state, props);
    }

    /**
     * Creates a new TextAreaInput component with state.
     * 
     * @param state the state to bind to the text area
     * @return a new TextAreaInput instance
     */
    public static TextAreaInput TextAreaInput(State<String> state) {
        return new TextAreaInput(state, new InputProps());
    }

    /**
     * Creates a new TextAreaInput component with state and properties.
     * 
     * @param state the state to bind to the text area
     * @param props the input properties
     * @return a new TextAreaInput instance
     */
    public static TextAreaInput TextAreaInput(State<String> state, InputProps props) {
        return new TextAreaInput(state, props);
    }

    /**
     * Creates a new ProgressBar component.
     * 
     * @param progressState the state containing progress value (0-100)
     * @return a new ProgressBar instance
     */
    public static ProgressBar ProgressBar(State<Integer> progressState) {
        return new ProgressBar(progressState);
    }

    /**
     * Creates a new Select component.
     * 
     * @param <T> the type of items
     * @return a new Select instance
     */
    public static <T> Select<T> Select() {
        return new Select<>();
    }

    /**
     * Creates a new Select component with properties.
     * 
     * @param <T> the type of items
     * @param props the select properties
     * @return a new Select instance
     */
    public static <T> Select<T> Select(SelectProps props) {
        return new Select<>(props);
    }

    /**
     * Creates a new DatePicker component.
     * 
     * @return a new DatePicker instance
     */
    public static DatePicker DatePicker() {
        return new DatePicker();
    }

    /**
     * Creates a new DatePicker component with properties.
     * 
     * @param props the date picker properties
     * @return a new DatePicker instance
     */
    public static DatePicker DatePicker(DatePickerProps props) {
        return new DatePicker(props);
    }
}