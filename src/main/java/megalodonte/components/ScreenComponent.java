package megalodonte.components;

public interface ScreenComponent {
    Component render();
    
    default void onMount() {
        // Implementação vazia - método opcional
    }
}
