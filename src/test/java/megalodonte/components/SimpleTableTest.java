package megalodonte.components;

import megalodonte.base.state.State;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.layout.VBox;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SimpleTableTest extends ApplicationTest {
    
    private SimpleTable<TestModel> table;
    private State<List<TestModel>> testData;
    private Stage stage;
    
    // Modelo de teste para simular FornecedorModel
    static class TestModel {
        Long id;
        String nome;
        String cpfCnpj;
        String email;
        String celular;
        LocalDateTime dataCriacao;
        
        TestModel(Long id, String nome, String cpfCnpj, String email, String celular, LocalDateTime dataCriacao) {
            this.id = id;
            this.nome = nome;
            this.cpfCnpj = cpfCnpj;
            this.email = email;
            this.celular = celular;
            this.dataCriacao = dataCriacao;
        }
    }
    
    @Override
    public void start(Stage stage) {
        this.stage = stage;
        table = new SimpleTable<TestModel>().horizontalScroll();
        testData = State.of(Arrays.asList(
            new TestModel(1L, "Empresa A", "123456789", "contato@empresaA.com", "11999999999", LocalDateTime.now()),
            new TestModel(2L, "Empresa B", "987654321", "contato@empresaB.com", "11888888888", LocalDateTime.now()),
            new TestModel(3L, "Empresa C", "456789123", "contato@empresaC.com", "11777777777", LocalDateTime.now())
        ));
        
        VBox root = new VBox(table.getJavaFxNode());
        stage.setScene(new Scene(root, 800, 600));
        stage.show();
    }
    
    @Test
    void testSimpleTableCreation() {
        assertNotNull(table);
        assertNotNull(table.getJavaFxNode());
        assertTrue(table.getJavaFxNode() instanceof javafx.scene.control.TableView);
    }
    
    @Test
    void testFromDataBinding() {
        interact(() -> {
            table.fromData(testData);
            assertEquals(3, table.getItems().size());
            List<TestModel> newItems = Arrays.asList(
                new TestModel(4L, "Nova Empresa", "111222333", "nova@empresa.com", "11666666666", LocalDateTime.now())
            );
            testData.set(newItems);
            assertEquals(1, table.getItems().size());
            assertEquals("Nova Empresa", table.getItems().get(0).nome);
        });
    }
    
    @Test
    void testHeaderConfiguration() {
        interact(() -> {
            table.fromData(testData);
            table.header()
                .columns()
                    .column("ID", item -> item.id, 40.0)
                    .column("Nome", item -> item.nome, 100.0)
                    .column("CNPJ", item -> item.cpfCnpj)
                    .column("Email", item -> item.email)
                    .column("Telefone", item -> formatPhone(item.celular))
                    .column("Data Criação", item -> formatDateTime(item.dataCriacao))
                .build();
            assertEquals(6, table.getTableView().getColumns().size());
        });
    }
    
    @Test
    void testItemSelection() {
        final TestModel[] selected = {null};
        interact(() -> {
            table.fromData(testData);
            table.onItemSelectChange(item -> selected[0] = item);
            table.header()
                .columns()
                    .column("ID", item -> item.id)
                    .column("Nome", item -> item.nome)
                .build();
            table.getTableView().getSelectionModel().select(0);
        });
        assertSame(testData.get().get(0), selected[0]);
    }
    
    @Test
    void testItemOperations() {
        interact(() -> {
            table.fromData(testData);
            TestModel newItem = new TestModel(4L, "Teste", "000000000", "teste@teste.com", "11000000000", LocalDateTime.now());
            table.addItem(newItem);
            assertEquals(4, table.getItems().size());
            assertTrue(table.removeItem(newItem));
            assertEquals(3, table.getItems().size());
            table.clear();
            assertEquals(0, table.getItems().size());
        });
    }
    
    @Test
    void testChainedConfiguration() {
        interact(() -> {
            SimpleTable<TestModel> configuredTable = new SimpleTable<TestModel>()
                .fromData(testData)
                .onItemSelectChange(item -> { })
                .onItemDoubleClick(item -> { })
                .header()
                    .columns()
                        .column("ID", item -> item.id)
                        .column("Nome", item -> item.nome)
                        .column("Email", item -> item.email)
                    .build();
            assertEquals(3, configuredTable.getItems().size());
            assertEquals(3, configuredTable.getTableView().getColumns().size());
        });
    }

    @Test
    void horizontalScrollDistribuiEspacoSobrandoSemEncolherCabecalhos() {
        interact(() -> table.fromData(testData)
                .header().columns()
                .column("Razão social", item -> item.nome)
                .column("CPF/CNPJ", item -> item.cpfCnpj)
                .column("Data de criação", item -> formatDateTime(item.dataCriacao))
                .build());

        waitForFx(100);

        interact(() -> {
            var tableView = table.getTableView();
            double totalColumnsWidth = tableView.getColumns().stream()
                    .mapToDouble(javafx.scene.control.TableColumnBase::getWidth)
                    .sum();
            assertTrue(totalColumnsWidth >= tableView.getWidth() - 20,
                    "As colunas devem aproveitar a largura disponível da tabela");
            assertTrue(tableView.getColumns().get(0).getWidth() > 120,
                    "Cabeçalho Razão social não deve ser truncado quando há espaço");
            assertTrue(tableView.getColumns().get(2).getWidth() > 140,
                    "Cabeçalho Data de criação não deve ser truncado quando há espaço");
        });
    }

    @Test
    void resizedTableKeepsAllShortListRowsVisibleAfterWheelScroll() {
        interact(() -> {
            var records = new java.util.ArrayList<TestModel>();
            for (long id = 1; id <= 6; id++)
                records.add(new TestModel(id, "Empresa " + id, "", "", "", LocalDateTime.now()));
            testData.set(records);
            table.fromData(testData).header().columns().column("Nome", row -> row.nome).build();
            table.getTableView().setFixedCellSize(62);
            stage.setHeight(280);
        });
        waitForFx(100);
        interact(() -> table.getTableView().scrollTo(5));
        waitForFx(100);
        interact(() -> stage.setHeight(700));
        waitForFx(100);
        moveTo(table.getTableView()).scroll(3);
        waitForFx(100);
        interact(() -> {
            var scrollBar = (javafx.scene.control.ScrollBar) table.getTableView().lookup(".scroll-bar:vertical");
            assertNotNull(scrollBar);
            assertEquals(scrollBar.getMin(), scrollBar.getValue(), 0.01,
                    "O viewport maior não deve manter o deslocamento calculado para a janela menor");
            var visibleIndexes = table.getTableView().lookupAll(".table-row-cell").stream()
                    .map(node -> (javafx.scene.control.TableRow<?>) node)
                    .filter(row -> !row.isEmpty() && row.isVisible())
                    .map(javafx.scene.control.TableRow::getIndex)
                    .toList();
            assertTrue(visibleIndexes.containsAll(List.of(0, 1, 2, 3, 4, 5)),
                    "Todos os registros devem permanecer visíveis quando cabem no viewport");
        });
    }
    
    // Métodos utilitários para formatação (simulando Utils)
    private String formatPhone(String phone) {
        if (phone == null || phone.length() < 11) return phone;
        return "(" + phone.substring(0, 2) + ") " + 
               phone.substring(2, 7) + "-" + phone.substring(7);
    }
    
    private String formatDateTime(LocalDateTime dateTime) {
        if (dateTime == null) return "";
        return dateTime.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
    }
    
    private void waitForFx(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
