package megalodonte.props;

import javafx.scene.Node;
import javafx.scene.control.TableCell;
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
    protected String rowTextColor;
    protected int headerHeight;

    public SimpleTableProps() {}

    public SimpleTableProps bgColor(String bgColor) {
        this.bgColor = bgColor;
        return this;
    }

    public SimpleTableProps headerBgColor(String headerBgColor) {
        this.headerBgColor = headerBgColor;
        return this;
    }

    public SimpleTableProps headerTextColor(String headerTextColor) {
        this.headerTextColor = headerTextColor;
        return this;
    }

    public SimpleTableProps borderColor(String borderColor) {
        this.borderColor = borderColor;
        return this;
    }

    public SimpleTableProps borderWidth(int borderWidth) {
        this.borderWidth = borderWidth;
        return this;
    }

    public SimpleTableProps borderRadius(int borderRadius) {
        this.borderRadius = borderRadius;
        return this;
    }

    public SimpleTableProps striped(boolean striped) {
        this.striped = striped;
        return this;
    }

    public SimpleTableProps rowEvenColor(String rowEvenColor) {
        this.rowEvenColor = rowEvenColor;
        return this;
    }

    public SimpleTableProps rowOddColor(String rowOddColor) {
        this.rowOddColor = rowOddColor;
        return this;
    }

    public SimpleTableProps rowHoverColor(String rowHoverColor) {
        this.rowHoverColor = rowHoverColor;
        return this;
    }

    public SimpleTableProps rowTextColor(String rowTextColor) {
        this.rowTextColor = rowTextColor;
        return this;
    }

    public SimpleTableProps headerHeight(int headerHeight) {
        this.headerHeight = headerHeight;
        return this;
    }

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

        Utils.updateBackgroundColor(tableView, finalBgColor);
        Utils.updateBorderRadius(tableView, finalBorderRadius);

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
        String finalRowTextColor = rowTextColor != null
                ? rowTextColor : theme.colors().textPrimary();
        String finalHoverColor = rowHoverColor != null
                ? rowHoverColor : hexWithAlpha(theme.colors().primary(), 0.06);
        String finalRowEven = rowEvenColor != null
                ? rowEvenColor : theme.colors().surface();
        String finalRowOdd = rowOddColor != null
                ? rowOddColor : theme.colors().background();

        ((TableView<Object>) tableView).setRowFactory(tv -> {
            TableRow<Object> row = new TableRow<>();

            row.hoverProperty().addListener((obs, wasHover, isHover) -> {
                if (row.isEmpty()) return;
                if (isHover) {
                    row.setStyle(row.getStyle()
                            + " -fx-background-color: " + finalHoverColor + ";");
                } else {
                    applyRowBaseStyle(row, finalRowEven, finalRowOdd, finalRowTextColor);
                }
            });

            row.itemProperty().addListener((obs, oldItem, newItem) -> {
                applyRowBaseStyle(row, finalRowEven, finalRowOdd, finalRowTextColor);
            });

            applyRowBaseStyle(row, finalRowEven, finalRowOdd, finalRowTextColor);
            return row;
        });
    }

    private void applyRowBaseStyle(TableRow<?> row, String evenColor, String oddColor, String textColor) {
        int index = row.getIndex();
        String bgColor = (index % 2 == 0) ? evenColor : oddColor;
        row.setStyle("-fx-background-color: " + bgColor + ";"
                + " -fx-text-fill: " + textColor + ";"
                + " -fx-border-color: transparent transparent #e2e8f0 transparent;"
                + " -fx-border-width: 0 0 1px 0;");
    }

    private void applyHeaderStyling(TableView<?> tableView, ThemeInterface theme) {
        String finalHeaderBg = headerBgColor != null
                ? headerBgColor : theme.colors().surface();
        String finalHeaderTextColor = headerTextColor != null
                ? headerTextColor : theme.colors().textSecondary();

        if (headerHeight > 0) {
            tableView.setFixedCellSize(-1);
        }

        for (TableColumn<?, ?> column : tableView.getColumns()) {
            @SuppressWarnings("unchecked")
            TableColumn<Object, String> col = (TableColumn<Object, String>) column;
            col.setCellFactory(c -> {
                TableCell<Object, String> cell = new TableCell<>() {
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        setText(empty ? null : item);
                    }
                };
                cell.setStyle("-fx-text-fill: " + finalHeaderTextColor + ";");
                return cell;
            });
        }

        tableView.skinProperty().addListener((obs, oldSkin, newSkin) -> {
            if (newSkin == null) return;
            javafx.application.Platform.runLater(() -> styleHeaderCells(tableView, finalHeaderBg, finalHeaderTextColor));
        });

        if (tableView.getSkin() != null) {
            styleHeaderCells(tableView, finalHeaderBg, finalHeaderTextColor);
        }
    }

    private void styleHeaderCells(TableView<?> tableView, String bgColor, String textColor) {
        for (TableColumn<?, ?> column : tableView.getColumns()) {
            if (column == null || column.getGraphic() == null) continue;
            if (column.getGraphic().getStyleClass().contains("column-header")) {
                for (Node child : column.getGraphic().lookupAll(".column-header .label")) {
                    child.setStyle("-fx-text-fill: " + textColor + "; -fx-font-weight: bold;");
                }
            }
        }

        try {
            var header = tableView.lookup(".column-header-background");
            if (header != null) {
                header.setStyle("-fx-background-color: " + bgColor + ";");
            }
        } catch (Exception ignored) {}

        for (var child : tableView.lookupAll(".column-header")) {
            child.setStyle(child.getStyle()
                    + " -fx-background-color: " + bgColor + ";"
                    + " -fx-border-color: transparent transparent #e2e8f0 transparent;"
                    + " -fx-border-width: 0 0 1px 0;");
        }
    }

    private static String hexWithAlpha(String hex, double alpha) {
        if (hex == null || !hex.startsWith("#") || hex.length() < 7) return hex;
        try {
            int r = Integer.parseInt(hex.substring(1, 3), 16);
            int g = Integer.parseInt(hex.substring(3, 5), 16);
            int b = Integer.parseInt(hex.substring(5, 7), 16);
            return String.format("rgba(%d, %d, %d, %.2f)", r, g, b, alpha);
        } catch (NumberFormatException e) {
            return hex;
        }
    }
}
