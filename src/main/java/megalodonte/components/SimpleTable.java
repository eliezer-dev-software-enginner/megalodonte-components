package megalodonte.components;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TableCell;
import javafx.scene.control.CheckBox;
import javafx.scene.control.SelectionMode;
import megalodonte.base.state.State;
import megalodonte.base.state.ReadableState;
import megalodonte.props.SimpleTableProps;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import megalodonte.base.components.Component;
public class SimpleTable<T> extends Component  {

    private final TableView<T> tableView;
    private final ObservableList<T> items;
    private Consumer<T> onItemSelectChange;
    private Consumer<T> onItemDoubleClick;
    private Consumer<Boolean> onChangeFocus;
    private Consumer<List<T>> onItemsSelectChange;
    private CheckBox selectAllCheckBox;

    // Cache por tabela — evita reler/redecodificar do disco a cada recycle de célula
    // enquanto rola (TableView virtualiza: a mesma imagem passa por updateItem() várias
    // vezes conforme o usuário rola pra cima/baixo). Uma entrada com valor null (falha
    // ao carregar) não fica em cache — tenta de novo na próxima vez, de propósito.
    private final java.util.Map<String, javafx.scene.image.Image> imageCache = new java.util.HashMap<>();


    //pagination

    private List<T> fullData = List.of();
    private int pageSize = -1; // -1 = paginação desabilitada (comportamento atual, sem mudança)
    private final State<Integer> currentPage = State.of(0); // 0-indexed
    private final State<String> pageLabel = State.of("");
    
    public SimpleTable() {
        this(new SimpleTableProps());
    }

    private boolean horizontalScrollEnabled = false;


    public SimpleTable(SimpleTableProps props) {
        super(new TableView<>(), props);
        this.tableView = (TableView<T>) this.node;
        this.items = FXCollections.observableArrayList();
        this.tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY); // default
        this.tableView.setItems(items);
        this.tableView.setEditable(true);

        // maxHeight (livre por padrão, ou o teto de SimpleTableProps.maxHeight) já foi
        // aplicado por props.apply(node) dentro do super(...) acima — ver
        // SimpleTableProps.applyContainerStyling. Setar de novo aqui sempre sobrescreveria
        // um teto customizado de volta pro padrão, incondicionalmente.
        this.tableView.setMinWidth(0);   // <- não deixa o piso das colunas virar o piso da página
        // Piso mínimo de altura: sem isso, dentro de um ScrollPane com fitToHeight,
        // a tabela encolhe pra caber no viewport em vez de nunca ultrapassá-lo — e
        // como o conteúdo nunca ultrapassa a altura disponível, o scroll de fora
        // nunca ativa. Com um mínimo, quando janela+form+busca+esse mínimo não
        // cabem mais, o overflow acontece de verdade e o scroll externo volta.
        this.tableView.setMinHeight(200);
        javafx.scene.layout.VBox.setVgrow(this.tableView, javafx.scene.layout.Priority.ALWAYS); // <- pede prioridade quando pai é VBox

        // Sem isso, rolar rápido/forte dentro da tabela (ex: chegando no fim da lista
        // numa única passada de roda) deixa o resto do scroll vazar pro ScrollPane da
        // página, rolando ela também. Ver Scroll.confineScrollEvents.
        Scroll.confineScrollEvents(this.tableView);

