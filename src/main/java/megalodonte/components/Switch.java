package megalodonte.components;

import javafx.scene.control.ToggleButton;
import megalodonte.base.components.Component;
import megalodonte.base.state.State;
import megalodonte.base.theme.ThemeManager;

import java.util.Objects;
import java.util.function.Consumer;

/** A theme-aware on/off control bound bidirectionally to a boolean state. */
public final class Switch extends Component {
    private final ToggleButton control;
    private Consumer<Boolean> onCheckedChange = ignored -> { };
    private boolean applyingState;

    public Switch(String accessibleLabel, State<Boolean> state) {
        super(new ToggleButton());
        Objects.requireNonNull(state, "state");
        if (accessibleLabel == null || accessibleLabel.isBlank()) {
            throw new IllegalArgumentException("accessibleLabel must not be blank");
        }
        control = (ToggleButton) node;
        control.setAccessibleText(accessibleLabel);
        control.setFocusTraversable(true);
        control.setMinWidth(48);
        control.setPrefWidth(48);
        control.setPrefHeight(26);
        control.selectedProperty().addListener((ignored, oldValue, value) -> {
            if (applyingState) return;
            state.set(value);
            onCheckedChange.accept(value);
        });
        state.subscribe(value -> {
            boolean selected = Boolean.TRUE.equals(value);
            if (control.isSelected() != selected) {
                applyingState = true;
                try { control.setSelected(selected); }
                finally { applyingState = false; }
            }
            var colors = ThemeManager.theme().colors();
            control.setText(selected ? "●" : "○");
            control.setStyle("-fx-background-color: " + (selected ? colors.primary() : colors.secondary())
                    + "; -fx-text-fill: " + colors.surface()
                    + "; -fx-background-radius: 20; -fx-font-size: 18;");
        });
    }

    /** Runs after a user interaction changes the checked state. */
    public Switch onCheckedChange(Consumer<Boolean> handler) {
        onCheckedChange = Objects.requireNonNull(handler, "handler");
        return this;
    }
}
