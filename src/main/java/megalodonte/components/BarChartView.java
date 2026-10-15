package megalodonte.components;

import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import megalodonte.base.components.Component;
import megalodonte.base.state.State;

import java.util.List;
import java.util.Objects;

/** A native bar chart whose values come from framework state. */
public final class BarChartView extends Component {
    public record Bar(String label, double value, String color) {
        public Bar {
            if (label == null || label.isBlank()) throw new IllegalArgumentException("Bar label is blank");
            if (!Double.isFinite(value)) throw new IllegalArgumentException("Bar value must be finite");
        }
    }

    private final BarChart<String, Number> chart;

    public BarChartView(String title, State<List<Bar>> bars) {
        super(new BarChart<String, Number>(new CategoryAxis(), new NumberAxis()));
        chart = (BarChart<String, Number>) node;
        chart.setTitle(Objects.requireNonNull(title, "title"));
        chart.setLegendVisible(false);
        chart.setPrefSize(320, 260);
        Objects.requireNonNull(bars, "bars").subscribe(this::setBars);
    }

    public void setBars(List<Bar> bars) {
        Objects.requireNonNull(bars, "bars");
        var series = new XYChart.Series<String, Number>();
        for (Bar bar : bars) {
            var data = new XYChart.Data<String, Number>(bar.label(), bar.value());
            if (bar.color() != null && !bar.color().isBlank()) {
                data.nodeProperty().addListener((ignored, oldNode, newNode) -> {
                    if (newNode != null) newNode.setStyle("-fx-bar-fill: " + bar.color() + ";");
                });
            }
            series.getData().add(data);
        }
        chart.getData().setAll(series);
    }
}
