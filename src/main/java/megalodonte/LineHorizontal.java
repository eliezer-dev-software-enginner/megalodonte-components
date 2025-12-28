package megalodonte;


import javafx.geometry.Orientation;
import javafx.scene.control.Separator;
import javafx.scene.layout.Region;

import java.util.Objects;

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
