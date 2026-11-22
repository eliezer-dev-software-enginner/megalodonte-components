package megalodonte.components;

import javafx.scene.control.ToggleButton;
import javafx.scene.control.ContentDisplay;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import megalodonte.base.components.Component;
import megalodonte.base.state.State;
import megalodonte.base.theme.ThemeManager;

import java.util.Objects;
import java.util.function.Consumer;

/** A theme-aware on/off control bound bidirectionally to a boolean state. */
public final class Switch extends Component {
    private final ToggleButton control;
    private final StackPane track = new StackPane();
    private final Circle thumb = new Circle(8);
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
        control.setText(accessibleLabel);
        control.setFocusTraversable(true);
        control.setContentDisplay(ContentDisplay.RIGHT);
        control.setGraphicTextGap(10);
        control.setMinHeight(30);
        track.setMinSize(44, 24);
        track.setPrefSize(44, 24);
        track.setMaxSize(44, 24);
        track.getChildren().add(thumb);
        control.setGraphic(track);
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
            track.setStyle("-fx-background-color: " + (selected ? colors.primary() : colors.border())
                    + "; -fx-background-radius: 999;");
            thumb.setFill(Color.web(selected ? colors.surface() : colors.textSecondary()));
            thumb.setTranslateX(selected ? 10 : -10);
            control.setStyle("-fx-background-color: transparent; -fx-padding: 2 0; -fx-text-fill: "
                    + colors.textPrimary() + "; -fx-cursor: hand;");
        });
    }

    /** Runs after a user interaction changes the checked state. */
    public Switch onCheckedChange(Consumer<Boolean> handler) {
        onCheckedChange = Objects.requireNonNull(handler, "handler");
        return this;
    }
}
