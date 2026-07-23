package megalodonte.components;

import javafx.geometry.Insets;
import javafx.scene.layout.HBox;
import megalodonte.base.components.Component;
import megalodonte.base.theme.ThemeManager;

import java.util.ArrayList;
import java.util.List;

public class MenuBar extends Component {
    private final HBox bar;
    private final List<Menu> menus;

    public MenuBar() {
        super(new HBox());
        this.bar = (HBox) this.node;
        this.menus = new ArrayList<>();

        var theme = ThemeManager.theme();
        bar.setSpacing(theme.spacing().sm());
        bar.setPadding(new Insets(
                theme.spacing().sm(), theme.spacing().md(),
                theme.spacing().sm(), theme.spacing().md()
        ));
        bar.setStyle("-fx-background-color: " + theme.colors().surface() + ";");
        bar.setMaxWidth(Double.MAX_VALUE);
    }

    public MenuBar addMenu(Menu menu) {
        this.menus.add(menu);
        this.bar.getChildren().add(menu.getTrigger().getNode());
        return this;
    }

    public MenuBar menu(Menu menu) {
        return addMenu(menu);
    }

    public MenuBar menu(String title, MenuItem... items) {
        Menu menu = new Menu(title);
        for (MenuItem item : items) {
            menu.addItem(item);
        }
        return addMenu(menu);
    }

    public List<Menu> getMenus() {
        return new ArrayList<>(menus);
    }

    public static MenuBar of() {
        return new MenuBar();
    }
}