        loadStyleSheet();
        setupDefaultBehavior();
    }

    public SimpleTable<T> horizontalScroll() {
        this.horizontalScrollEnabled = true;
        tableView.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
        return this;
    }


    private javafx.scene.image.Image loadImage(String path, double size) {
        return imageCache.computeIfAbsent(path, p -> {
            try {
                return new javafx.scene.image.Image(p, size, size, true, true, true);
            } catch (Exception e) {
                return null;
            }
        });
    }

    private void loadStyleSheet() {
        var css = getClass().getResource("/simple-table.css");
        if (css != null && !tableView.getStylesheets().contains(css.toExternalForm())) {
            tableView.getStylesheets().add(css.toExternalForm());
        }
    }

    public TableView<T> getTableView() {
        return tableView;
    }
    
    private void setupDefaultBehavior() {
        // Configurar seleção de item
        tableView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (onItemSelectChange != null) {
                onItemSelectChange.accept(newVal);
            }
        });

        tableView.getSelectionModel().getSelectedItems().addListener(
                (javafx.collections.ListChangeListener<T>) change -> {
                    if (onItemsSelectChange != null) {
                        onItemsSelectChange.accept(List.copyOf(tableView.getSelectionModel().getSelectedItems()));
                    }
                    refreshSelectionColumn();
                });

        tableView.focusedProperty().addListener((obs, oldVal, newVal) -> {
           //IO.println("Table view focused: " + newVal);
            if(onChangeFocus != null) {
                onChangeFocus.accept(newVal);
            }
        });
        
        // Configurar double click, preservando o rowFactory já definido por
        // SimpleTableProps (hover/seleção/zebra) em vez de substituí-lo por
        // uma TableRow "crua" — senão o hover para de funcionar.
        var previousRowFactory = tableView.getRowFactory();
        tableView.setRowFactory(tv -> {
            javafx.scene.control.TableRow<T> row = previousRowFactory != null
                    ? previousRowFactory.call(tv)
                    : new javafx.scene.control.TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !row.isEmpty()) {
                    if (onItemDoubleClick != null) {
                        onItemDoubleClick.accept(row.getItem());
                    }
                }
            });
            return row;
        });
    }

    /** Habilita paginação client-side sobre a lista completa já vinculada via fromData. */
    public SimpleTable<T> paginate(int pageSize) {
        this.pageSize = pageSize;
        refreshPage();
        return this;
    }
    
    /**
     * Vincula os dados da tabela a um State<List<T>> reativo.
     * Quando o state mudar, a tabela será atualizada automaticamente.
     * 
     * @param state o State<List<T>> contendo os dados
     * @return this SimpleTable instance para method chaining
     */
    public SimpleTable<T> fromData(ReadableState<List<T>> state) {
        state.subscribe(newItems -> {
            fullData = newItems != null ? newItems : List.of();
            int maxPage = Math.max(0, totalPages() - 1);
            if (currentPage.get() > maxPage) currentPage.set(maxPage); // dispara refreshPage via listener abaixo
            else refreshPage();
        });
        currentPage.subscribe(p -> refreshPage());
        return this;
    }

    private void refreshPage() {
        items.clear();
        if (pageSize <= 0) {
            items.addAll(fullData);
            pageLabel.set("");
            return;
        }
        int from = currentPage.get() * pageSize;
        if (from < fullData.size()) {
            items.addAll(fullData.subList(from, Math.min(from + pageSize, fullData.size())));
        }
        pageLabel.set("Página " + (currentPage.get() + 1) + " de " + Math.max(1, totalPages()));
    }

    private int totalPages() {
        return pageSize <= 0 ? 1 : (int) Math.ceil(fullData.size() / (double) pageSize);
    }

    public void nextPage() {
        if (currentPage.get() + 1 < totalPages()) currentPage.set(currentPage.get() + 1);
    }

    public void previousPage() {
        if (currentPage.get() > 0) currentPage.set(currentPage.get() - 1);
    }

    /** Row pronta com Anterior/Próxima + rótulo "Página X de Y" — adicione ao lado da tabela. */
    public Component paginationControls() {
        return new megalodonte.components.layout_components.Row(
                new megalodonte.props.RowProps().spacingOf(10).bottomVertically())
                .r_child(new megalodonte.components.Button("< Anterior").onClick(this::previousPage))
                .r_child(new megalodonte.components.Text(pageLabel,
                        new megalodonte.props.TextProps().fontSize(megalodonte.base.theme.ThemeManager.theme().typography().small())))
                .r_child(new megalodonte.components.Button("Próxima >").onClick(this::nextPage));
    }
    
    /**
     * Configurador para o cabeçalho e colunas da tabela.
     * 
     * @return HeaderBuilder para configuração das colunas
     */
    public HeaderBuilder header() {
        return new HeaderBuilder();
    }

    public SimpleTable<T> onChangeFocus(Consumer<Boolean> callback) {
        this.onChangeFocus = callback;
        return this;
    }


    /**
     * Configura o callback para mudança de seleção de item.
     * 
     * @param callback função chamada quando a seleção mudar
     * @return this SimpleTable instance para method chaining
     */
    public SimpleTable<T> onItemSelectChange(Consumer<T> callback) {
        this.onItemSelectChange = callback;
        return this;
    }

    /**
     * Exibe checkboxes para seleção de várias linhas. O checkbox do cabeçalho seleciona
     * ou limpa os itens da página atualmente exibida.
     */
    public SimpleTable<T> enableMultipleSelection(Consumer<List<T>> callback) {
        this.onItemsSelectChange = callback;
        tableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        if (selectAllCheckBox != null) return this;

        selectAllCheckBox = new CheckBox();
        selectAllCheckBox.setOnAction(event -> {
            if (selectAllCheckBox.isSelected()) {
                tableView.getSelectionModel().selectAll();
            } else {
                tableView.getSelectionModel().clearSelection();
            }
        });

        TableColumn<T, Boolean> selectionColumn = new TableColumn<>();
        selectionColumn.setGraphic(selectAllCheckBox);
        selectionColumn.setSortable(false);
        selectionColumn.setResizable(false);
        selectionColumn.setPrefWidth(42);
        selectionColumn.setMinWidth(42);
        selectionColumn.setMaxWidth(42);
        selectionColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleBooleanProperty(
                tableView.getSelectionModel().getSelectedItems().contains(data.getValue())));
        selectionColumn.setCellFactory(column -> new TableCell<>() {
            private final CheckBox checkBox = new CheckBox();

            {
                checkBox.setOnAction(event -> {
                    if (getIndex() < 0 || getIndex() >= tableView.getItems().size()) return;
                    if (checkBox.isSelected()) {
                        tableView.getSelectionModel().select(getIndex());
                    } else {
                        tableView.getSelectionModel().clearSelection(getIndex());
                    }
                });
                setGraphic(checkBox);
            }

            @Override
            protected void updateItem(Boolean ignored, boolean empty) {
                super.updateItem(ignored, empty);
                checkBox.setVisible(!empty);
                checkBox.setManaged(!empty);
                checkBox.setSelected(!empty && tableView.getSelectionModel().isSelected(getIndex()));
            }
        });
        tableView.getColumns().add(0, selectionColumn);
        return this;
    }

    public List<T> getSelectedItems() {
        return List.copyOf(tableView.getSelectionModel().getSelectedItems());
    }

    private void refreshSelectionColumn() {
        if (selectAllCheckBox == null) return;
        var selectedItems = tableView.getSelectionModel().getSelectedItems();
        selectAllCheckBox.setSelected(!items.isEmpty() && selectedItems.containsAll(items));
        tableView.refresh();
    }
    
    /**
     * Configura o callback para double click em um item.
     * 
     * @param callback função chamada quando houver double click
     * @return this SimpleTable instance para method chaining
     */
    public SimpleTable<T> onItemDoubleClick(Consumer<T> callback) {
        this.onItemDoubleClick = callback;
        return this;
    }

