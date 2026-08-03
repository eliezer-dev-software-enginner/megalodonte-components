package megalodonte.props;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import megalodonte.base.scale.ScaleProvider;
import megalodonte.base.theme.ThemeInterface;

import static megalodonte.styles.util.StyleUtils.*;

public class CheckboxProps extends TextComponentProps<CheckboxProps> {

    @Override
    protected void applyTheme(Node node, Props props, ThemeInterface theme) {
        if (!(node instanceof CheckBox checkBox)) return;

        checkBox.setCursor(javafx.scene.Cursor.HAND);

        int finalFontSize = getFontSize() != null ? ScaleProvider.scale(getFontSize()) : theme.typography().body();
        updateFontSize(checkBox, finalFontSize);

        String finalTextColor = getFinalColor(textColor, theme.colors().textPrimary());
        updateTextColor_Input(checkBox, finalTextColor);

        applyBoxStyling(checkBox, theme);
        checkBox.selectedProperty().addListener((obs, old, isSelected) -> applyBoxStyling(checkBox, theme));
    }

    /**
     * O box/mark do CheckBox vivem dentro do skin (não no node raiz), então só dá
     * pra fazer o lookup depois que o skin é instalado — mesmo problema resolvido em
     * DatePicker.applyIcon()/SimpleTableProps.applyHeaderStyling().
     */
    private void applyBoxStyling(CheckBox checkBox, ThemeInterface theme) {
        Runnable apply = () -> {
            var box = checkBox.lookup(".box");
            if (box == null) return;

            String bgColor = checkBox.isSelected() ? theme.colors().primary() : theme.colors().surface();
            updateBackgroundColor(box, bgColor);
            updateBorderColor(box, theme.colors().border());
            updateBorderWidth(box, theme.border().width());
            updateBorderRadius(box, theme.border().radiusSm());

            for (Node mark : box.lookupAll(".mark")) {
                updateBackgroundColor(mark, "white");
            }
        };

        if (checkBox.getSkin() != null) {
            Platform.runLater(apply);
        } else {
            checkBox.skinProperty().addListener((obs, oldSkin, newSkin) -> {
                if (newSkin != null) Platform.runLater(apply);
            });
        }
    }
}
