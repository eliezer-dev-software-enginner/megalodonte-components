package megalodonte.components;


import javafx.scene.layout.Region;
import megalodonte.ReadableState;

import java.util.Objects;

public class SpacerVertical extends Component {
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
        region.setMinHeight(v);
        region.setPrefHeight(v);
        region.setMaxHeight(v);
    }
}
