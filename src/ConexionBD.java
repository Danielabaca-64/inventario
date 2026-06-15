import java.sql.Connection;
import java.sql.DriverManager;

public class ConexionBD {

    public static Connection conectarBD() {

        Connection con = null;

        String host = "localhost";
        int puerto = 3306;
        String usuario = "root";
        String contrasena = "";
        String nombreBD = "inventariotech";

        try {

            Class.forName("com.mysql.cj.jdbc.Driver");

            String url =
                "jdbc:mysql://" +
                host + ":" +
                puerto + "/" +
                nombreBD;

            con = DriverManager.getConnection(
                    url,
                    usuario,
                    contrasena);

            System.out.println("Conexion Exitosa");

        } catch (Exception e) {

            System.out.println(
                    "Error: " +
                    e.getMessage());
        }

        return con;
    }
}