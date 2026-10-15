package megalodonte.components;

import javafx.scene.control.Label;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import megalodonte.base.components.Component;
import java.nio.file.Files;
import java.nio.file.Path;

/** Local image preview with a centered 16:9 viewport. */
public final class ImagePreview extends Component implements AutoCloseable {
    private final ImageView image = new ImageView();
    private final Canvas particles = new Canvas();
    private final Label placeholder = new Label("Selecione uma cena e importe uma imagem");
    private final StackPane frame = new StackPane(image, placeholder, particles);

    public ImagePreview() {
        super(new Pane() {
            @Override protected void layoutChildren() {
                super.layoutChildren();
                StackPane frame = (StackPane) getChildren().getFirst();
                double width = Math.min(getWidth(), getHeight() * 16.0 / 9);
                double height = width * 9 / 16;
                frame.resizeRelocate((getWidth()-width)/2, (getHeight()-height)/2, width, height);
            }
        });
        Pane pane = (Pane) node;
        frame.setId("videoFrame"); frame.setManaged(false);
        frame.setStyle("-fx-background-color: #000000;");
        pane.setStyle("-fx-background-color: #151b28; -fx-background-radius: 8;");
        pane.getChildren().add(frame);
        placeholder.setStyle("-fx-text-fill: #c9d4e6;");
        image.setPreserveRatio(true);
        image.fitWidthProperty().bind(frame.widthProperty());
        image.fitHeightProperty().bind(frame.heightProperty());
        particles.widthProperty().bind(frame.widthProperty());
        particles.heightProperty().bind(frame.heightProperty());
        particles.setMouseTransparent(true);
        particles.widthProperty().addListener((observable, before, after) -> paintParticles());
        particles.heightProperty().addListener((observable, before, after) -> paintParticles());
    }

    public void setSource(String source) {
        var loaded = LocalImages.load(source, 1600, 900);
        image.setImage(loaded);
        placeholder.setVisible(loaded == null);
    }

    private String visualEffect = "";
    private int sceneIndex;
    private double sceneTime;

    /** Draw the same sparse, deterministic drifting-light cue used by the exporter. */
    public void setVisualEffect(String effect, int sceneIndex, double sceneTime) {
        this.visualEffect = effect == null ? "" : effect;
        this.sceneIndex = sceneIndex;
        this.sceneTime = Math.max(0, sceneTime);
        paintParticles();
    }

    private void paintParticles() {
        double width=particles.getWidth(), height=particles.getHeight();
        if(visualEffect.equals("shake") && image.getImage()!=null) {
            double frame=sceneTime*30;
            image.setScaleX(1.018);image.setScaleY(1.018);
            image.setTranslateX(Math.sin(frame*.31)*width/240);
            image.setTranslateY(Math.cos(frame*.27)*height/240);
        } else {
            image.setScaleX(1);image.setScaleY(1);image.setTranslateX(0);image.setTranslateY(0);
        }
        var graphics=particles.getGraphicsContext2D();graphics.clearRect(0,0,width,height);
        if(!visualEffect.equals("particles") || width<=0 || height<=0 || image.getImage()==null) return;
        var random=new java.util.Random(0x5eedL+sceneIndex);
        for(int i=0;i<26;i++) {
            double x=sceneIndex==0 ? (i%2==0 ? random.nextInt(480) : 1440+random.nextInt(480)) : random.nextInt(1920);
            double y=random.nextInt(1080),speed=8+random.nextInt(20),size=14+random.nextInt(11);
            x=(x+speed*sceneTime)%1920*width/1920;
            y=(((y-speed*sceneTime)%1080+1080)%1080)*height/1080;
            double radius=Math.max(2,size*width/1920);
            graphics.setFill(Color.rgb(255,244,220,.68));
            graphics.fillOval(x,y,radius,radius);
        }
    }

    public double viewportAspectRatio() { return frame.getWidth() / frame.getHeight(); }

    public boolean hasImage() { return image.getImage() != null; }

    @Override public void close() {
        image.fitWidthProperty().unbind();
        image.fitHeightProperty().unbind();
        particles.widthProperty().unbind();particles.heightProperty().unbind();
        image.setImage(null);
    }
}
