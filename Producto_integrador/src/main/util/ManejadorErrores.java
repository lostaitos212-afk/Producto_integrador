package main.util;
import javax.swing.JOptionPane;

public class ManejadorErrores {
    public static void registrarError(String mensajeUsuario, Exception e) {
        System.err.println("CRITICAL ERROR: " + e.getMessage());
        e.printStackTrace();
        JOptionPane.showMessageDialog(null, 
            mensajeUsuario + "\nDetalle: " + e.getMessage(), 
            "Error del Sistema", 
            JOptionPane.ERROR_MESSAGE);
    }
}