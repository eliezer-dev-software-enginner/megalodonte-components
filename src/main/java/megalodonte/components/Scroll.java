package megalodonte.components;

import javafx.event.Event;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import megalodonte.base.components.Component;

import static megalodonte.styles.util.StyleUtils.applyStyleProperty;
import static megalodonte.styles.util.StyleUtils.updateBackgroundColor;
import static megalodonte.styles.util.StyleUtils.updateBorderColor;

public class Scroll extends Component  {

    private final ScrollPane scrollPane;

    public Scroll(Component component) {
        //super(new ScrollPane(), new CardProps(), new CardStyler());
        super(new ScrollPane(), null);
        this.scrollPane = (ScrollPane) node;

        updateBackgroundColor(scrollPane, "transparent");
        updateBorderColor(scrollPane, "transparent");
        // Modena's ".scroll-pane .viewport" rule paints its own opaque
        // background (-fx-control-inner-background, white by default),
        // independent of the ScrollPane's own -fx-background-color above - so
        // without this, the ScrollPane's content area stays hardcoded white/light
        // regardless of theme no matter what color the ScrollPane control itself
        // is given. "-fx-background" is the looked-up color several of Modena's
        // built-in skins (including the viewport) fall back to; making the skin
        // apply it here is override, not a lucky reference. transparentizeViewport
        // additionally targets ".viewport" directly once the skin exists, in case
        // a given JavaFX version's viewport rule doesn't chain through it.
        applyStyleProperty(scrollPane, "transparent", "-fx-background");
        transparentizeViewport(scrollPane);
        VBox.setVgrow(scrollPane, Priority.ALWAYS);
        this.scrollPane.setFitToWidth(true);
        this.scrollPane.setContent(component.getJavaFxNode());
        confineScrollEvents(scrollPane);
    }

    private static void transparentizeViewport(ScrollPane scrollPane) {
        scrollPane.skinProperty().addListener((obs, oldSkin, newSkin) -> applyViewportBackground(scrollPane));
        applyViewportBackground(scrollPane);
    }

    private static void applyViewportBackground(ScrollPane scrollPane) {
        Node viewport = scrollPane.lookup(".viewport");
        if (viewport != null) {
            applyStyleProperty(viewport, "transparent", "-fx-background-color");
        }
    }

    /**
     * Impede que o scroll de {@code scrollableNode} vaze pra um ScrollPane ancestral
     * (ex: uma lista/tabela dentro de um form que já está dentro do scroll da página).
     * Por padrão, quando um ScrollPane/TableView já rolou tudo que dava — seja por
     * estar no limite, seja porque uma rolada forte ultrapassou numa passada só o que
     * sobrava de conteúdo — o restante do evento continua se propagando pros pais, que
     * então também rolam. addEventHandler (não addEventFilter) roda depois do
     * comportamento padrão do próprio controle já ter processado o scroll, então só
     * precisamos consumir o evento aqui pra travar a propagação, sem reimplementar a
     * lógica de rolagem.
     */
    public static void confineScrollEvents(Node scrollableNode) {
        scrollableNode.addEventHandler(ScrollEvent.SCROLL, Event::consume);
    }


//    public Scroll props(CardProps props) {
//        super(new ScrollPane(), props, new CardStyler());
//        this.scrollPane = (ScrollPane) node;
//        defaultConfig();
//
//        return this;
//    }

//    public Scroll(Component content, CardProps props, CardStyler styler) {
//        super(new StackPane(), props, styler);
//        this.container = (StackPane) node;
//        impedirCrescimentoAutomaticoDoFilho();
//
//        this.container.getChildren().add(content.getNode());
//    }

}
