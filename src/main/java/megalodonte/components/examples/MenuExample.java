package megalodonte.components.examples;

import megalodonte.components.Menu;
import megalodonte.components.MenuBar;
import megalodonte.components.MenuItem;

public class MenuExample {
    public static MenuBar createSampleMenu() {
        // Exemplo 1: Criando itens e menus separadamente
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
        
        // Criando MenuBar com API fluida
        return new MenuBar()
            .menu("Preferências") // menu vazio
            .menu(menuCadastros)
            .menu(menuGerencial);
    }
    
    public static MenuBar createCompactMenu() {
        // Exemplo 2: API mais compacta
        return MenuBar.of()
            .menu("Cadastros", 
                MenuItem.of("Fornecedores", () -> System.out.println("Fornecedores")),
                MenuItem.of("Clientes", () -> System.out.println("Clientes")),
                MenuItem.of("Categorias", () -> System.out.println("Categorias"))
            )
            .menu("Gerencial",
                MenuItem.of("Empresa", () -> System.out.println("Empresa"))
            );
    }
    
    public static void demonstrateUsage() {
        System.out.println("=== Demonstração da API de Menu ===\n");
        
        // Criando menu do exemplo 1
        MenuBar menuBar1 = createSampleMenu();
        System.out.println("MenuBar 1 criado com " + menuBar1.getMenus().size() + " menus:");
        for (Menu menu : menuBar1.getMenus()) {
            System.out.println("  - " + menu.getTitle() + " (" + menu.getItems().size() + " itens)");
        }
        
        System.out.println();
        
        // Criando menu do exemplo 2
        MenuBar menuBar2 = createCompactMenu();
        System.out.println("MenuBar 2 criado com " + menuBar2.getMenus().size() + " menus:");
        for (Menu menu : menuBar2.getMenus()) {
            System.out.println("  - " + menu.getTitle() + " (" + menu.getItems().size() + " itens)");
        }
        
        System.out.println("\n=== Comparação com código original ===");
        System.out.println("Código original: ~15 linhas boilerplate");
        System.out.println("Nova API: ~6 linhas declarativas");
        System.out.println("Redução de código: ~60%");
        
        // Para usar em uma aplicação JavaFX real:
        // VBox root = new VBox(menuBar1.getJavaFxMenuBar());
        // Scene scene = new Scene(root, 800, 600);
        // primaryStage.setScene(scene);
        // primaryStage.show();
    }
}