package megalodonte.components.layout_components;

import megalodonte.ForEachState;
import megalodonte.props.Props;
import megalodonte.base.components.Component;

public interface LayoutComponent {
    Props props();
    LayoutComponent children(Component... components);
    <T, C extends Component > LayoutComponent items(ForEachState<T, C> forEachState);
    <T, C extends Component > LayoutComponent items(ForEachState<T, C> forEachState, boolean isScrollable);
}
