package megalodonte.components;

import java.util.ArrayList;
import java.util.List;

public class Menu {
    private final javafx.scene.control.Menu menu;
    private final List<MenuItem> items;

    public Menu(String title) {
        this.menu = new javafx.scene.control.Menu(title);
        this.items = new ArrayList<>();
    }

    public Menu addItem(MenuItem item) {
        this.items.add(item);
        this.menu.getItems().add(item.getJavaFxMenuItem());
        return this;
    }

    public Menu item(MenuItem item) {
        return addItem(item);
    }

    public Menu item(String title, Runnable action) {
        return addItem(new MenuItem(title, action));
    }

    public List<MenuItem> getItems() {
        return new ArrayList<>(items);
    }

    public String getTitle() {
        return this.menu.getText();
    }

    public void setTitle(String title) {
        this.menu.setText(title);
    }

    public javafx.scene.control.Menu getJavaFxMenu() {
        return this.menu;
    }

    public static Menu of(String title) {
        return new Menu(title);
    }
}