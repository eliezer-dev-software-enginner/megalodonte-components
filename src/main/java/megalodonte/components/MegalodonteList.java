package megalodonte.components;

import javafx.scene.layout.VBox;
import megalodonte.props.ListProps;
import megalodonte.ReadableState;
import megalodonte.State;
import megalodonte.ComputedState;
import megalodonte.styles.ListStyler;
import megalodonte.theme.Theme;
import megalodonte.theme.ThemeManager;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class MegalodonteList<T> extends Component {
    private final VBox container;
    private State<String> searchState;
    private ComputedState<List<T>> filteredItems;
    private final ListStyler<T> styler;
    private final ListItemRenderer<T> renderer;

    public MegalodonteList(ListProps<T> props, ListItemRenderer<T> renderer) {
        super(new VBox(), props, new ListStyler<>());
        this.container = (VBox) this.node;
        this.styler = new ListStyler<>();
        this.renderer = renderer;
        this.searchState = new State<>("");
        
        setupFiltering(props);
        render();
    }

    public MegalodonteList(ListProps<T> props, ListItemRenderer<T> renderer, ListStyler<T> styler) {
        super(new VBox(), props, styler);
        this.container = (VBox) this.node;
        this.styler = styler;
        this.renderer = renderer;
        this.searchState = new State<>("");
        
        setupFiltering(props);
        render();
    }

    private void setupFiltering(ListProps<T> props) {
        // Configurar busca se necessário
        if (props.getSearchTerm() != null) {
            searchState = (State<String>) props.getSearchTerm();
        }

        // Criar computed state para itens filtrados
        final ListProps<T> finalProps = props;
        final State<String> finalSearchState = searchState;
        
        this.filteredItems = ComputedState.of(() -> {
            List<T> items = finalProps.getItems();
            String searchValue = finalSearchState.get().toLowerCase().trim();
            
            if (searchValue.isEmpty()) {
                return items;
            }

            return items.stream()
                    .filter(item -> {
                        String itemString = item.toString().toLowerCase();
                        return itemString.contains(searchValue);
                    })
                    .toList();
        }, finalSearchState);

        // Re-render quando itens filtrados mudarem
        this.filteredItems.subscribe(items -> render());
    }

    private void render() {
        ListProps<T> props = (ListProps<T>) this.props;
        List<T> items = filteredItems.get();
        Theme theme = ThemeManager.theme();

        // Limpar container
        container.getChildren().clear();

        // Renderizar campo de busca se necessário
        if (props.isShowSearch()) {
            Component searchField = createSearchField(theme);
            container.getChildren().add(searchField.getNode());
        }

        // Renderizar cabeçalho se necessário
        if (props.isShowHeader()) {
            Component header = renderer.renderHeader();
            if (header != null) {
                container.getChildren().add(header.getNode());
            }
        }

        // Renderizar itens
        if (items.isEmpty()) {
            Component emptyState = createEmptyState(props.getEmptyMessage(), theme);
            container.getChildren().add(emptyState.getNode());
        } else {
            for (int i = 0; i < items.size(); i++) {
                T item = items.get(i);
                Component row = renderer.render(item, i, props);
                container.getChildren().add(row.getNode());
            }
        }
    }

    private Component createSearchField(Theme theme) {
        return new Text("🔍 Pesquisar: ");
    }

    private Component createEmptyState(String message, Theme theme) {
        return new Column(new megalodonte.ColumnProps()
                .centerHorizontally()
                .paddingAll(32)
                .spacingOf(16))
                .c_child(new Text("📋"))
                .c_child(new Text(message));
    }
}