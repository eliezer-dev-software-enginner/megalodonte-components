package megalodonte.components;

import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import megalodonte.ReadableState;
import megalodonte.State;
import megalodonte.base.components.Component;
import megalodonte.props.ButtonProps;
import megalodonte.props.SelectProps;

import java.lang.reflect.Field;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Function;

public class Checkbox extends Component  {

    private final CheckBox javaFxCheckbox;

    public Checkbox(String label,State<Boolean> state) {
        super(new CheckBox(label));
        this.javaFxCheckbox = (CheckBox) this.node;

        javaFxCheckbox.selectedProperty().addListener((_,_,v)->{
            state.set(v);
        });

        state.subscribe((stateValue)-> javaFxCheckbox.setSelected(stateValue));
    }
}

