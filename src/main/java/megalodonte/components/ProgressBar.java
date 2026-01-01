package megalodonte.components;

import megalodonte.State;

public class ProgressBar extends Component {

    private final javafx.scene.control.ProgressBar pb;

    public ProgressBar(State<Integer> progressState) {
        super(new javafx.scene.control.ProgressBar(0));

        this.pb = (javafx.scene.control.ProgressBar) this.node;

        // State → ProgressBar
        progressState.subscribe(value -> {
            double normalized = value / 100.0;
            pb.setProgress(normalized);
        });
    }
}
