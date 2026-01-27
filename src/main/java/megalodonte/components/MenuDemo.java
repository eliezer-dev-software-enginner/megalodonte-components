package megalodonte.components;

import megalodonte.components.Menu;
import megalodonte.components.MenuBar;
import megalodonte.components.MenuItem;

public class MenuDemo {
    public static void main(String[] args) {
        demonstrateUsage();
    }
    
    public static void demonstrateUsage() {
        System.out.println("=== Demonstração da API de Menu ===\n");
        
        // Criando itens de menu
        MenuItem fornecedoresItem = new MenuItem("Fornecedores", () -> {
            System.out.println("Abrir fornecedores");
            // router.spawnWindow("fornecedores");
        });
        
        MenuItem clientesItem = MenuItem.of("Clientes", () -> {
            System.out.println("Abrir clientes");
            // router.spawnWindow("clientes");
        });
        
        // Criando menu com API fluida
        Menu menuCadastros = new Menu("Cadastros")
            .item(fornecedoresItem)
            .item(clientesItem)
            .item("Categorias", () -> System.out.println("Abrir categorias"));
        
        Menu menuGerencial = Menu.of("Gerencial")
            .item("Empresa", () -> System.out.println("Abrir empresa"));
        
        System.out.println("Menu criado com " + menuCadastros.getItems().size() + " itens:");
        for (MenuItem item : menuCadastros.getItems()) {
            System.out.println("  - " + item.getTitle());
        }
        
        System.out.println("\nMenu Gerencial criado com " + menuGerencial.getItems().size() + " itens:");
        for (MenuItem item : menuGerencial.getItems()) {
            System.out.println("  - " + item.getTitle());
        }
        
        System.out.println("\n=== Testando actions ===");
        System.out.println("Executando ação do item 'Fornecedores':");
        fornecedoresItem.getJavaFxMenuItem().getOnAction().handle(null);
        
        System.out.println("Executando ação do item 'Empresa':");
        menuGerencial.getItems().get(0).getJavaFxMenuItem().getOnAction().handle(null);
        
        System.out.println("\n=== Comparação com código original ===");
        System.out.println("Código original: ~15 linhas boilerplate");
        System.out.println("Nova API: ~6 linhas declarativas");
        System.out.println("Redução de código: ~60%");
        
        System.out.println("\n=== Exemplo de uso na aplicação cliente ===");
        System.out.println("return new MenuBar()");
        System.out.println("    .menu(\"Cadastros\",");
        System.out.println("        MenuItem.of(\"Fornecedores\", () -> router.spawnWindow(\"fornecedores\")),");
        System.out.println("        MenuItem.of(\"Clientes\", () -> router.spawnWindow(\"clientes\")),");
        System.out.println("        MenuItem.of(\"Categorias\", () -> router.spawnWindow(\"categorias\"))");
        System.out.println("    )");
        System.out.println("    .menu(\"Gerencial\",");
        System.out.println("        MenuItem.of(\"Empresa\", () -> router.spawnWindow(\"empresa\"))");
        System.out.println("    );");
    }
}