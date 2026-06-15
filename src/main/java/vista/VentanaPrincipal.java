package vista;

import dao.CategoriaDAO;
import dao.ProductoDAO;
import modelo.Categoria;
import modelo.Producto;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

/**
 * Ventana principal de la aplicación InventarioTech.
 */
public class VentanaPrincipal extends JFrame {

    private final ProductoDAO  productoDAO  = new ProductoDAO();
    private final CategoriaDAO categoriaDAO = new CategoriaDAO();

    private final String[] COL_PRODUCTO = { "ID", "Código", "Nombre", "Categoría",
                                            "Precio", "Stock", "Activo", "Fecha Registro" };
    private final DefaultTableModel modeloProductos = new DefaultTableModel(COL_PRODUCTO, 0) {
        @Override public boolean isCellEditable(int r, int c) { return false; }
    };
    private final JTable tablaProductos = new JTable(modeloProductos);
    private final JTextField txtBuscar  = new JTextField(22);

    private final String[] COL_CAT  = { "ID", "Nombre", "Descripción" };
    private final DefaultTableModel modeloCategorias = new DefaultTableModel(COL_CAT, 0) {
        @Override public boolean isCellEditable(int r, int c) { return false; }
    };
    private final JTable tablaCategorias = new JTable(modeloCategorias);

    public VentanaPrincipal() {
        setTitle("InventarioTech – Gestión de Inventario");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(960, 560));
        setLocationRelativeTo(null);

        add(crearHeader(), BorderLayout.NORTH);

        JTabbedPane pestañas = new JTabbedPane();
        pestañas.addTab("📦 Productos",   crearPanelProductos());
        pestañas.addTab("🏷  Categorías", crearPanelCategorias());
        pestañas.setBorder(new EmptyBorder(0, 8, 8, 8));
        add(pestañas, BorderLayout.CENTER);

        cargarProductos();
        cargarCategorias();

