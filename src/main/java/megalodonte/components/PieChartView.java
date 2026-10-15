package megalodonte.components;

import javafx.scene.chart.PieChart;
import megalodonte.base.components.Component;
import megalodonte.base.state.State;

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
    }

    public PieChartView(String title, State<List<Slice>> slices) {
        this(title);
        Objects.requireNonNull(slices, "slices").subscribe(this::setSlices);
    }

    public void setSlices(List<Slice> slices) {
        Objects.requireNonNull(slices, "slices");
        chart.getData().setAll(slices.stream().map(slice -> new PieChart.Data(slice.label(), slice.value())).toList());
    }
}
