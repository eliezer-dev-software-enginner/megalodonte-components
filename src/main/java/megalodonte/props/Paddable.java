package megalodonte.props;

import javafx.geometry.Insets;
import megalodonte.base.scale.ScaleProvider;

public interface Paddable<T extends Paddable<T>> {

    int getPaddingUnitsTop();

    int getPaddingUnitsRight();

    int getPaddingUnitsDown();

    int getPaddingUnitsLeft();

    void setPaddingUnitsTop(int units);

    void setPaddingUnitsRight(int units);

    void setPaddingUnitsDown(int units);

    void setPaddingUnitsLeft(int units);

    @SuppressWarnings("unchecked")
    default T paddingAll(int units) {
        setPaddingUnitsTop(units);
        setPaddingUnitsRight(units);
        setPaddingUnitsDown(units);
        setPaddingUnitsLeft(units);
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    default T paddingTop(int units) {
        setPaddingUnitsTop(units);
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    default T paddingRight(int units) {
        setPaddingUnitsRight(units);
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    default T paddingDown(int units) {
        setPaddingUnitsDown(units);
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    default T paddingLeft(int units) {
        setPaddingUnitsLeft(units);
        return (T) this;
    }

    default Insets toInsets() {
        return new Insets(
                ScaleProvider.scale(getPaddingUnitsTop()),
                ScaleProvider.scale(getPaddingUnitsRight()),
                ScaleProvider.scale(getPaddingUnitsDown()),
                ScaleProvider.scale(getPaddingUnitsLeft())
        );
    }
}
