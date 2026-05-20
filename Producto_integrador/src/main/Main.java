package main;

//import com.formdev.flatlaf.FlatDarkLaf;
import main.view.LoginView;
import main.controller.LoginController;
import javax.swing.SwingUtilities;
import java.sql.Connection;
import main.util.ConexionBD;

public class Main {
    public static void main(String[] args) {
        // 1. Configuración de estilo moderno (FlatLaf)
    	Connection test = ConexionBD.getConexion();

    	if(test != null) {
    	    System.out.println("CONECTADO A MYSQL");
    	} else {
    	    System.out.println("NO SE PUDO CONECTAR");
    	}
       // FlatDarkLaf.setup();
        
        // 2. Iniciar en el hilo de eventos de Swing
        SwingUtilities.invokeLater(() -> {
            try {
                LoginView loginView = new LoginView();
                // El controlador debe ser creado después de la vista
                new LoginController(loginView);
                loginView.setVisible(true);
            } catch (Exception e) {
                // Si algo falla al arrancar, lo veremos aquí sin que truene el programa
                e.printStackTrace();
            }
        });
    }
    
}