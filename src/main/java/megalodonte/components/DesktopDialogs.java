package megalodonte.components;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import megalodonte.base.components.Component;

/** Native desktop dialogs exposed through the component library. */
public final class DesktopDialogs {
    private DesktopDialogs() { }

    public static void information(String message) {
        new Alert(Alert.AlertType.INFORMATION, message, ButtonType.OK).showAndWait();
    }

    public static boolean confirm(String message) {
        return new Alert(Alert.AlertType.CONFIRMATION, message, ButtonType.CANCEL, ButtonType.OK)
                .showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK;
    }

    public static boolean form(String title, Component content) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle(title);
        dialog.getDialogPane().setContent(content.getJavaFxNode());
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL, ButtonType.OK);
        return dialog.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK;
    }
}
