package megalodonte.components;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import megalodonte.props.ButtonProps;

public class ButtonDemo extends Application {
    
    @Override
    public void start(Stage stage) {
        VBox root = new VBox(10);
        
        // Botão básico
        Button btn1 = new Button("Botão Simples");
        
        // Botão com props e onClick
        Button btn2 = new Button("Botão com Callback", new ButtonProps()
            .primary()
            .onClick(() -> System.out.println("Botão clicado!")));
        
        // Botão success
        Button btn3 = new Button("Botão Success", new ButtonProps()
            .success()
            .onClick(() -> System.out.println("Success!")));
        
        // Botão danger com hover
        Button btn4 = new Button("Botão Danger", new ButtonProps()
            .danger()
            .onClick(() -> System.out.println("Danger clicked!")));
        
        root.getChildren().addAll(
            btn1.getNode(),
            btn2.getNode(), 
            btn3.getNode(),
            btn4.getNode()
        );
        
        Scene scene = new Scene(root, 300, 200);
        stage.setTitle("Button Demo - Hand Cursor & Fade Animation");
        stage.setScene(scene);
        stage.show();
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}