package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {

    private static final String URL =
            "jdbc:mysql://localhost:3306/inventariotech"
            + "?useSSL=false"
            + "&allowPublicKeyRetrieval=true"
            + "&serverTimezone=America/Bogota";

    private static final String USUARIO = "root";
    private static final String PASSWORD = "123456";

    private static Connection instancia = null;

  public static Connection getConexion() throws SQLException {

    System.out.println("================================");
    System.out.println("URL      = " + URL);
    System.out.println("USUARIO  = " + USUARIO);
    System.out.println("PASSWORD = " + PASSWORD);
    System.out.println("================================");

    if (instancia == null || instancia.isClosed()) {
        instancia = DriverManager.getConnection(URL, USUARIO, PASSWORD);
    }

    return instancia;
}

    public static void cerrar() {
        if (instancia != null) {
            try {
                instancia.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private Conexion() {
    }
}