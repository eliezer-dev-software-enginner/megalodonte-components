package megalodonte.components;

import javafx.scene.Scene;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import megalodonte.base.state.State;
import megalodonte.base.theme.ThemeManager;
import megalodonte.theme.DefaultTheme;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ProgressBarThemeTest extends ApplicationTest {
    private State<Integer> progress;
    private javafx.scene.control.ProgressBar control;

    @Override public void start(Stage stage) {
        ThemeManager.setTheme(new DefaultTheme());
        progress = State.of(25);
        control = (javafx.scene.control.ProgressBar) new ProgressBar(progress).getJavaFxNode();
        stage.setScene(new Scene(new StackPane(control), 320, 80));
        stage.show();
    }

    @Test void usesMegalodonteColorsAndTracksState() {
        interact(() -> {
            control.applyCss();
            Region bar = (Region) control.lookup(".bar");
            Region track = (Region) control.lookup(".track");
            assertNotNull(bar);
            assertNotNull(track);
            assertEquals(Color.web(ThemeManager.theme().colors().primary()),
                    bar.getBackground().getFills().getFirst().getFill());
            assertEquals(Color.web(ThemeManager.theme().colors().border()),
                    track.getBackground().getFills().getFirst().getFill());
            assertEquals(0.25, control.getProgress());
            progress.set(75);
            assertEquals(0.75, control.getProgress());
        });
    }
}
