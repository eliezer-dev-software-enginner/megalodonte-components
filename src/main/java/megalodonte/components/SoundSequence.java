package megalodonte.components;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import megalodonte.base.components.Component;
import megalodonte.components.v2.HorizontalScroll;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.function.IntConsumer;

/** Audio timeline with overlapping clips in separate lanes. */
public final class SoundSequence extends Component implements AutoCloseable {
    public record Item(double start, double duration, double volume, String label) {}
    private record Parts(Pane lane, HorizontalScroll scroll) {}
    private static Parts create() {
        var lane = new Pane(); lane.setMinHeight(110);
        return new Parts(lane, new HorizontalScroll(Component.CreateFromJavaFxNode(lane)));
    }
    private final Pane lane;
    private final HorizontalScroll scroll;
    private int selected = -1;
    private double minimumHeight = 170;
    private IntConsumer onSelect = index -> {};

    public SoundSequence() { this(create()); }
    private SoundSequence(Parts parts) {
        super(parts.scroll().getJavaFxNode());
        lane = parts.lane(); scroll = parts.scroll();
    }
    public long itemCount() { return lane.getChildren().stream().filter(item -> item.getUserData() instanceof Integer).count(); }
    public double viewportMinimumHeight() { return ((Region) scroll.getJavaFxNode()).getMinHeight(); }

    public void setMinimumHeight(double value) { minimumHeight = value; }
    public void onSelect(IntConsumer handler) { onSelect = handler; }
    public void setItems(List<Item> items) {
        lane.getChildren().clear();
        var ends = new ArrayList<Double>(); double width = 600;
        for (int i = 0; i < items.size(); i++) {
            var item = items.get(i); int index = i;
            double x = 12 + item.start() * 24, w = Math.max(100, item.duration() * 24); int row = 0;
            while (row < ends.size() && ends.get(row) > x) row++;
            if (row == ends.size()) ends.add(0.0); ends.set(row, x + w + 4);
            var label = new Label(item.label()); label.setMaxWidth(w - 12);
            var time = new Label(String.format(Locale.ROOT, "%.1fs → %.1fs · %.0f%%", item.start(), item.start() + item.duration(), item.volume() * 100));
            var card = new VBox(4, time, label); card.setPadding(new Insets(6)); card.setManaged(false);
            card.resizeRelocate(x, 26 + row * 66, w, 60); card.setUserData(index);
            card.setFocusTraversable(true); card.setAccessibleText(label.getText() + " " + time.getText());
            Tooltip.install(card, new Tooltip(label.getText() + "\n" + time.getText()));
            card.setOnMouseClicked(event -> onSelect.accept(index));
            card.setOnKeyPressed(event -> { if (event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.SPACE) { onSelect.accept(index); event.consume(); } });
            lane.getChildren().add(card); width = Math.max(width, x + w + 12);
        }
        for (int second = 0; second * 24 < width; second += 10) {
            var tick = new Label(second + "s"); tick.setManaged(false); tick.resizeRelocate(12 + second * 24, 2, 60, 20); lane.getChildren().add(tick);
        }
        if (items.isEmpty()) { var empty = new Label("Adicione sons para posicioná-los no vídeo"); empty.relocate(12, 32); lane.getChildren().add(empty); }
        double height = Math.max(minimumHeight, 50 + ends.size() * 66);
        lane.setPrefSize(width, height - 14);
        ((Region) scroll.getJavaFxNode()).setMinHeight(height);
        ((Region) scroll.getJavaFxNode()).setPrefHeight(height);
        scroll.refresh(); select(selected);
    }
    public void select(int index) {
        selected = index;
        for (var item : lane.getChildren()) if (item.getUserData() instanceof Integer current)
            item.setStyle("-fx-background-color: " + (index == current ? "#a6e5dc" : "#d7eee9") + "; -fx-border-color: #368c80; -fx-background-radius: 5; -fx-border-radius: 5;");
        for (var item : lane.getChildren()) if (Integer.valueOf(index).equals(item.getUserData())) scroll.reveal(item);
    }
    @Override public void close() { lane.getChildren().clear(); onSelect = index -> {}; }
}
