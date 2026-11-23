package megalodonte.components;

import javafx.scene.chart.PieChart;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import megalodonte.base.theme.ThemeManager;
import megalodonte.base.components.Component;
import megalodonte.base.state.ReadableState;

import java.util.List;
import java.util.Objects;

/** A native pie chart whose slices come from framework state. */
public final class PieChartView extends Component {
    public record Slice(String label, double value) {
        public Slice {
            if (label == null || label.isBlank()) throw new IllegalArgumentException("Slice label is blank");
            if (!Double.isFinite(value) || value < 0) throw new IllegalArgumentException("Slice value must be nonnegative and finite");
        }
    }

    private final PieChart chart;

    public PieChartView(String title) {
        super(new PieChart());
        chart = (PieChart) node;
        chart.setTitle(Objects.requireNonNull(title, "title"));
        chart.setLegendVisible(false);
        chart.setPrefSize(320, 260);
        chart.sceneProperty().addListener((observable, oldScene, newScene) -> {
            if (newScene != null) Platform.runLater(this::applyTheme);
        });
    }

    public PieChartView(String title, ReadableState<List<Slice>> slices) {
        this(title);
        Objects.requireNonNull(slices, "slices").subscribe(this::setSlices);
    }

    public void setSlices(List<Slice> slices) {
        Objects.requireNonNull(slices, "slices");
        chart.getData().setAll(slices.stream().map(slice -> new PieChart.Data(slice.label(), slice.value())).toList());
        if (chart.getScene() != null) Platform.runLater(this::applyTheme);
    }

    private void applyTheme() {
        var colors = ThemeManager.theme().colors();
        chart.setStyle("-fx-background-color:" + colors.surface() + ";");
        Color text = Color.web(colors.textPrimary());
        var title = chart.lookup(".chart-title");
        if (title instanceof Label label) label.setTextFill(text);
        chart.lookupAll(".chart-pie-label").stream().filter(Label.class::isInstance)
                .map(Label.class::cast).forEach(label -> label.setTextFill(text));
    }
}
