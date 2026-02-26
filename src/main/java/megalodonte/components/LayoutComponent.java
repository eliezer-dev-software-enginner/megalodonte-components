package megalodonte.components;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import megalodonte.ForEachState;
import megalodonte.ReadableState;
import megalodonte.props.ButtonProps;
import megalodonte.props.ColumnProps;
import megalodonte.props.Props;
import megalodonte.props.RowProps;

import java.util.List;

public interface LayoutComponent {
    Props props();
    LayoutComponent children(Component... components);
    <T, C extends Component> LayoutComponent items(ForEachState<T, C> forEachState);
}
