package megalodonte.props;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.layout.Region;

public class CardProps extends Props {

    private int padding = 12;
    private int radius = 8;

    private int height;

    public CardProps height(int height){
        this.height = height;
        return this;
    }


    public CardProps padding(int value) {
        this.padding = value;
        return this;
    }

    public CardProps radius(int value) {
        this.radius = value;
        return this;
    }




    @Override
    public void apply(Node node) {
        if (!(node instanceof Region r)) return;

        r.setPadding(new Insets(padding));

        if(height > 0){
            r.setPrefHeight(height);
            r.setMinHeight(height);
            r.setMaxHeight(height);
        }
        r.setStyle(
                "-fx-background-radius: " + radius + ";" +
                        "-fx-background-color: white;"
        );


    }
}