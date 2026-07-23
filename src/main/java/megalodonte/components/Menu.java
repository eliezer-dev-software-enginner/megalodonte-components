package megalodonte.components;

import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.layout.Region;
import javafx.stage.Popup;
import megalodonte.base.components.Component;
import megalodonte.base.theme.ThemeInterface;
import megalodonte.base.theme.ThemeManager;
import megalodonte.components.layout_components.Column;
import megalodonte.components.layout_components.Container;
import megalodonte.props.ColumnProps;
import megalodonte.props.ContainerProps;
import megalodonte.props.TextProps;

import java.util.ArrayList;
import java.util.List;

/**
 * Menu suspenso (dropdown) próprio, sem depender de javafx.scene.control.Menu —
 * o menu nativo usa Label internamente com uma área de clique que às vezes não
 * cobre a linha inteira (é preciso acertar o texto). Aqui cada linha é um
 * Clickable de bounds cheios (StackPane com pickOnBounds), e tudo é construído
 * com os componentes temáticos da lib (Container/Card/Text), então segue o tema
 * da aplicação em vez do look nativo do SO.
 */
public class Menu {
    private static Popup currentlyOpen;

    private final List<MenuItem> items = new ArrayList<>();
    private final javafx.scene.text.Text triggerLabel;
    private final Clickable trigger;
    private final Popup popup = new Popup();

    public Menu(String title) {
        var theme = ThemeManager.theme();

        Text label = new Text(title, new TextProps().textColor(theme.colors().textPrimary()).bold());
        this.triggerLabel = (javafx.scene.text.Text) label.getNode();

        var triggerContainer = new Container(new ContainerProps().paddingAll(8)).children(label);
        addHoverHighlight(triggerContainer.getNode(), theme);

        this.trigger = new Clickable(triggerContainer, this::toggle);

        popup.setAutoHide(true);
        popup.setOnAutoHide(e -> {
            if (currentlyOpen == popup) currentlyOpen = null;
        });
    }

    public Menu addItem(MenuItem item) {
        items.add(item);
        return this;
    }

    public Menu item(MenuItem item) {
        return addItem(item);
    }

    public Menu item(String title, Runnable action) {
        return addItem(new MenuItem(title, action));
    }

    public List<MenuItem> getItems() {
        return new ArrayList<>(items);
    }

    public String getTitle() {
        return triggerLabel.getText();
    }

    public void setTitle(String title) {
        triggerLabel.setText(title);
    }

    Component getTrigger() {
        return trigger;
    }

    private void toggle() {
        if (popup.isShowing()) {
            popup.hide();
            return;
        }
        if (currentlyOpen != null && currentlyOpen.isShowing()) {
            currentlyOpen.hide();
        }

        popup.getContent().setAll(buildDropdown().getNode());

        Node triggerNode = trigger.getNode();
        Point2D screenPoint = triggerNode.localToScreen(0, triggerNode.getBoundsInLocal().getHeight());
        popup.show(triggerNode, screenPoint.getX(), screenPoint.getY());
        currentlyOpen = popup;
    }

    private Component buildDropdown() {
        var theme = ThemeManager.theme();
        var column = new Column(new ColumnProps());

        if (items.isEmpty()) {
            column.c_child(new Container(new ContainerProps().paddingAll(12))
                    .children(new Text("Sem opções", new TextProps().textColor(theme.colors().textSecondary()))));
        } else {
            for (MenuItem item : items) {
                column.c_child(menuItemRow(item, theme));
            }
        }

        // Não usa Card aqui de propósito: CardProps liga um DropShadow que troca de
        // raio (8→16) no hover, e como o Popup recalcula o próprio tamanho a cada
        // layout, isso fazia a janela do popup "pulsar"/reposicionar a cada
        // passada de mouse. Aqui a sombra é fixa (não reage a hover).
        var wrapper = new Container(new ContainerProps().paddingAll(4)).children(column);
        var wrapperNode = wrapper.getNode();
        wrapperNode.setStyle(
                "-fx-background-color: " + theme.colors().surface() + ";"
                        + " -fx-background-radius: " + theme.border().radiusMd() + ";"
                        + " -fx-border-color: " + theme.colors().border() + ";"
                        + " -fx-border-width: 1px;"
                        + " -fx-border-radius: " + theme.border().radiusMd() + ";"
        );
        wrapperNode.setEffect(new javafx.scene.effect.DropShadow(8, javafx.scene.paint.Color.rgb(0, 0, 0, 0.2)));

        return wrapper;
    }

    private Component menuItemRow(MenuItem item, ThemeInterface theme) {
        var itemContainer = new Container(new ContainerProps().paddingAll(8))
                .children(new Text(item.getTitle(), new TextProps().textColor(theme.colors().textPrimary())));
        addHoverHighlight(itemContainer.getNode(), theme);

        var clickable = new Clickable(itemContainer, () -> {
            popup.hide();
            item.run();
        });
        // Clickable trava o próprio node em USE_PREF_SIZE (pra não crescer sem
        // querer em outros usos) — aqui a gente quer o oposto: a linha precisa
        // esticar até a largura da Column, senão o fundo do hover só cobre o
        // texto em vez da linha inteira.
        ((Region) clickable.getNode()).setMaxWidth(Double.MAX_VALUE);
        return clickable;
    }

    private void addHoverHighlight(Node node, ThemeInterface theme) {
        node.setOnMouseEntered(e -> node.setStyle("-fx-background-color: " + theme.colors().hover() + ";"));
        node.setOnMouseExited(e -> node.setStyle("-fx-background-color: transparent;"));
    }

    public static Menu of(String title) {
        return new Menu(title);
    }
}
