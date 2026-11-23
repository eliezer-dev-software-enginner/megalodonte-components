package megalodonte.components;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

final class SelectFilteringTest {
    private record Option(String code, String name) { }

    @Test void filtersByDisplayTextWithoutReplacingTypedOptions() {
        var phone = new Option("1", "Phone");
        var options = List.of(phone, new Option("2", "Case"), new Option("3", "Charger"));

        var filtered = Select.filteredItems(options, "PHO", Option::name);

        assertEquals(List.of(phone), filtered);
        assertEquals(options, Select.filteredItems(options, "", Option::name));
    }
}
