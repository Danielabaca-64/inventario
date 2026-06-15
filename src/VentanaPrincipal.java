import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class VentanaPrincipal extends JFrame {

    private JTextField txtCodigo;
    private JTextField txtNombre;
    private JTextField txtPrecio;
    private JTextField txtStock;

    private JButton btnGuardar;
    private JButton btnMostrar;
    private JButton btnVender;
    private JButton btnEliminar;

    private JComboBox<String> cbCategoria;

    private JTable tabla;
    private DefaultTableModel modelo;

    public VentanaPrincipal() {

        setTitle("InventarioTech");
        setSize(700, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        JLabel lblCodigo = new JLabel("Código:");
        lblCodigo.setBounds(20, 20, 100, 25);
        add(lblCodigo);

        txtCodigo = new JTextField();
        txtCodigo.setBounds(120, 20, 250, 25);
        add(txtCodigo);

        JLabel lblNombre = new JLabel("Nombre:");
        lblNombre.setBounds(20, 60, 100, 25);
        add(lblNombre);

        txtNombre = new JTextField();
        txtNombre.setBounds(120, 60, 250, 25);
        add(txtNombre);

        JLabel lblPrecio = new JLabel("Precio:");
        lblPrecio.setBounds(20, 100, 100, 25);
        add(lblPrecio);

        txtPrecio = new JTextField();
        txtPrecio.setBounds(120, 100, 250, 25);
        add(txtPrecio);

        JLabel lblStock = new JLabel("Stock:");
        lblStock.setBounds(20, 140, 100, 25);
        add(lblStock);

        txtStock = new JTextField();
        txtStock.setBounds(120, 140, 250, 25);
        add(txtStock);

        JLabel lblCategoria = new JLabel("Categoría:");
        lblCategoria.setBounds(20, 180, 100, 25);
        add(lblCategoria);

        cbCategoria = new JComboBox<>();
        cbCategoria.addItem("Laptops");
        cbCategoria.addItem("Smartphones");
        cbCategoria.addItem("Accesorios");
        cbCategoria.addItem("Componentes");

        cbCategoria.setBounds(120, 180, 250, 25);
        add(cbCategoria);

        btnGuardar = new JButton("Guardar");
        btnGuardar.setBounds(20, 230, 120, 40);
        add(btnGuardar);

        btnMostrar = new JButton("Mostrar");
        btnMostrar.setBounds(150, 230, 120, 40);
        add(btnMostrar);

        btnVender = new JButton("Vender");
        btnVender.setBounds(280, 230, 120, 40);
        add(btnVender);

        btnEliminar = new JButton("Eliminar");
        btnEliminar.setBounds(410, 230, 120, 40);
        add(btnEliminar);

        modelo = new DefaultTableModel();

        modelo.addColumn("ID");
        modelo.addColumn("Código");
        modelo.addColumn("Nombre");
        modelo.addColumn("Precio");
        modelo.addColumn("Stock");

        tabla = new JTable(modelo);

        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBounds(20, 290, 640, 180);

        add(scroll);

        btnGuardar.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                try {

                    String codigo = txtCodigo.getText();
                    String nombre = txtNombre.getText();

                    double precio =
                            Double.parseDouble(txtPrecio.getText());

                    int stock =
                            Integer.parseInt(txtStock.getText());

                    int categoria = 1;

                    if (cbCategoria.getSelectedItem().equals("Smartphones"))
                        categoria = 2;

                    if (cbCategoria.getSelectedItem().equals("Accesorios"))
                        categoria = 3;

                    if (cbCategoria.getSelectedItem().equals("Componentes"))
                        categoria = 4;

                    App.registrarProducto(
                            codigo,
                            nombre,
                            "Producto registrado desde Swing",
                            categoria,
                            precio,
                            stock);

                    JOptionPane.showMessageDialog(
                            null,
                            "Producto guardado correctamente");

                    txtCodigo.setText("");
                    txtNombre.setText("");
                    txtPrecio.setText("");
                    txtStock.setText("");

                    cargarProductos();

                } catch (Exception ex) {

                    JOptionPane.showMessageDialog(
                            null,
                            ex.getMessage());
                }
            }
        });

        btnMostrar.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                cargarProductos();
            }
        });

        btnVender.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                try {

                    String idTexto =
                            JOptionPane.showInputDialog(
                                    "Ingrese ID del producto");

                    String stockTexto =
                            JOptionPane.showInputDialog(
                                    "Nuevo stock");

                    int id =
                            Integer.parseInt(idTexto);

                    int stock =
                            Integer.parseInt(stockTexto);

                    App.actualizarStock(id, stock);

                    JOptionPane.showMessageDialog(
                            null,
                            "Stock actualizado");

                    cargarProductos();

                } catch (Exception ex) {

                    JOptionPane.showMessageDialog(
                            null,
                            ex.getMessage());
                }
            }
        });

        btnEliminar.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                try {

                    String idTexto =
                            JOptionPane.showInputDialog(
                                    "ID del producto a eliminar");

                    int id =
                            Integer.parseInt(idTexto);

                    App.eliminarProducto(id);

                    JOptionPane.showMessageDialog(
                            null,
                            "Producto eliminado");

                    cargarProductos();

                } catch (Exception ex) {

                    JOptionPane.showMessageDialog(
                            null,
                            ex.getMessage());
                }
            }
        });

        setVisible(true);
    }

    public void cargarProductos() {

        modelo.setRowCount(0);

        String sql =
                "SELECT * FROM productos WHERE activo = true";

        try {

            Connection con =
                    ConexionBD.conectarBD();

            PreparedStatement ps =
                    con.prepareStatement(sql);

            ResultSet rs =
                    ps.executeQuery();

            while (rs.next()) {

                Object[] fila = {
                        rs.getInt("id"),
                        rs.getString("codigo"),
                        rs.getString("nombre"),
                        rs.getDouble("precio"),
                        rs.getInt("stock")
                };

                modelo.addRow(fila);

                if (rs.getInt("stock") < 2) {

                    JOptionPane.showMessageDialog(
                            null,
                            "ALERTA: "
                                    + rs.getString("nombre")
                                    + " tiene stock menor a 2");
                }
            }

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    null,
                    e.getMessage());
        }
    }
}