//    public SimpleTable<T> onClickOutside(Runnable callback) {
//        tableView.setOnMouseClicked(event -> {
//            var target = event.getTarget();
//            // Clicou na área vazia da tabela (não em uma célula ou linha)
//            if (target instanceof TableView || target instanceof javafx.scene.control.skin.VirtualFlow) {
//                tableView.getSelectionModel().clearSelection();
//                if (callback != null) callback.run();
//            }
//        });
//        return this;
//    }
//

    public SimpleTable<T> onClickOutside(Runnable callback) {
        tableView.setOnMouseClicked(event -> {
            javafx.scene.Node target = (javafx.scene.Node) event.getTarget();
            // Sobe na árvore de nós a partir do target
            // Se encontrar um TableRow com item, é clique em linha — ignora
            // Se chegar no TableView sem passar por TableRow, é área vazia
            javafx.scene.Node current = target;
            while (current != null && current != tableView) {
                if (current instanceof javafx.scene.control.TableRow<?> row && !row.isEmpty()) {
                    return; // clique em linha com dado — não faz nada
                }
                current = current.getParent();
            }
            // chegou aqui = área vazia ou cabeçalho
            tableView.getSelectionModel().clearSelection();
            if (callback != null) callback.run();
        });
        return this;
    }
    /**
     * Builder para configuração das colunas da tabela.
     */
    public class HeaderBuilder {
        private ColumnsBuilder columnsBuilder = new ColumnsBuilder();
        
        /**
         * Inicia a configuração das colunas.
         * 
         * @return ColumnsBuilder para adicionar colunas
         */
        public ColumnsBuilder columns() {
            return columnsBuilder;
        }
        
        /**
         * Finaliza a configuração e retorna a tabela.
         * 
         * @return SimpleTable instance
         */
        public SimpleTable<T> build() {
            return SimpleTable.this;
        }
    }
    
    /**
     * Builder para adicionar colunas à tabela.
     */
    public class ColumnsBuilder {
        
        /**
         * Adiciona uma coluna simples baseada em uma propriedade do objeto.
         * 
         * @param title título da coluna
         * @param valueExtractor função para extrair o valor do objeto
         * @return ColumnsBuilder para method chaining
         */
        public ColumnsBuilder column(String title, Function<T, Object> valueExtractor) {
            return column(title, valueExtractor, null);
        }
        
        /**
         * Adiciona uma coluna com largura máxima específica.
         * 
         * @param title título da coluna
         * @param valueExtractor função para extrair o valor do objeto
         * @return ColumnsBuilder para method chaining
         */
        public ColumnsBuilder column(String title, Function<T, Object> valueExtractor, Double width) {
            TableColumn<T, String> col = new TableColumn<>(title);
            col.setCellValueFactory(data -> {
                T item = data.getValue();
                if (item == null) {
                    return new javafx.beans.property.SimpleStringProperty("");
                }
                try {
                    Object value = valueExtractor.apply(item);
                    String displayValue = value != null ? value.toString() : "";
                    return new javafx.beans.property.SimpleStringProperty(displayValue);
                } catch (Exception e) {
                    return new javafx.beans.property.SimpleStringProperty("");
                }
            });

            // Sem largura explícita, TableColumn ficaria travada em 80px (default do
            // JavaFX), curto demais pra headers como "Data de criação" — mede o texto
            // do título (mesma técnica de medição já usada em v2.Input pro caret) e
            // define uma largura mínima confortável a partir dele.
            col.setPrefWidth(width != null ? width : defaultColumnWidth(title));

            tableView.getColumns().add(col);
            return this;
        }

        private double defaultColumnWidth(String title) {
            var measurer = new javafx.scene.text.Text(title);
            double headerWidth = measurer.getLayoutBounds().getWidth();
            return Math.max(100, headerWidth + 40); // folga pra padding do header + ícone de ordenação
        }

        /**
         * Adiciona uma coluna que mostra uma miniatura de imagem em vez de texto.
         * {@code pathExtractor} deve retornar um caminho que {@link javafx.scene.image.Image}
         * aceite diretamente — URI de arquivo (ex: {@code file:///...}, o formato que
         * FileChooser.getSelectedFile().toURI() já produz), URL http(s) ou caminho de
         * recurso no classpath. Caminho nulo/vazio ou falha ao carregar deixam a célula
         * em branco (não lança exceção nem quebra a linha).
         *
         * @param title         título da coluna
         * @param pathExtractor função que extrai o caminho/URI da imagem do item
         * @param size          largura e altura (em px) da miniatura
         * @return ColumnsBuilder para method chaining
         */
        public ColumnsBuilder imageColumn(String title, Function<T, String> pathExtractor, double size) {
            TableColumn<T, String> col = new TableColumn<>(title);
            col.setSortable(false);
            col.setCellValueFactory(data -> {
                T item = data.getValue();
                if (item == null) return new javafx.beans.property.SimpleStringProperty("");
                try {
                    String path = pathExtractor.apply(item);
                    return new javafx.beans.property.SimpleStringProperty(path != null ? path : "");
                } catch (Exception e) {
                    return new javafx.beans.property.SimpleStringProperty("");
                }
            });

            col.setCellFactory(c -> new TableCell<T, String>() {
                private final javafx.scene.image.ImageView imageView = new javafx.scene.image.ImageView();
                {
                    imageView.setFitWidth(size);
                    imageView.setFitHeight(size);
                    imageView.setPreserveRatio(true);
                    setGraphic(imageView);
                }

                @Override
                protected void updateItem(String path, boolean empty) {
                    super.updateItem(path, empty);
                    imageView.setImage(empty || path == null || path.isBlank() ? null : loadImage(path, size));
                }
            });

            tableView.getColumns().add(col);
            return this;
        }

        /** Mesmo que {@link #imageColumn(String, Function, double)}, miniatura de 40px. */
        public ColumnsBuilder imageColumn(String title, Function<T, String> pathExtractor) {
            return imageColumn(title, pathExtractor, 40.0);
        }

        /**
         * Adiciona uma coluna editável.
         * Quando o usuário confirmar a edição (Enter ou perda de foco),
         * o onCommit é chamado com o item e o novo valor como String.
         *
         * @param title          título da coluna
         * @param valueExtractor função para extrair o valor do objeto
         * @param onCommit       chamado com (item, novoValor) ao confirmar edição
         * @return ColumnsBuilder para method chaining
         */
        public ColumnsBuilder editableColumn(String title, Function<T, Object> valueExtractor, BiConsumer<T, String> onCommit) {
            TableColumn<T, String> col = new TableColumn<>(title);

            col.setCellValueFactory(data -> {
                T item = data.getValue();
                if (item == null) return new javafx.beans.property.SimpleStringProperty("");
                try {
                    Object value = valueExtractor.apply(item);
                    return new javafx.beans.property.SimpleStringProperty(value != null ? value.toString() : "");
                } catch (Exception e) {
                    return new javafx.beans.property.SimpleStringProperty("");
                }
            });

            if (onCommit != null) {
                col.setCellFactory(javafx.scene.control.cell.TextFieldTableCell.forTableColumn());
                col.setOnEditCommit(event -> {
                    T item = event.getRowValue();
                    onCommit.accept(item, event.getNewValue());
                });
            }

            tableView.getColumns().add(col);
            return this;
        }
        
        /**
         * Finaliza a configuração das colunas.
         * 
         * @return HeaderBuilder para continuar configuração
         */
        public HeaderBuilder end() {
            return new HeaderBuilder();
        }
        
        /**
         * Finaliza e constrói a tabela.
         * 
         * @return SimpleTable instance
         */
        public SimpleTable<T> build() {
            return SimpleTable.this;
        }
    }
    
    /**
     * Obtém a lista de itens da tabela.
     * 
     * @return ObservableList<T> itens da tabela
     */
    public ObservableList<T> getItems() {
        return items;
    }
    
    /**
     * Obtém o item selecionado atualmente.
     * 
     * @return item selecionado ou null
     */
    public T getSelectedItem() {
        return tableView.getSelectionModel().getSelectedItem();
    }
    
    /**
     * Limpa todos os itens da tabela.
     */
    public void clear() {
        items.clear();
    }
    
    /**
     * Adiciona um item à tabela.
     * 
     * @param item item a ser adicionado
     */
    public void addItem(T item) {
        items.add(item);
    }
    
    /**
     * Remove um item da tabela.
     * 
     * @param item item a ser removido
     * @return true se o item foi removido
     */
    public boolean removeItem(T item) {
        return items.remove(item);
    }
}
