package megalodonte.props;

import javafx.geometry.Insets;
import megalodonte.base.scale.ScaleProvider;
import megalodonte.base.theme.ThemeInterface;

public interface Paddable<T extends Paddable<T>> {

    int UNSET = -1;

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

    /**
     * Resolves the final {@code Insets} padding for each side independently:
     * <ul>
     *   <li>If the value was set (>= 0), scales with {@link ScaleProvider#scale(int)}.</li>
     *   <li>If the value is {@code UNSET} (-1), uses {@code theme.padding().md()}
     *       (already scaled internally by {@code ThemePadding}).</li>
     * </ul>
     * Each scaling source is applied exactly once — never twice.
     */
    default Insets resolvePadding(ThemeInterface theme) {
        int top = getPaddingUnitsTop();
        int right = getPaddingUnitsRight();
        int down = getPaddingUnitsDown();
        int left = getPaddingUnitsLeft();

        int themeMd = theme.padding().md();

        return new Insets(
                top >= 0 ? ScaleProvider.scale(top) : themeMd,
                right >= 0 ? ScaleProvider.scale(right) : themeMd,
                down >= 0 ? ScaleProvider.scale(down) : themeMd,
                left >= 0 ? ScaleProvider.scale(left) : themeMd
        );
    }
}
