package megalodonte.components.examples;

import megalodonte.components.SimpleTable;
import megalodonte.State;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

/**
 * Exemplo de uso do SimpleTable para demonstrar a API simplificada.
 * Este exemplo mostra como criar uma tabela declarativa e reativa
 * substituindo o código JavaFX TableView tradicional.
 */
public class SimpleTableExample {
    
    // Modelo de exemplo (similar ao FornecedorModel)
    static class FornecedorModel {
        Long id;
        String nome;
        String cpfCnpj;
        String email;
        String celular;
        LocalDateTime dataCriacao;
        
        FornecedorModel(Long id, String nome, String cpfCnpj, String email, String celular, LocalDateTime dataCriacao) {
            this.id = id;
            this.nome = nome;
            this.cpfCnpj = cpfCnpj;
            this.email = email;
            this.celular = celular;
            this.dataCriacao = dataCriacao;
        }
    }
    
    public SimpleTable<FornecedorModel> createTableExample() {
        // Estado reativo com dados dos fornecedores
        State<List<FornecedorModel>> fornecedores = State.of(Arrays.asList(
            new FornecedorModel(1L, "Empresa A", "123456789", "contato@empresaA.com", "11999999999", LocalDateTime.now()),
            new FornecedorModel(2L, "Empresa B", "987654321", "contato@empresaB.com", "11888888888", LocalDateTime.now()),
            new FornecedorModel(3L, "Empresa C", "456789123", "contato@empresaC.com", "11777777777", LocalDateTime.now())
        ));
        
        // Criação da tabela usando API simplificada
        SimpleTable<FornecedorModel> table = new SimpleTable<FornecedorModel>()
            .fromData(fornecedores)
            .onItemSelectChange(item -> {
                System.out.println("Item selecionado: " + (item != null ? item.nome : "null"));
            })
            .header()
                .columns()
                    .column("ID", it -> it.id != null ? String.valueOf(it.id) : "", 40.0)
                    .column("Nome", it -> it.nome, 100.0)
                    .column("CNPJ", it -> it.cpfCnpj)
                    .column("Email", it -> it.email)
                    .column("Telefone", it -> formatPhone(it.celular))
                    .column("Data de Criação", it -> formatDateTime(it.dataCriacao))
                .build();
        
        return table;
    }
    
    // Exemplo demonstrando atualização reativa
    public void demonstrateReactiveUpdate() {
        State<List<FornecedorModel>> fornecedores = State.of(Arrays.asList(
            new FornecedorModel(1L, "Empresa Original", "123456789", "contato@original.com", "11999999999", LocalDateTime.now())
        ));
        
        SimpleTable<FornecedorModel> table = new SimpleTable<FornecedorModel>()
            .fromData(fornecedores)
            .header()
                .columns()
                    .column("ID", it -> it.id != null ? String.valueOf(it.id) : "")
                    .column("Nome", it -> it.nome)
                    .column("Email", it -> it.email)
                .build();
        
        System.out.println("Tabela inicial com 1 item");
        System.out.println("Items na tabela: " + table.getItems().size());
        
        // Simular atualização de dados (viria de API, banco, etc.)
        List<FornecedorModel> novosFornecedores = Arrays.asList(
            new FornecedorModel(1L, "Empresa Atualizada", "123456789", "contato@atualizada.com", "11999999999", LocalDateTime.now()),
            new FornecedorModel(2L, "Nova Empresa", "987654321", "contato@nova.com", "11888888888", LocalDateTime.now())
        );
        
        fornecedores.set(novosFornecedores);
        
        System.out.println("Tabela após atualização reativa");
        System.out.println("Items na tabela: " + table.getItems().size());
    }
    
    // Exemplo comparativo: código antigo vs novo
    public void demonstrateSimplification() {
        System.out.println("=== CÓDIGO ANTIGO (TableView tradicional) ===");
        System.out.println("Precisava criar manualmente:");
        System.out.println("- TableView<FornecedorModel> table = new TableView<>();");
        System.out.println("- table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);");
        System.out.println("- TableColumn<FornecedorModel, String> idCol = new TableColumn<>(\"ID\");");
        System.out.println("- idCol.setCellValueFactory(data -> new SimpleStringProperty(...));");
        System.out.println("- ... (repetir para cada coluna)");
        System.out.println("- table.getColumns().addAll(...);");
        System.out.println("- table.setItems(fornecedores);");
        System.out.println("- Utils.onItemTableSelectedChange(table, data-> fornecedorSelected.set(data));");
        
        System.out.println("\n=== CÓDIGO NOVO (SimpleTable) ===");
        System.out.println("SimpleTable<FornecedorModel> table = new SimpleTable<>()");
        System.out.println("    .fromData(fornecedores)");
        System.out.println("    .onItemSelectChange(it -> fornecedorSelected.set(it))");
        System.out.println("    .header()");
        System.out.println("        .columns()");
        System.out.println("            .column(\"ID\", it-> it.id)");
        System.out.println("            .column(\"Nome\", it-> it.nome)");
        System.out.println("            .column(\"Data de criação\", it-> formatDateTime(it.dataCriacao))");
        System.out.println("        .build();");
        
        System.out.println("\nVantagens:");
        System.out.println("✓ Declarativo e legível");
        System.out.println("✓ Reativo (atualização automática quando State mudar)");
        System.out.println("✓ Menos verboso");
        System.out.println("✓ Type-safe");
        System.out.println("✓ API fluente para encadeamento");
    }
    
    // Métodos utilitários
    private String formatPhone(String phone) {
        if (phone == null || phone.length() < 11) return phone;
        return "(" + phone.substring(0, 2) + ") " + 
               phone.substring(2, 7) + "-" + phone.substring(7);
    }
    
    private String formatDateTime(LocalDateTime dateTime) {
        if (dateTime == null) return "";
        return dateTime.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
    }
    
    public static void main(String[] args) {
        SimpleTableExample example = new SimpleTableExample();
        
        System.out.println("=== EXEMPLO DE USO DO SIMPLETABLE ===");
        
        // Criar e demonstrar tabela
        SimpleTable<FornecedorModel> table = example.createTableExample();
        System.out.println("Tabela criada com " + table.getItems().size() + " itens");
        
        // Demonstrar atualização reativa
        example.demonstrateReactiveUpdate();
        
        // Mostrar simplificação
        example.demonstrateSimplification();
    }
}