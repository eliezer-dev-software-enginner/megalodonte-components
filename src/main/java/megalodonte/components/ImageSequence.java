package megalodonte.components;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import megalodonte.base.components.Component;
import megalodonte.components.v2.HorizontalScroll;
import java.util.List;
import java.util.Locale;
import java.util.function.IntConsumer;

/** Scene timeline; data and selection are supplied by the caller. */
public final class ImageSequence extends Component implements AutoCloseable {
    public record Item(double duration, String source, String label) {}
    private final HBox cards;
    private final HorizontalScroll scroll;
    private List<Item> items = List.of();
    private int selected = -1;
    private IntConsumer onSelect = index -> {};

    private record Parts(HBox cards, HorizontalScroll scroll) {}
    private static Parts create() {
        var cards = new HBox(8);
        return new Parts(cards, new HorizontalScroll(Component.CreateFromJavaFxNode(cards)));
    }
    public ImageSequence() { this(create()); }
    private ImageSequence(Parts parts) {
        super(parts.scroll().getJavaFxNode());
        cards = parts.cards(); scroll = parts.scroll();
        cards.setPadding(new Insets(12));
    }

    public void setItems(List<Item> items) {
        this.items = List.copyOf(items);
        cards.getChildren().clear();
        double start = 0;
        for (int i = 0; i < items.size(); i++) {
            Item item = items.get(i); int index = i;
            var thumb = new ImageView(LocalImages.load(item.source(), 200, 90));
            thumb.setFitWidth(120); thumb.setFitHeight(65); thumb.setPreserveRatio(true);
            var label = new Label((i + 1) + " · " + item.label()); label.setWrapText(true); label.setMaxWidth(130);
            var card = new VBox(6, new Label(String.format(Locale.ROOT, "%.1fs → %.1fs", start, start + item.duration())), thumb, label);
            card.setPadding(new Insets(8)); card.setPrefWidth(Math.max(150, Math.min(280, item.duration() * 25))); card.setMinWidth(150);
            card.setFocusTraversable(true); card.setAccessibleText(label.getText());
            card.setOnMouseClicked(event -> onSelect.accept(index));
            card.setOnKeyPressed(event -> { if (event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.SPACE) { onSelect.accept(index); event.consume(); } });
            cards.getChildren().add(card); start += item.duration();
        }
        scroll.refresh(); select(selected);
    }

    public void scrollTo(double pixels) { scroll.scrollTo(pixels); }
    public double getScrollX() { return scroll.getScrollX(); }

    public int itemCount() { return items.size(); }

    public void onSelect(IntConsumer handler) { onSelect = handler; }
    public void select(int index) {
        selected = index;
        for (int i = 0; i < cards.getChildren().size(); i++) cards.getChildren().get(i).setStyle(
            "-fx-background-color: " + (index == i ? "#c7ddff" : "#e5eaf2") + "; -fx-border-color: " + (index == i ? "#367bef" : "#bcc6d5") + "; -fx-border-radius: 6; -fx-background-radius: 6;");
        if (index >= 0 && index < cards.getChildren().size()) scroll.reveal(cards.getChildren().get(index));
    }
    @Override public void close() { cards.getChildren().clear(); onSelect = index -> {}; }
}
