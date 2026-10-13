package megalodonte.components;

import megalodonte.base.components.Component;
import megalodonte.base.state.State;
import megalodonte.base.theme.ThemeManager;

import java.util.Objects;

/** Numeric range control bound bidirectionally to a double state. */
public final class Slider extends Component {
    private final javafx.scene.control.Slider control;

    public Slider(double min, double max, State<Double> state) {
        super(new javafx.scene.control.Slider(min, max, min));
        Objects.requireNonNull(state, "state");
        if (!Double.isFinite(min) || !Double.isFinite(max) || max <= min) {
            throw new IllegalArgumentException("Slider requires a finite ascending range");
        }
        control = (javafx.scene.control.Slider) node;
        control.setStyle("-fx-accent: " + ThemeManager.theme().colors().primary() + ";");
        control.valueProperty().addListener((ignored, oldValue, value) -> state.set(value.doubleValue()));
        state.subscribe(value -> {
            double bounded = Math.max(min, Math.min(max, Objects.requireNonNull(value, "value")));
            if (Double.compare(control.getValue(), bounded) != 0) control.setValue(bounded);
        });
    }
}
