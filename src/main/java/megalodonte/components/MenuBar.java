package megalodonte.components;

import javafx.geometry.Insets;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import megalodonte.base.components.Component;
import megalodonte.base.theme.ThemeManager;

import java.util.ArrayList;
import java.util.List;

import static megalodonte.styles.util.StyleUtils.updateBackgroundColor;

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
        updateBackgroundColor(bar, theme.colors().surface());
        bar.setMaxWidth(Double.MAX_VALUE);
    }

    public MenuBar addMenu(Menu menu) {
        this.menus.add(menu);
        this.bar.getChildren().add(menu.getTrigger().getJavaFxNode());
        return this;
    }

    public MenuBar menu(Menu menu) {
        return addMenu(menu);
    }

    /** Sobrescreve o fundo padrão ({@code theme.colors().surface()}) — útil pra combinar com outra área da UI (ex.: sidebar escura). */
    public MenuBar bgColor(String color) {
        updateBackgroundColor(bar, color);
        return this;
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

    /** Docks an arbitrary component (e.g. a theme-toggle icon) at the far right of the bar, after every menu added so far. */
    public MenuBar trailing(Component component) {
        SpacerHorizontal spacer = new SpacerHorizontal().fill();
        HBox.setHgrow(spacer.getJavaFxNode(), Priority.ALWAYS);
        this.bar.getChildren().addAll(spacer.getJavaFxNode(), component.getJavaFxNode());
        return this;
    }

    public static MenuBar of() {
        return new MenuBar();
    }
}
