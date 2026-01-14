package megalodonte.components;

import megalodonte.State;
import megalodonte.props.DatePickerProps;
import megalodonte.styles.DatePickerStyler;

import java.time.LocalDate;
import java.util.Locale;

public class DatePicker extends Component {

    public DatePicker() {
        this(new DatePickerProps(), new DatePickerStyler());
    }

    public DatePicker(DatePickerProps props) {
        this(props, new DatePickerStyler());
    }

    public DatePicker(DatePickerProps props, DatePickerStyler styler) {
        super(new javafx.scene.control.DatePicker(LocalDate.now()), props, styler);
    }

    public DatePicker(State<LocalDate> state) {
        this(state, new DatePickerProps(), new DatePickerStyler());
    }

    public DatePicker(State<LocalDate> state, DatePickerProps props) {
        this(state, props, new DatePickerStyler());
    }

    public DatePicker(State<LocalDate> state, DatePickerProps props, DatePickerStyler styler) {
        super(new javafx.scene.control.DatePicker(state.get() != null ? state.get() : LocalDate.now()), props, styler);
        bind(state);
    }

    public DatePicker(LocalDate initialValue) {
        this(initialValue, new DatePickerProps(), new DatePickerStyler());
    }

    public DatePicker(LocalDate initialValue, DatePickerProps props) {
        this(initialValue, props, new DatePickerStyler());
    }

    public DatePicker(LocalDate initialValue, DatePickerProps props, DatePickerStyler styler) {
        super(new javafx.scene.control.DatePicker(initialValue), props, styler);
    }

    @Deprecated(forRemoval = true)
    // Brazilian Portuguese constructor
    public static DatePicker brazilian() {
        return new DatePicker(new DatePickerProps()
            .locale(new Locale("pt", "BR"))
            .pattern("dd/MM/yyyy")
            .editable(false)
            .placeHolder("dd/MM/yyyy"));
    }

    @Deprecated(forRemoval = true)
    // Brazilian Portuguese constructor with state
    public static DatePicker brazilian(State<LocalDate> state) {
        return new DatePicker(state, new DatePickerProps()
            .locale(new Locale("pt", "BR"))
            .pattern("dd/MM/yyyy")
            .editable(false)
            .placeHolder("dd/MM/yyyy"));
    }

    private void bind(State<LocalDate> state) {
        if (state == null) return;

        javafx.scene.control.DatePicker datePicker = (javafx.scene.control.DatePicker) this.node;
        
        // Set initial value
        if (state.get() != null) {
            datePicker.setValue(state.get());
        }

        // Bind state changes to DatePicker
        state.subscribe(date -> {
            if (date != null) {
                datePicker.setValue(date);
            }
        });

        // Bind DatePicker changes to state
        datePicker.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                state.set(newVal);
            }
        });
    }

    private DatePicker(javafx.scene.control.DatePicker datePicker) {
        super(datePicker, null, new DatePickerStyler());
    }

}