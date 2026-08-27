package megalodonte.props;

import javafx.geometry.Insets;
import megalodonte.base.scale.ScaleProvider;
import megalodonte.base.theme.ThemeInterface;

public interface Paddable<T extends Paddable<T>> {

    int UNSET = -1;

    /**
     * Resolves the final {@code Insets} padding for each side independently:
     * <ul>
     *   <li>If the value was set (>= 0), scales with {@link ScaleProvider#scale(int)}.</li>
     *   <li>If the value is {@code UNSET} (-1), uses {@code theme.padding().md()}
     *       (already scaled internally by {@code ThemePadding}).</li>
     * </ul>
     * Each scaling source is applied exactly once — never twice.
     */
    default Insets resolvePadding(ThemeInterface theme, int top, int right, int down, int left) {
        int themeMd = theme.padding().md();

        return new Insets(
                top >= 0 ? ScaleProvider.scale(top) : themeMd,
                right >= 0 ? ScaleProvider.scale(right) : themeMd,
                down >= 0 ? ScaleProvider.scale(down) : themeMd,
                left >= 0 ? ScaleProvider.scale(left) : themeMd
        );
    }
}
