package megalodonte.components;

import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import megalodonte.base.components.Component;
import megalodonte.base.state.State;

import java.util.List;
import java.util.Objects;

/** Accessible tab navigation with framework component pages. */
public final class Tabs extends Component {
    public record Page(String title, Component content) {
        public Page {
            if (title == null || title.isBlank()) throw new IllegalArgumentException("Tab title must not be blank");
            Objects.requireNonNull(content, "content");
        }
    }

    private final TabPane control;

    public Tabs(List<Page> pages, State<Integer> selected) {
        super(new TabPane());
        Objects.requireNonNull(pages, "pages");
        if (pages.isEmpty()) throw new IllegalArgumentException("Tabs requires at least one page");
        control = (TabPane) node;
        for (Page page : pages) {
            var tab = new Tab(page.title(), page.content().getJavaFxNode());
            tab.setClosable(false);
            control.getTabs().add(tab);
        }
        if (selected != null) {
            control.getSelectionModel().selectedIndexProperty().addListener(
                    (ignored, oldValue, value) -> selected.set(value.intValue()));
            selected.subscribe(index -> {
                if (index != null && index >= 0 && index < pages.size()
                        && control.getSelectionModel().getSelectedIndex() != index) {
                    control.getSelectionModel().select(index);
                }
            });
        }
    }
}
