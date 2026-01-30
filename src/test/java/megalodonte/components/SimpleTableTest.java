package megalodonte.components;

import megalodonte.State;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.application.Platform;
import javafx.scene.layout.VBox;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SimpleTableTest extends ApplicationTest {
    
    private SimpleTable<TestModel> table;
    private State<List<TestModel>> testData;
    
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
        table = new SimpleTable<>();
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
        // Vincular dados reativos
        table.fromData(testData);
        
        // Verificar que os itens foram carregados
        assertEquals(3, table.getItems().size());
        
        // Atualizar dados e verificar reatividade
        Platform.runLater(() -> {
            List<TestModel> newItems = Arrays.asList(
                new TestModel(4L, "Nova Empresa", "111222333", "nova@empresa.com", "11666666666", LocalDateTime.now())
            );
            testData.set(newItems);
        });
        
        // Esperar a atualização e verificar
        waitForFx(100);
        assertEquals(1, table.getItems().size());
        assertEquals("Nova Empresa", table.getItems().get(0).nome);
    }
    
    @Test
    void testHeaderConfiguration() {
        table.fromData(testData);
        
        // Configurar colunas usando API fluente
        table.header()
            .columns()
                .column("ID", item -> item.id, 40.0)
                .column("Nome", item -> item.nome, 100.0)
                .column("CNPJ", item -> item.cpfCnpj)
                .column("Email", item -> item.email)
                .column("Telefone", item -> formatPhone(item.celular))
                .column("Data Criação", item -> formatDateTime(item.dataCriacao))
            .build();
        
        // Verificar que as colunas foram criadas
        assertEquals(6, ((javafx.scene.control.TableView<?>) table.getJavaFxNode()).getColumns().size());
    }
    
    @Test
    void testItemSelection() {
        table.fromData(testData);
        
        // Configurar callback de seleção
        final TestModel[] selected = {null};
        table.onItemSelectChange(item -> selected[0] = item);
        
        // Configurar colunas para permitir seleção
        table.header()
            .columns()
                .column("ID", item -> item.id)
                .column("Nome", item -> item.nome)
            .build();
        
        // Simular seleção
        Platform.runLater(() -> {
            table.getItems().get(0); // Acessar primeiro item
        });
        
        waitForFx(100);
        assertNotNull(table.getItems());
        assertEquals(3, table.getItems().size());
    }
    
    @Test
    void testItemOperations() {
        table.fromData(testData);
        
        // Testar adição de item
        TestModel newItem = new TestModel(4L, "Teste", "000000000", "teste@teste.com", "11000000000", LocalDateTime.now());
        table.addItem(newItem);
        assertEquals(4, table.getItems().size());
        
        // Testar remoção de item
        assertTrue(table.removeItem(newItem));
        assertEquals(3, table.getItems().size());
        
        // Testar limpeza
        table.clear();
        assertEquals(0, table.getItems().size());
    }
    
    @Test
    void testChainedConfiguration() {
        // Testar API fluente completa
        SimpleTable<TestModel> configuredTable = new SimpleTable<TestModel>()
            .fromData(testData)
            .onItemSelectChange(item -> System.out.println("Selected: " + item.nome))
            .onItemDoubleClick(item -> System.out.println("Double clicked: " + item.nome))
            .header()
                .columns()
                    .column("ID", item -> item.id)
                    .column("Nome", item -> item.nome)
                    .column("Email", item -> item.email)
                .build();
        
        assertNotNull(configuredTable);
        assertEquals(3, configuredTable.getItems().size());
        assertEquals(3, ((javafx.scene.control.TableView<?>) configuredTable.getJavaFxNode()).getColumns().size());
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