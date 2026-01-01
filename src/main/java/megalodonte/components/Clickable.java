package megalodonte.components;

import javafx.scene.Node;

import java.util.function.Consumer;

public class Clickable extends Component {

    public Clickable(Component component) {
        super(component.getNode());
        bind();
    }

    private Runnable runnable;
    private Consumer<Exception> errorCallback;

    private void bind() {
        Node node = getNode();

        node.setOnMouseClicked(event -> {
            if (runnable == null) return;

            try {
                runnable.run();
            } catch (Exception e) {
                if (errorCallback != null) {
                    errorCallback.accept(e);
                } else {
                    throw e;
                }
            }
        });
    }

    public Clickable onClick(Runnable runnable) {
        this.runnable = runnable;
        return this;
    }

    public Clickable onClick(Runnable runnable, Consumer<Exception> errorCallback) {
        this.runnable = runnable;
        this.errorCallback = errorCallback;
        return this;
    }
}
