package megalodonte.components;

import java.util.ArrayList;
import java.util.List;

public class MenuBar extends Component {
    private final javafx.scene.control.MenuBar menuBar;
    private final List<Menu> menus;

    public MenuBar() {
        super(new javafx.scene.control.MenuBar(), null, null);
        this.menuBar = (javafx.scene.control.MenuBar) this.node;
        this.menus = new ArrayList<>();
    }

    public MenuBar addMenu(Menu menu) {
        this.menus.add(menu);
        this.menuBar.getMenus().add(menu.getJavaFxMenu());
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

    public javafx.scene.control.MenuBar getJavaFxMenuBar() {
        return this.menuBar;
    }

    public static MenuBar of() {
        return new MenuBar();
    }
}