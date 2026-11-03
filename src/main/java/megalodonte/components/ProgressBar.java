package megalodonte.components;

import megalodonte.base.state.State;
import megalodonte.base.components.Component;
import megalodonte.base.theme.ThemeManager;
import megalodonte.styles.util.StyleUtils;

import java.util.Objects;

/** Progress indicator styled from the active Megalodonte theme. */
public class ProgressBar extends Component {

    private final javafx.scene.control.ProgressBar pb;

    public ProgressBar(State<Integer> progressState) {
        super(new javafx.scene.control.ProgressBar(0));
        Objects.requireNonNull(progressState, "progressState");
        this.pb = (javafx.scene.control.ProgressBar) this.node;
        this.pb.getStylesheets().add(Objects.requireNonNull(
                getClass().getResource("/progress-bar.css"), "progress-bar.css").toExternalForm());
        this.pb.setMaxWidth(Double.MAX_VALUE);

        var theme = Objects.requireNonNull(ThemeManager.theme(), "Megalodonte theme must be set before creating ProgressBar");
        StyleUtils.applyStyleProperty(pb, theme.colors().primary(), "-megalodonte-progress-fill");
        StyleUtils.applyStyleProperty(pb, theme.colors().border(), "-megalodonte-progress-track");
        StyleUtils.applyStyleProperty(pb, theme.border().radiusMd() + "px", "-megalodonte-progress-radius");

        // State → ProgressBar
        progressState.subscribe(value -> {
            double normalized = Math.max(0, Math.min(100, Objects.requireNonNull(value, "progress"))) / 100.0;
            pb.setProgress(normalized);
        });
    }
}
