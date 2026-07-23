package megalodonte.components.layout_components;

import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Region;
import megalodonte.base.state.ForEachState;
import megalodonte.base.components.Component;
import megalodonte.props.FlowRowProps;
import megalodonte.props.Props;

import java.util.List;

public class FlowRow extends Component implements LayoutComponent {
    private final FlowPane nodeInternal;

    public FlowRow() {
        this(new FlowRowProps());
    }

    public FlowRow(FlowRowProps props) {
        super(new FlowPane(), props);
        this.nodeInternal = (FlowPane) this.node;
        nodeInternal.setMaxHeight(Region.USE_PREF_SIZE);
    }

    public FlowRow r_child(Component component) {
        this.nodeInternal.getChildren().add(component.getNode());
        return this;
    }

    @Override
    public Props props() {
        return null;
    }

    @Override
    public FlowRow children(Component... components) {
        for (Component c : components) {
            r_child(c);
        }
        return this;
    }

    @Override
    public FlowRow items(List<? extends Component> components) {
        for (Component c : components) {
            r_child(c);
        }
        return this;
    }

    @Override
    public <T, C extends Component> FlowRow items(ForEachState<T, C> forEachState) {
        List<C> componentes = forEachState.getComponents();
        for (C component : componentes) {
            r_child(component);
        }

        forEachState.getState().subscribe(newItems -> {
            this.nodeInternal.getChildren().clear();
            List<C> novosComponentes = forEachState.getComponents();
            for (C component : novosComponentes) {
                r_child(component);
            }
        });

        return this;
    }

    @Override
    public <T, C extends Component> LayoutComponent items(ForEachState<T, C> forEachState, boolean isScrollable) {
        return null;
    }
}