        pack();
    }

    private JPanel crearHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(0x1E3A5F));
        header.setBorder(new EmptyBorder(14, 20, 14, 20));
        JLabel titulo = new JLabel("InventarioTech");
        titulo.setForeground(Color.WHITE);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        JLabel sub = new JLabel("Sistema de gestión de inventario");
        sub.setForeground(new Color(0xA8C4E0));
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        JPanel textos = new JPanel(new GridLayout(2, 1));
        textos.setOpaque(false);
        textos.add(titulo);
        textos.add(sub);
        header.add(textos, BorderLayout.WEST);
        return header;
    }

    private JPanel crearPanelProductos() {
        JPanel panel = new JPanel(new BorderLayout(0, 8));
        panel.setBorder(new EmptyBorder(12, 0, 0, 0));

        JPanel barra = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        JButton btnNuevo    = boton("＋ Nuevo",    new Color(0x16A34A), Color.WHITE);
        JButton btnEditar   = boton("✎ Editar",   new Color(0x2563EB), Color.WHITE);
        JButton btnEliminar = boton("✕ Eliminar", new Color(0xDC2626), Color.WHITE);
        JButton btnRefresh  = boton("↺ Actualizar", new Color(0x6B7280), Color.WHITE);
        barra.add(btnNuevo); barra.add(btnEditar); barra.add(btnEliminar);
        barra.add(new JSeparator(JSeparator.VERTICAL));
        barra.add(new JLabel("Buscar:"));
        barra.add(txtBuscar);
        JButton btnBuscar = boton("Buscar", new Color(0x374151), Color.WHITE);
        barra.add(btnBuscar);
        barra.add(btnRefresh);
        panel.add(barra, BorderLayout.NORTH);

        tablaProductos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaProductos.setRowHeight(26);
        tablaProductos.getTableHeader().setReorderingAllowed(false);
        tablaProductos.getColumnModel().getColumn(0).setMaxWidth(45);
        tablaProductos.getColumnModel().getColumn(1).setPreferredWidth(90);
        tablaProductos.getColumnModel().getColumn(4).setPreferredWidth(80);
        tablaProductos.getColumnModel().getColumn(5).setMaxWidth(60);
        tablaProductos.getColumnModel().getColumn(6).setMaxWidth(60);
        panel.add(new JScrollPane(tablaProductos), BorderLayout.CENTER);

        btnNuevo.addActionListener(e -> {
            DialogoProducto d = new DialogoProducto(this);
            d.setVisible(true);
            if (d.isGuardado()) cargarProductos();
        });
        btnEditar.addActionListener(e -> editarProductoSeleccionado());
        btnEliminar.addActionListener(e -> eliminarProductoSeleccionado());
        btnBuscar.addActionListener(e -> buscarProductos());
        btnRefresh.addActionListener(e -> { txtBuscar.setText(""); cargarProductos(); });
        txtBuscar.addActionListener(e -> buscarProductos());
        tablaProductos.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2) editarProductoSeleccionado();
            }
        });

        return panel;
    }

    private void cargarProductos() {
        try {
            List<Producto> lista = productoDAO.listarTodos();
            refrescarTablaProductos(lista);
        } catch (SQLException ex) {
            error("Error cargando productos", ex);
        }
    }

    private void buscarProductos() {
        String term = txtBuscar.getText().trim();
        if (term.isEmpty()) { cargarProductos(); return; }
        try {
            refrescarTablaProductos(productoDAO.buscar(term));
        } catch (SQLException ex) {
            error("Error en búsqueda", ex);
        }
    }

    private void refrescarTablaProductos(List<Producto> lista) {
        modeloProductos.setRowCount(0);
        for (Producto p : lista) {
            modeloProductos.addRow(new Object[]{
                p.getId(), p.getCodigo(), p.getNombre(), p.getCategoriaNombre(),
                "$" + (p.getPrecio() != null ? p.getPrecio().toPlainString() : "0"), p.getStock(),
                p.isActivo() ? "Sí" : "No",
                p.getFechaRegistro() != null ? p.getFechaRegistro().toString().substring(0,10) : ""
            });
        }
    }

    private void editarProductoSeleccionado() {
        int fila = tablaProductos.getSelectedRow();
        if (fila < 0) { JOptionPane.showMessageDialog(this, "Selecciona un producto primero."); return; }
        int id = (int) modeloProductos.getValueAt(fila, 0);
        try {
            Producto p = productoDAO.buscarPorId(id);
            if (p == null) return;
            DialogoProducto d = new DialogoProducto(this, p);
            d.setVisible(true);
            if (d.isGuardado()) cargarProductos();
        } catch (SQLException ex) {
            error("Error cargando producto", ex);
        }
    }

    private void eliminarProductoSeleccionado() {
        int fila = tablaProductos.getSelectedRow();
        if (fila < 0) { JOptionPane.showMessageDialog(this, "Selecciona un producto primero."); return; }
        String nombre = (String) modeloProductos.getValueAt(fila, 2);
        int resp = JOptionPane.showConfirmDialog(this,
            "¿Eliminar el producto «" + nombre + "»? Esta acción no se puede deshacer.",
            "Confirmar eliminación", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (resp != JOptionPane.YES_OPTION) return;
        int id = (int) modeloProductos.getValueAt(fila, 0);
        try {
            if (productoDAO.eliminar(id)) cargarProductos();
        } catch (SQLException ex) {
            error("Error eliminando producto", ex);
        }
    }

    private JPanel crearPanelCategorias() {
        JPanel panel = new JPanel(new BorderLayout(0, 8));
        panel.setBorder(new EmptyBorder(12, 0, 0, 0));

        JPanel barra = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        JButton btnNuevo    = boton("＋ Nueva",    new Color(0x16A34A), Color.WHITE);
        JButton btnEditar   = boton("✎ Editar",   new Color(0x2563EB), Color.WHITE);
        JButton btnEliminar = boton("✕ Eliminar", new Color(0xDC2626), Color.WHITE);
        barra.add(btnNuevo); barra.add(btnEditar); barra.add(btnEliminar);
        panel.add(barra, BorderLayout.NORTH);

        tablaCategorias.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaCategorias.setRowHeight(26);
        tablaCategorias.getTableHeader().setReorderingAllowed(false);
        tablaCategorias.getColumnModel().getColumn(0).setMaxWidth(45);
        tablaCategorias.getColumnModel().getColumn(1).setPreferredWidth(150);
        panel.add(new JScrollPane(tablaCategorias), BorderLayout.CENTER);

        btnNuevo.addActionListener(e -> {
            DialogoCategoria d = new DialogoCategoria(this);
            d.setVisible(true);
            if (d.isGuardado()) cargarCategorias();
        });
        btnEditar.addActionListener(e -> editarCategoriaSeleccionada());
        btnEliminar.addActionListener(e -> eliminarCategoriaSeleccionada());
        tablaCategorias.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2) editarCategoriaSeleccionada();
            }
        });

        return panel;
    }

    private void cargarCategorias() {
        try {
            List<Categoria> lista = categoriaDAO.listarTodas();
            modeloCategorias.setRowCount(0);
            for (Categoria c : lista) {
                modeloCategorias.addRow(new Object[]{ c.getId(), c.getNombre(), c.getDescripcion() });
            }
        } catch (SQLException ex) {
            error("Error cargando categorías", ex);
        }
    }

    private void editarCategoriaSeleccionada() {
        int fila = tablaCategorias.getSelectedRow();
        if (fila < 0) { JOptionPane.showMessageDialog(this, "Selecciona una categoría primero."); return; }
        int id = (int) modeloCategorias.getValueAt(fila, 0);
        try {
            Categoria c = categoriaDAO.buscarPorId(id);
            if (c == null) return;
            DialogoCategoria d = new DialogoCategoria(this, c);
            d.setVisible(true);
            if (d.isGuardado()) cargarCategorias();
        } catch (SQLException ex) {
            error("Error cargando categoría", ex);
        }
    }

    private void eliminarCategoriaSeleccionada() {
        int fila = tablaCategorias.getSelectedRow();
        if (fila < 0) { JOptionPane.showMessageDialog(this, "Selecciona una categoría primero."); return; }
        String nombre = (String) modeloCategorias.getValueAt(fila, 1);
        int resp = JOptionPane.showConfirmDialog(this,
            "¿Eliminar la categoría «" + nombre + "»?\n" +
            "Sólo es posible si no tiene productos asociados.",
            "Confirmar eliminación", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (resp != JOptionPane.YES_OPTION) return;
        int id = (int) modeloCategorias.getValueAt(fila, 0);
        try {
            if (categoriaDAO.eliminar(id)) cargarCategorias();
        } catch (SQLException ex) {
            if (ex.getErrorCode() == 1451) { // FK violation
                JOptionPane.showMessageDialog(this,
                    "No puedes eliminar esta categoría porque tiene productos asociados.",
                    "Operación no permitida", JOptionPane.WARNING_MESSAGE);
            } else {
                error("Error eliminando categoría", ex);
            }
        }
    }

    private JButton boton(String texto, Color fondo, Color letra) {
        JButton btn = new JButton(texto);
        btn.setBackground(fondo);
        btn.setForeground(letra);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setOpaque(true);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setFont(btn.getFont().deriveFont(Font.BOLD, 12f));
        btn.setBorder(BorderFactory.createEmptyBorder(7, 14, 7, 14));
        return btn;
    }

    private void error(String contexto, Exception ex) {
        JOptionPane.showMessageDialog(this,
            contexto + ":\n" + ex.getMessage(),
            "Error", JOptionPane.ERROR_MESSAGE);
    }
}
