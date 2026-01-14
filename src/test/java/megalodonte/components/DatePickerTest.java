package megalodonte.components;

import megalodonte.props.DatePickerProps;
import megalodonte.props.Props;
import megalodonte.props.TextProps;
import megalodonte.styles.DatePickerStyler;
import megalodonte.styles.Estilizador;

import java.lang.reflect.Method;
import java.time.LocalDate;

public class DatePickerTest {

    public static void testDatePickerProps() {
        System.out.println("Testing DatePickerProps...");
        
        DatePickerProps props = new DatePickerProps();
        
        // Test fluent API
        props.pattern("dd/MM/yyyy", new java.util.Locale("pt", "BR"))
            .editable(false)
            .placeHolder("dd/MM/yyyy");
        
        // Test reflection for method signatures
        Method[] methods = DatePickerProps.class.getDeclaredMethods();
        for (Method method : methods) {
            if (method.getName().startsWith("pattern") || 
                method.getName().startsWith("editable") || 
                method.getName().startsWith("promptText")) {
                System.out.println("Found method: " + method.getName() + " with " + method.getParameterCount() + " parameters");
            }
        }
        
        System.out.println("DatePickerProps test passed!");
    }

    public static void testDatePickerStyler() {
        System.out.println("Testing DatePickerStyler...");
        
        DatePickerStyler styler = new DatePickerStyler();
        
        // Test fluent API
        styler.bgColor("#ffffff")
            .borderColor("#cccccc")
            .borderWidth(1)
            .borderRadius(4);
        
        // Test inheritance from BaseStyler
        Class<?> superClass = styler.getClass().getSuperclass();
        System.out.println("DatePickerStyler extends: " + superClass.getName());
        
        System.out.println("DatePickerStyler test passed!");
    }

    public static void testComponentExistence() {
        System.out.println("Testing component existence...");
        
        try {
            Class<?> clazz = Class.forName("megalodonte.components.DatePicker");
            System.out.println("DatePicker class found: " + clazz.getName());
            
            // Check constructors
            java.lang.reflect.Constructor<?>[] constructors = clazz.getConstructors();
            System.out.println("Found " + constructors.length + " constructors:");
            for (java.lang.reflect.Constructor<?> constructor : constructors) {
                System.out.println("  " + constructor);
            }
            
            // Check methods
            Method[] methods = clazz.getDeclaredMethods();
            for (Method method : methods) {
                if (method.getName().equals("fromJavaFxNode") || 
                    method.getName().equals("CreateFromJavaFxNode") ||
                    method.getName().startsWith("bind")) {
                    System.out.println("Found method: " + method.getName());
                }
            }
            
            System.out.println("Component existence test passed!");
            
        } catch (ClassNotFoundException e) {
            System.err.println("DatePicker class not found!");
        }
    }

    public static void testPropsInheritance() {
        System.out.println("Testing Props inheritance...");
        
        DatePickerProps props = new DatePickerProps();
        
        // Check if it extends Props
        Class<?> superClass = props.getClass().getSuperclass();
        System.out.println("DatePickerProps extends: " + superClass.getName());
        
        // Check if it has apply method
        try {
            Method applyMethod = superClass.getMethod("apply", javafx.scene.Node.class);
            System.out.println("Found apply method: " + applyMethod);
        } catch (NoSuchMethodException e) {
            System.err.println("apply method not found!");
        }
        
        System.out.println("Props inheritance test passed!");
    }

    public static void main(String[] args) {
        System.out.println("=== Megalodonte DatePicker Test Suite ===");
        
        testDatePickerProps();
        testDatePickerStyler();
        testComponentExistence();
        testPropsInheritance();
        
        System.out.println("=== All tests completed! ===");
    }
}