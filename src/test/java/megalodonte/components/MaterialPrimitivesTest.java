package megalodonte.components;

import javafx.scene.control.TabPane;
import javafx.scene.control.TitledPane;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.RadioButton;
import javafx.scene.layout.VBox;
import megalodonte.base.state.State;
import megalodonte.base.theme.ThemeManager;
import megalodonte.theme.DefaultTheme;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MaterialPrimitivesTest extends ApplicationTest {
    @BeforeEach void theme() { ThemeManager.setTheme(new DefaultTheme()); }

    @Test void switchSynchronizesStateInBothDirections() {
        var selected = State.of(false);
        var control = new Switch("Notifications", selected);
        var toggle = (ToggleButton) control.getJavaFxNode();
        assertFalse(toggle.isSelected());
        interact(() -> toggle.setSelected(true));
        assertTrue(selected.get());
        interact(() -> selected.set(false));
        assertFalse(toggle.isSelected());
    }

    @Test void sliderBoundsInitialValueAndTracksChanges() {
        var value = State.of(120.0);
        var control = new Slider(0, 100, value);
        var slider = (javafx.scene.control.Slider) control.getJavaFxNode();
        assertEquals(100, slider.getValue());
        interact(() -> slider.setValue(25));
        assertEquals(25, value.get());
    }

    @Test void radioGroupSynchronizesExclusiveChoice() {
        var selected = State.of("weekly");
        var group = new RadioGroup("Billing frequency", List.of(
                new RadioGroup.Option("monthly", "Monthly"),
                new RadioGroup.Option("weekly", "Weekly")), selected);
        var buttons = ((VBox) group.getJavaFxNode()).getChildren();
        var monthly = (RadioButton) buttons.get(0);
        var weekly = (RadioButton) buttons.get(1);
        assertTrue(weekly.isSelected());
        interact(() -> monthly.setSelected(true));
        assertEquals("monthly", selected.get());
        assertFalse(weekly.isSelected());
        interact(() -> selected.set("weekly"));
        assertTrue(weekly.isSelected());
        assertThrows(IllegalArgumentException.class, () -> new RadioGroup("Invalid", List.of(
                new RadioGroup.Option("same", "One"), new RadioGroup.Option("same", "Two")), selected));
    }

    @Test void accordionAndTabsAcceptFrameworkContent() {
        var expanded = State.of(false);
        var accordion = new Accordion("Details", new Text("Content"), expanded);
        assertFalse(((TitledPane) accordion.getJavaFxNode()).isExpanded());
        interact(() -> expanded.set(true));
        assertTrue(((TitledPane) accordion.getJavaFxNode()).isExpanded());

        var selected = State.of(1);
        var tabs = new Tabs(List.of(
                new Tabs.Page("First", new Text("One")),
                new Tabs.Page("Second", new Text("Two"))), selected);
        assertEquals(1, ((TabPane) tabs.getJavaFxNode()).getSelectionModel().getSelectedIndex());
    }
}
