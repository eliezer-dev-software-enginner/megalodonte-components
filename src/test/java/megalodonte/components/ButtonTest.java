package megalodonte.components;

import megalodonte.props.ButtonProps;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import static org.junit.jupiter.api.Assertions.*;

class ButtonTest extends ApplicationTest {
    private StackPane root;

    @Override public void start(Stage stage) {
        root = new StackPane();
        stage.setScene(new Scene(root, 240, 120));
        stage.show();
    }
    
    @Test
    void testButtonPropsCreation() {
        ButtonProps props = new ButtonProps()
            .height(50)
            .fontSize(16)
            .textColor("red");
        
        assertNotNull(props);
    }
    
    @Test
    void testButtonPropsFillWidth() {
        ButtonProps props = new ButtonProps().fillWidth();
        
        assertNotNull(props);
    }
    
    @Test
    void testButtonPropsOnClick() {
        final boolean[] clicked = {false};
        var button = new Button("Click").onClick(() -> clicked[0] = true);
        interact(() -> root.getChildren().add(button.getJavaFxNode()));
        clickOn(button.getJavaFxNode());
        assertTrue(clicked[0]);
    }
}
