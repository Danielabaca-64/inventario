package app;

import util.Conexion;
import vista.VentanaPrincipal;

import javax.swing.*;
import java.sql.SQLException;

/**
 * Punto de entrada de InventarioTech.
 */
public class Main {

    public static void main(String[] args) {

          System.out.println("INICIO DEL PROGRAMA");

    try {
        Conexion.getConexion();
        System.out.println("CONEXION EXITOSA");
    } catch (Exception e) {
        e.printStackTrace();
    }



        // 1. Aplicar Look & Feel del sistema operativo
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        // 2. Verificar la conexión a MySQL antes de abrir la UI
       try {
    java.sql.Connection c =
        java.sql.DriverManager.getConnection(
            "jdbc:mysql://localhost:3306/inventariotech?useSSL=false&allowPublicKeyRetrieval=true",
            "inventario",
            "123456"
        );

    System.out.println("CONEXION EXITOSA");
} catch (Exception e) {
    e.printStackTrace();
}
        // 3. Lanzar la ventana en el hilo de Swing (EDT)
        SwingUtilities.invokeLater(() -> {
            VentanaPrincipal ventana = new VentanaPrincipal();
            ventana.setVisible(true);
        });

        // 4. Cerrar la conexión al salir
        Runtime.getRuntime().addShutdownHook(new Thread(Conexion::cerrar));
    }
}
