package megalodonte.props;

import javafx.scene.Node;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import megalodonte.base.scale.ScaleProvider;
import megalodonte.base.theme.ThemeInterface;
import megalodonte.styles.util.Utils;

import static megalodonte.styles.util.StyleUtils.*;

public class SimpleTableProps extends Props {

    protected String bgColor;
    protected String headerBgColor;
    protected String headerTextColor;
    protected String borderColor;
    protected int borderWidth;
    protected int borderRadius;
    protected boolean striped = true;
    protected String rowEvenColor;
    protected String rowOddColor;
    protected String rowHoverColor;
    protected String selectionColor;
    protected String focusColor;
    protected String rowTextColor;
    protected String separatorColor;
    protected int headerHeight;
    protected Double maxWidth;

    public SimpleTableProps() {}

    public SimpleTableProps maxWidth(double maxWidth) { this.maxWidth = maxWidth; return this; }

    public SimpleTableProps bgColor(String bgColor) { this.bgColor = bgColor; return this; }
    public SimpleTableProps headerBgColor(String headerBgColor) { this.headerBgColor = headerBgColor; return this; }
    public SimpleTableProps headerTextColor(String headerTextColor) { this.headerTextColor = headerTextColor; return this; }
    public SimpleTableProps borderColor(String borderColor) { this.borderColor = borderColor; return this; }
    public SimpleTableProps borderWidth(int borderWidth) { this.borderWidth = borderWidth; return this; }
    public SimpleTableProps borderRadius(int borderRadius) { this.borderRadius = borderRadius; return this; }
    public SimpleTableProps striped(boolean striped) { this.striped = striped; return this; }
    public SimpleTableProps rowEvenColor(String rowEvenColor) { this.rowEvenColor = rowEvenColor; return this; }
    public SimpleTableProps rowOddColor(String rowOddColor) { this.rowOddColor = rowOddColor; return this; }
    public SimpleTableProps rowHoverColor(String rowHoverColor) { this.rowHoverColor = rowHoverColor; return this; }
    public SimpleTableProps selectionColor(String selectionColor) { this.selectionColor = selectionColor; return this; }
    public SimpleTableProps focusColor(String focusColor) { this.focusColor = focusColor; return this; }
    public SimpleTableProps rowTextColor(String rowTextColor) { this.rowTextColor = rowTextColor; return this; }
    public SimpleTableProps separatorColor(String separatorColor) { this.separatorColor = separatorColor; return this; }
    public SimpleTableProps headerHeight(int headerHeight) { this.headerHeight = headerHeight; return this; }

    @Override
    @SuppressWarnings("unchecked")
    protected void applyTheme(Node node, Props props, ThemeInterface theme) {
        if (!(node instanceof TableView<?> tv)) return;

        TableView<Object> tableView = (TableView<Object>) tv;

        applyContainerStyling(tableView, theme);
        applyRowFactory(tableView, theme);
        applyHeaderStyling(tableView, theme);
    }

    private void applyContainerStyling(TableView<?> tableView, ThemeInterface theme) {
        String finalBgColor = getFinalBackgroundColor(theme, bgColor);
        int finalBorderRadius = borderRadius > 0
                ? ScaleProvider.scale(borderRadius)
                : theme.border().radiusMd();
        int fontSize = theme.typography().body();

        Utils.updateBackgroundColor(tableView, finalBgColor);
        Utils.updateBorderRadius(tableView, finalBorderRadius);
        Utils.updateFontSize(tableView, fontSize);


        if (maxWidth != null) {
            tableView.setMaxWidth(ScaleProvider.scale(maxWidth));
        }

        if (borderWidth > 0) {
            String finalBorderColor = getFinalBorderColor(theme, borderColor);
            Utils.updateBorderColor(tableView, finalBorderColor);
            Utils.updateBorderWidth(tableView, ScaleProvider.scale(borderWidth));
        } else {
            Utils.updateBorderColor(tableView, "transparent");
        }
    }

    @SuppressWarnings("unchecked")
    private void applyRowFactory(TableView<?> tableView, ThemeInterface theme) {
        String textColor = rowTextColor != null
                ? rowTextColor : theme.colors().textPrimary();
        String hoverColor = rowHoverColor != null
                ? rowHoverColor : theme.colors().hover();
        String evenColor = rowEvenColor != null
                ? rowEvenColor : theme.colors().surface();
        String oddColor = rowOddColor != null
                ? rowOddColor : theme.colors().background();
        String separator = separatorColor != null
                ? separatorColor : theme.colors().border();
        String selection = selectionColor != null
                ? selectionColor : theme.colors().selection();

        ((TableView<Object>) tableView).setRowFactory(tv -> {
            TableRow<Object> row = new TableRow<>();

            row.hoverProperty().addListener((obs, old, isHover) -> refreshRowStyle(row, evenColor, oddColor, textColor, separator, hoverColor, selection));
            row.selectedProperty().addListener((obs, old, isSelected) -> refreshRowStyle(row, evenColor, oddColor, textColor, separator, hoverColor, selection));
            row.indexProperty().addListener((obs, old, idx) -> refreshRowStyle(row, evenColor, oddColor, textColor, separator, hoverColor, selection));

            refreshRowStyle(row, evenColor, oddColor, textColor, separator, hoverColor, selection);
            return row;
        });
    }

    private void refreshRowStyle(TableRow<?> row, String evenColor, String oddColor,
                                  String textColor, String separator, String hoverColor, String selectionColor) {
        if (row.isEmpty()) {
            row.setStyle("");
            return;
        }

        String bg;
        if (row.isSelected()) {
            bg = selectionColor;
        } else if (row.isHover()) {
            bg = hoverColor;
        } else {
            bg = (row.getIndex() % 2 == 0) ? evenColor : oddColor;
        }

        row.setStyle("-fx-background-color: " + bg + ";"
                + " -fx-text-fill: " + textColor + ";"
                + " -fx-border-color: transparent transparent " + separator + " transparent;"
                + " -fx-border-width: 0 0 1px 0;");
    }

    private void applyHeaderStyling(TableView<?> tableView, ThemeInterface theme) {
        String headerBg = headerBgColor != null
                ? headerBgColor : theme.colors().surface();
        String headerText = headerTextColor != null
                ? headerTextColor : theme.colors().textSecondary();
        String separator = separatorColor != null
                ? separatorColor : theme.colors().border();

        tableView.skinProperty().addListener((obs, oldSkin, newSkin) -> {
            if (newSkin == null) return;
            javafx.application.Platform.runLater(() ->
                    styleHeaderCells(tableView, headerBg, headerText, separator));
        });

        if (tableView.getSkin() != null) {
            styleHeaderCells(tableView, headerBg, headerText, separator);
        }
    }

    private void styleHeaderCells(TableView<?> tableView, String bgColor, String textColor, String separatorColor) {
        for (var child : tableView.lookupAll(".column-header")) {
            child.setStyle("-fx-background-color: " + bgColor + ";"
                    + " -fx-border-color: transparent transparent " + separatorColor + " transparent;"
                    + " -fx-border-width: 0 0 1px 0;");

            for (Node label : child.lookupAll(".label")) {
                label.setStyle("-fx-text-fill: " + textColor + "; -fx-font-weight: bold;");
            }
        }

        try {
            var headerBg = tableView.lookup(".column-header-background");
            if (headerBg != null) {
                headerBg.setStyle("-fx-background-color: " + bgColor + ";");
            }
        } catch (Exception ignored) {}
    }
}
