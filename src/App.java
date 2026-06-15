import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class App {

    public static void main(String[] args) {

        new VentanaPrincipal();        

        System.out.println("Iniciando InventarioTech...");

        if (ConexionBD.conectarBD() != null) {
            System.out.println("Sistema conectado correctamente");
        }

        registrarProducto(
                "LAP003",
                "Lenovo IdeaPad",
                "16GB RAM",
                1,
                3200.00,
                10);

        listarProductos();

        alertaStock();
    }

    public static void registrarProducto(
            String codigo,
            String nombre,
            String descripcion,
            int categoria,
            double precio,
            int stock) {

        String sql =
                "INSERT INTO productos " +
                "(codigo,nombre,descripcion,categoria_id,precio,stock) " +
                "VALUES (?,?,?,?,?,?)";

        try {

            Connection con = ConexionBD.conectarBD();

            PreparedStatement ps =
                    con.prepareStatement(sql);

            ps.setString(1, codigo);
            ps.setString(2, nombre);
            ps.setString(3, descripcion);
            ps.setInt(4, categoria);
            ps.setDouble(5, precio);
            ps.setInt(6, stock);

            int registro = ps.executeUpdate();

            if (registro > 0) {
                System.out.println("Producto registrado correctamente");
            }

        } catch (SQLException e) {

            System.out.println(
                    "Error al registrar producto: "
                    + e.getMessage());
        }
    }

    public static void listarProductos() {

        String sql =
                "SELECT * FROM productos WHERE activo = true";

        try {

            Connection con =
                    ConexionBD.conectarBD();

            PreparedStatement ps =
                    con.prepareStatement(sql);

            ResultSet rs =
                    ps.executeQuery();

            System.out.println("\nLISTA DE PRODUCTOS");

            while (rs.next()) {

                System.out.println(
                        rs.getInt("id")
                        + " | "
                        + rs.getString("codigo")
                        + " | "
                        + rs.getString("nombre")
                        + " | $"
                        + rs.getDouble("precio")
                        + " | Stock: "
                        + rs.getInt("stock"));
            }

        } catch (SQLException e) {

            System.out.println(
                    "Error al listar productos: "
                    + e.getMessage());
        }
    }

    public static void buscarPorCategoria(
            int categoriaId) {

        String sql =
                "SELECT * FROM productos WHERE categoria_id=?";

        try {

            Connection con =
                    ConexionBD.conectarBD();

            PreparedStatement ps =
                    con.prepareStatement(sql);

            ps.setInt(1, categoriaId);

            ResultSet rs =
                    ps.executeQuery();

            while (rs.next()) {

                System.out.println(
                        rs.getString("nombre"));
            }

        } catch (SQLException e) {

            System.out.println(
                    "Error: "
                    + e.getMessage());
        }
    }

    public static void actualizarStock(
            int id,
            int nuevoStock) {

        String sql =
                "UPDATE productos SET stock=? WHERE id=?";

        try {

            Connection con =
                    ConexionBD.conectarBD();

            PreparedStatement ps =
                    con.prepareStatement(sql);

            ps.setInt(1, nuevoStock);
            ps.setInt(2, id);

            ps.executeUpdate();

            System.out.println(
                    "Stock actualizado correctamente");

        } catch (SQLException e) {

            System.out.println(
                    "Error: "
                    + e.getMessage());
        }
    }

    public static void alertaStock() {

        String sql =
                "SELECT * FROM productos WHERE stock < 2";

        try {

            Connection con =
                    ConexionBD.conectarBD();

            PreparedStatement ps =
                    con.prepareStatement(sql);

            ResultSet rs =
                    ps.executeQuery();

            while (rs.next()) {

                System.out.println(
                        "ALERTA -> "
                        + rs.getString("nombre")
                        + " tiene stock "
                        + rs.getInt("stock"));
            }

        } catch (SQLException e) {

            System.out.println(
                    "Error: "
                    + e.getMessage());
        }
    }

    public static void eliminarProducto(
            int id) {

        String sql =
                "UPDATE productos SET activo = false WHERE id=?";

        try {

            Connection con =
                    ConexionBD.conectarBD();

            PreparedStatement ps =
                    con.prepareStatement(sql);

            ps.setInt(1, id);

            ps.executeUpdate();

            System.out.println(
                    "Producto eliminado correctamente");

        } catch (SQLException e) {

            System.out.println(
                    "Error: "
                    + e.getMessage());
        }
    }
}