package megalodonte.components;

import megalodonte.components.Menu;
import megalodonte.components.MenuBar;
import megalodonte.components.MenuItem;

public class MenuTest {
    public static void main(String[] args) {
        // Exemplo de uso da nova API de menu
        
        // Criando itens de menu
        MenuItem fornecedoresItem = new MenuItem("Fornecedores", () -> {
            System.out.println("Abrir fornecedores");
            // router.spawnWindow("fornecedores");
        });
        
        MenuItem clientesItem = MenuItem.of("Clientes", () -> {
            System.out.println("Abrir clientes");
            // router.spawnWindow("clientes");
        });
        
        MenuItem categoriasItem = MenuItem.of("Categorias", () -> {
            System.out.println("Abrir categorias");
            // router.spawnWindow("categorias");
        });
        
        MenuItem empresaItem = new MenuItem("Empresa", () -> {
            System.out.println("Abrir empresa");
            // router.spawnWindow("empresa");
        });
        
        // Criando menus
        Menu menuCadastros = new Menu("Cadastros")
            .item(fornecedoresItem)
            .item(clientesItem)
            .item(categoriasItem);
        
        Menu menuGerencial = Menu.of("Gerencial")
            .item(empresaItem);
        
        Menu menuPreferencias = new Menu("Preferências");
        
        // Criando MenuBar com API fluida
        MenuBar menuBar = new MenuBar()
            .menu(menuPreferencias)
            .menu(menuCadastros)
            .menu(menuGerencial);
        
        // Ou usando o método direto
        MenuBar menuBar2 = MenuBar.of()
            .menu("Cadastros", 
                MenuItem.of("Fornecedores", () -> System.out.println("Fornecedores")),
                MenuItem.of("Clientes", () -> System.out.println("Clientes")),
                MenuItem.of("Categorias", () -> System.out.println("Categorias"))
            )
            .menu("Gerencial",
                MenuItem.of("Empresa", () -> System.out.println("Empresa"))
            );
        
        System.out.println("Menu criado com sucesso!");
        System.out.println("MenuBar 1 tem " + menuBar.getMenus().size() + " menus");
        System.out.println("MenuBar 2 tem " + menuBar2.getMenus().size() + " menus");
        
        // Para usar em uma aplicação JavaFX real:
        // VBox root = new VBox(menuBar.getJavaFxMenuBar());
        // Scene scene = new Scene(root, 800, 600);
        // primaryStage.setScene(scene);
        // primaryStage.show();
    }
}