package megalodonte.components;


import javafx.scene.layout.Region;
import megalodonte.base.scale.ScaleProvider;
import megalodonte.base.state.ReadableState;

import java.util.Objects;
import megalodonte.base.components.Component;

public class SpacerVertical extends Component  {
    private final Region region;
    public SpacerVertical(){
        super(new Region());
        this.region = (Region) this.node;
    }

    public SpacerVertical(int spacingUnits){
        super(new Region());
        this.region = (Region) this.node;
        applyHeight(spacingUnits);
    }

    public SpacerVertical(ReadableState<Integer> spacingUnitsState){
        super(new Region());
        this.region = (Region) this.node;

        Objects.requireNonNull(spacingUnitsState);

        spacingUnitsState.subscribe(this::applyHeight);
    }

    public SpacerVertical fill() {
        region.setMaxHeight(Double.MAX_VALUE);
        return this;
    }

    private void applyHeight(int v) {
        double scaled = ScaleProvider.scale(v);
        region.setMinHeight(scaled);
        region.setPrefHeight(scaled);
        region.setMaxHeight(scaled);
    }
}
