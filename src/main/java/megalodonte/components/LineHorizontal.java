package megalodonte.components;


import javafx.geometry.Orientation;
import javafx.scene.control.Separator;

public class LineHorizontal extends Component {
    public LineHorizontal(){
        super(new Separator());
        var separator = (Separator) this.node;

        applyAttributes(separator);
    }

    private void applyAttributes(Separator separator) {
        separator.setOrientation(Orientation.HORIZONTAL);
    }
}
