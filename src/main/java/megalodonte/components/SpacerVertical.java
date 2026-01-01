package megalodonte.components;


import javafx.scene.layout.Region;
import megalodonte.ReadableState;

import java.util.Objects;

public class SpacerVertical extends Component {

    public SpacerVertical(int spacingUnits){
        super(new Region());
        Region region = (Region) this.node;
        applySpacing(region, spacingUnits);
    }

    public SpacerVertical(ReadableState<Integer> spacingUnitsState){
        super(new Region());
        Region region = (Region) this.node;

        Objects.requireNonNull(spacingUnitsState);

        spacingUnitsState.subscribe(v-> applySpacing(region, spacingUnitsState.get()));
    }

    private void applySpacing(Region region, int v) {
        region.setMinHeight(v);
        region.setPrefHeight(v);
        region.setMaxHeight(v);
    }
}
