package megalodonte.components;

import megalodonte.base.components.IconInterface;

public class MenuItem {
    private String title;
    private final Runnable action;
    private final IconInterface icon;


    public MenuItem(String title) {
        this(title, () -> {});
    }

    public MenuItem(String title, Runnable action) {
        this(null,title,action);
    }

    public MenuItem(IconInterface icon, String title, Runnable action) {
        this.icon = icon;
        this.title = title;
        this.action = action;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void run() {
        action.run();
    }

    public static MenuItem of(String title) {
        return new MenuItem(title);
    }

    public static MenuItem of(String title, Runnable action) {
        return new MenuItem(title, action);
    }

    public IconInterface getIcon() {
        return icon;
    }
}
