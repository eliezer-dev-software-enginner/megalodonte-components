package megalodonte.components;

import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;
import megalodonte.base.components.Component;
import megalodonte.base.state.State;
import megalodonte.base.theme.ThemeManager;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;

/** An accessible, single-choice group bound to a string state. */
public final class RadioGroup extends Component {
    /** One selectable value and its visible label. */
    public record Option(String value, String label) {
        public Option {
            if (value == null || value.isBlank()) throw new IllegalArgumentException("Option value is blank");
            if (label == null || label.isBlank()) throw new IllegalArgumentException("Option label is blank");
        }
    }

    private final ToggleGroup controls = new ToggleGroup();

    public RadioGroup(String accessibleLabel, List<Option> options, State<String> selected) {
        super(new VBox(8));
        if (accessibleLabel == null || accessibleLabel.isBlank()) {
            throw new IllegalArgumentException("accessibleLabel must not be blank");
        }
        Objects.requireNonNull(options, "options");
        Objects.requireNonNull(selected, "selected");
        if (options.isEmpty()) throw new IllegalArgumentException("RadioGroup requires an option");
        var values = new HashSet<String>();
        var box = (VBox) node;
        box.setAccessibleText(accessibleLabel);
        for (Option option : options) {
            if (!values.add(option.value())) throw new IllegalArgumentException("Duplicate option: " + option.value());
            var button = new RadioButton(option.label());
            button.setUserData(option.value());
            button.setToggleGroup(controls);
            button.setAccessibleText(option.label());
            button.setStyle("-fx-mark-color: " + ThemeManager.theme().colors().primary() + ";");
            box.getChildren().add(button);
        }
        controls.selectedToggleProperty().addListener((ignored, oldValue, value) -> {
            if (value != null) selected.set((String) value.getUserData());
        });
        selected.subscribe(value -> {
            if (value == null || value.isBlank()) {
                controls.selectToggle(null);
                return;
            }
            for (var toggle : controls.getToggles()) {
                if (value.equals(toggle.getUserData())) {
                    if (controls.getSelectedToggle() != toggle) controls.selectToggle(toggle);
                    return;
                }
            }
            controls.selectToggle(null);
        });
    }
}
