package megalodonte.components;

import javafx.scene.layout.Region;
import megalodonte.ReadableState;

import java.util.Objects;

public class SpacerHorizontal extends Component {

    private final Region region;

    public SpacerHorizontal(){
        super(new Region());
        this.region = (Region) this.node;
    }

    public SpacerHorizontal(int widthUnits){
        super(new Region());
        this.region = (Region) this.node;
        applyWidth(widthUnits);
    }

    public SpacerHorizontal(ReadableState<Integer> widthUnitsState){
        super(new Region());
        this.region = (Region) this.node;

        Objects.requireNonNull(widthUnitsState);

        widthUnitsState.subscribe(v -> applyWidth(v));
    }

    private void applyWidth(int v) {
        region.setMinWidth(v);
        region.setPrefWidth(v);
        region.setMaxWidth(v);
    }

    /** intenção: ocupar toda a largura disponível */
    //o pai deve permitir
    public SpacerHorizontal fill() {
        region.setMaxWidth(Double.MAX_VALUE);
        return this;
    }
}
