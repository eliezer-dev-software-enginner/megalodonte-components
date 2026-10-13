package megalodonte.components;

import javafx.scene.control.TitledPane;
import megalodonte.base.components.Component;
import megalodonte.base.state.State;
import megalodonte.base.theme.ThemeManager;

import java.util.Objects;

/** Expandable section with a framework component as its content. */
public final class Accordion extends Component {
    private final TitledPane control;

    public Accordion(String title, Component content, State<Boolean> expanded) {
        super(new TitledPane());
        control = (TitledPane) node;
        control.setText(Objects.requireNonNull(title, "title"));
        control.setContent(Objects.requireNonNull(content, "content").getJavaFxNode());
        control.setAnimated(false);
        control.setStyle("-fx-text-fill: " + ThemeManager.theme().colors().textPrimary() + ";");
        if (expanded != null) {
            control.expandedProperty().addListener((ignored, oldValue, value) -> expanded.set(value));
            expanded.subscribe(value -> {
                boolean open = Boolean.TRUE.equals(value);
                if (control.isExpanded() != open) control.setExpanded(open);
            });
        }
    }
}
