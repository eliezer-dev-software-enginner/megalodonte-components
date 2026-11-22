package megalodonte.components;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertTrue;

final class DarkThemeCssContractTest {
    @Test void tableStylesCellsAndUnusedAreaWithThemeTokens() throws IOException {
        try (var resource = getClass().getResourceAsStream("/simple-table.css")) {
            assertTrue(resource != null, "simple-table.css must be available");
            String css = new String(resource.readAllBytes(), StandardCharsets.UTF_8);
            assertTrue(css.contains("-megalodonte-table-background"));
            assertTrue(css.contains("-megalodonte-table-text"));
            assertTrue(css.contains(".table-row-cell:empty"));
            assertTrue(css.contains(".table-cell"));
        }
    }
}
