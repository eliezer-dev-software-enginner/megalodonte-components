package megalodonte.components;

import javafx.scene.layout.Region;
import megalodonte.base.scale.ScaleProvider;
import megalodonte.base.state.ReadableState;

import java.util.Objects;
import megalodonte.base.components.Component;
public class SpacerHorizontal extends Component  {

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
        double scaled = ScaleProvider.scale(v);
        region.setMinWidth(scaled);
        region.setPrefWidth(scaled);
        region.setMaxWidth(scaled);
    }

    /** intenção: ocupar toda a largura disponível */
    //o pai deve permitir
    public SpacerHorizontal fill() {
        region.setMaxWidth(Double.MAX_VALUE);
        return this;
    }
}
