package megalodonte.components;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TableCell;
import megalodonte.State;
import megalodonte.ReadableState;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class SimpleTable<T> extends Component {
    
    private final TableView<T> tableView;
    private final ObservableList<T> items;
    private Consumer<T> onItemSelectChange;
    private Consumer<T> onItemDoubleClick;
    
    public SimpleTable() {
        super(new TableView<>());
        this.tableView = (TableView<T>) this.node;
        this.items = FXCollections.observableArrayList();
        this.tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        this.tableView.setItems(items);
        
        setupDefaultBehavior();
    }
    
    private void setupDefaultBehavior() {
        // Configurar seleção de item
        tableView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (onItemSelectChange != null) {
                onItemSelectChange.accept(newVal);
            }
        });
        
        // Configurar double click
        tableView.setRowFactory(tv -> {
            javafx.scene.control.TableRow<T> row = new javafx.scene.control.TableRow<>();
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
    
    /**
     * Vincula os dados da tabela a um State<List<T>> reativo.
     * Quando o state mudar, a tabela será atualizada automaticamente.
     * 
     * @param state o State<List<T>> contendo os dados
     * @return this SimpleTable instance para method chaining
     */
    public SimpleTable<T> fromData(ReadableState<List<T>> state) {
        state.subscribe(newItems -> {
            items.clear();
            if (newItems != null) {
                items.addAll(newItems);
            }
        });
        return this;
    }
    
    /**
     * Configurador para o cabeçalho e colunas da tabela.
     * 
     * @return HeaderBuilder para configuração das colunas
     */
    public HeaderBuilder header() {
        return new HeaderBuilder();
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
     * Configura o callback para double click em um item.
     * 
     * @param callback função chamada quando houver double click
     * @return this SimpleTable instance para method chaining
     */
    public SimpleTable<T> onItemDoubleClick(Consumer<T> callback) {
        this.onItemDoubleClick = callback;
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
         * @param maxWidth largura máxima da coluna (null para sem limite)
         * @return ColumnsBuilder para method chaining
         */
        public ColumnsBuilder column(String title, Function<T, Object> valueExtractor, Double maxWidth) {
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
            
            // Aplicar largura máxima se especificada
            if (maxWidth != null) {
                col.setMaxWidth(maxWidth);
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