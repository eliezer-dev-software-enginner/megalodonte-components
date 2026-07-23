package megalodonte.props;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import megalodonte.base.scale.ScaleProvider;
import megalodonte.base.theme.ThemeInterface;

import java.util.Objects;

import static megalodonte.styles.util.StyleUtils.applyBackgroundStyling;

public class ColumnProps extends LayoutProps<ColumnProps> {
    private boolean fillHeight;

    private enum Alignment {CENTER_HORIZONTALLY, CENTER_VERTICALLY}
    private Alignment alignment;

    public ColumnProps centerHorizontally() {
        alignment = Alignment.CENTER_HORIZONTALLY;
        return this;
    }

    public ColumnProps centerVertically() {
        alignment = Alignment.CENTER_VERTICALLY;
        return this;
    }

    public ColumnProps fillHeight(){
        fillHeight = true;
        return this;
    }

    @Override
    protected void applyTheme(Node node, Props props, ThemeInterface theme) {
        if (node instanceof VBox vBox) {
            applyBaseLayout(node);

            if (spacingUnits > 0) {
                vBox.setSpacing(ScaleProvider.scale(spacingUnits));
            }

            if (alignment != null) {
                if (alignment.equals(Alignment.CENTER_HORIZONTALLY)) {
                    vBox.setAlignment(Pos.TOP_CENTER);
                    vBox.setFillWidth(false);
                }
                if (alignment.equals(Alignment.CENTER_VERTICALLY)) {
                    vBox.setAlignment(Pos.CENTER);
                }
            }

            if(fillHeight){
                VBox.setVgrow(node, Priority.ALWAYS);
            }

           //byte type = (byte) (bgImage != null? 1 : 0);
            // Apply all common theme-aware styling
           // applyBackgroundStyling(node, theme,  bgImage != null? bgImage : bgColor, type);
        }
    }
}
