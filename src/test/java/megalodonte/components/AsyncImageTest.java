package megalodonte.components;

import megalodonte.base.theme.ThemeManager;
import megalodonte.theme.DefaultTheme;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

final class AsyncImageTest {
    @BeforeEach void theme() { ThemeManager.setTheme(new DefaultTheme()); }

    @Test void rejectsMissingSources() {
        assertThrows(IllegalArgumentException.class, () -> new AsyncImage(" "));
        assertThrows(IllegalArgumentException.class, () -> new AsyncImage(null));
    }
}
