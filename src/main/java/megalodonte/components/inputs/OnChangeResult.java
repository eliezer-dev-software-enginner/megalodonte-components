package megalodonte.components.inputs;

public class OnChangeResult {
    private final String displayValue;
    private final String stateValue;

    public OnChangeResult(String displayValue, String stateValue) {
        this.displayValue = displayValue;
        this.stateValue = stateValue;
    }

    public String getDisplayValue() {
        return displayValue;
    }

    public String getStateValue() {
        return stateValue;
    }

    public static OnChangeResult of(String displayValue, String stateValue) {
        return new OnChangeResult(displayValue, stateValue);
    }

    public static OnChangeResult same(String value) {
        return new OnChangeResult(value, value);
    }
}