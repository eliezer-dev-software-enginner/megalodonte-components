package megalodonte.components;

public class MenuItem {
    private final javafx.scene.control.MenuItem menuItem;

    public MenuItem(String title) {
        this.menuItem = new javafx.scene.control.MenuItem(title);
    }

    public MenuItem(String title, Runnable action) {
        this.menuItem = new javafx.scene.control.MenuItem(title);
        setOnAction(action);
    }

    public void setOnAction(Runnable action) {
        this.menuItem.setOnAction(e -> action.run());
    }

    public String getTitle() {
        return this.menuItem.getText();
    }

    public void setTitle(String title) {
        this.menuItem.setText(title);
    }

    public javafx.scene.control.MenuItem getJavaFxMenuItem() {
        return this.menuItem;
    }

    public static MenuItem of(String title) {
        return new MenuItem(title);
    }

    public static MenuItem of(String title, Runnable action) {
        return new MenuItem(title, action);
    }
}