package megalodonte.components;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import megalodonte.base.components.Component;
import megalodonte.base.theme.ThemeManager;

import java.util.Objects;

/** Native desktop dialogs exposed through the component library. */
public final class DesktopDialogs {
    private DesktopDialogs() { }

    public static void information(String message) {
        Alert dialog = new Alert(Alert.AlertType.INFORMATION, message, ButtonType.OK);
        dialog.setTitle("Mensagem");
        dialog.setHeaderText(null);
        dialog.setGraphic(null);
        style(dialog.getDialogPane());
        dialog.showAndWait();
    }

    public static boolean confirm(String message) {
        Alert dialog = new Alert(Alert.AlertType.CONFIRMATION, message, ButtonType.CANCEL, ButtonType.OK);
        dialog.setTitle("Confirmação");
        dialog.setHeaderText(null);
        dialog.setGraphic(null);
        style(dialog.getDialogPane());
        return dialog.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK;
    }

    public static boolean form(String title, Component content) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle(title);
        dialog.getDialogPane().setContent(content.getJavaFxNode());
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL, ButtonType.OK);
        style(dialog.getDialogPane());
        return dialog.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK;
    }

    private static void style(DialogPane pane) {
        var stylesheet = Objects.requireNonNull(DesktopDialogs.class.getResource("/desktop-dialogs.css"));
        pane.getStylesheets().add(stylesheet.toExternalForm());
        var theme = ThemeManager.theme();
        if (theme == null) return;
        var colors = theme.colors();
        pane.setStyle("-dialog-surface: " + colors.surface() + ";"
                + "-dialog-text: " + colors.textPrimary() + ";"
                + "-dialog-secondary: " + colors.textSecondary() + ";"
                + "-dialog-primary: " + colors.primary() + ";"
                + "-dialog-border: " + colors.border() + ";"
                + "-dialog-hover: " + colors.hover() + ";"
                + "-fx-font-size: " + theme.typography().body() + "px;"
                + "-fx-background-radius: " + theme.border().radiusMd() + "px;"
                + "-fx-border-radius: " + theme.border().radiusMd() + "px;");
    }
}
