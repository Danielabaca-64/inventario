package vista;

import dao.CategoriaDAO;
import dao.ProductoDAO;
import modelo.Categoria;
import modelo.Producto;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

public class DialogoProducto extends JDialog {

    private final JTextField   txtCodigo      = new JTextField(20);
    private final JTextField   txtNombre      = new JTextField(30);
    private final JTextArea    txtDescripcion = new JTextArea(3, 30);
    private final JComboBox<Categoria> cbCategoria = new JComboBox<>();
    private final JTextField   txtPrecio      = new JTextField(12);
    private final JTextField   txtStock       = new JTextField(8);
    private final JCheckBox    chkActivo      = new JCheckBox("Producto activo", true);

    private final JButton btnGuardar  = new JButton("Guardar");
    private final JButton btnCancelar = new JButton("Cancelar");

    private final ProductoDAO  productoDAO  = new ProductoDAO();
    private final CategoriaDAO categoriaDAO = new CategoriaDAO();

    private Producto productoEditar = null;
    private boolean  guardado       = false;

    public DialogoProducto(Frame padre) {
        super(padre, "Nuevo Producto", true);
        construirUI();
        cargarCategorias();
    }

    public DialogoProducto(Frame padre, Producto producto) {
        super(padre, "Editar Producto", true);
        this.productoEditar = producto;
        construirUI();
        cargarCategorias();
        cargarDatos();
    }

    private void construirUI() {
        setResizable(false);
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(new EmptyBorder(16, 20, 12, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 4, 5, 4);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill   = GridBagConstraints.HORIZONTAL;

        agregarCampo(panel, gbc, 0, "Código *",     txtCodigo);
        agregarCampo(panel, gbc, 1, "Nombre *",     txtNombre);
        agregarCampo(panel, gbc, 2, "Categoría *",  cbCategoria);
        agregarCampo(panel, gbc, 3, "Precio ($) *", txtPrecio);
        agregarCampo(panel, gbc, 4, "Stock *",      txtStock);

        gbc.gridx = 0; gbc.gridy = 5; gbc.anchor = GridBagConstraints.NORTHWEST;
        panel.add(new JLabel("Descripción"), gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST;
        txtDescripcion.setLineWrap(true);
        txtDescripcion.setWrapStyleWord(true);
        panel.add(new JScrollPane(txtDescripcion), gbc);

        gbc.gridx = 1; gbc.gridy = 6;
        panel.add(chkActivo, gbc);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        estilizar(btnGuardar,  new Color(0x2563EB), Color.WHITE);
        estilizar(btnCancelar, new Color(0xE5E7EB), new Color(0x374151));
        panelBotones.add(btnCancelar);
        panelBotones.add(btnGuardar);

        gbc.gridx = 0; gbc.gridy = 7; gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.EAST; gbc.fill = GridBagConstraints.NONE;
        panel.add(panelBotones, gbc);

        add(panel);
        pack();
        setLocationRelativeTo(getOwner());

        btnGuardar.addActionListener(e -> guardar());
        btnCancelar.addActionListener(e -> dispose());
        getRootPane().setDefaultButton(btnGuardar);
    }

    private void agregarCampo(JPanel p, GridBagConstraints gbc, int fila,
                               String etiqueta, JComponent campo) {
        gbc.gridx = 0; gbc.gridy = fila; gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST; gbc.fill = GridBagConstraints.NONE;
        p.add(new JLabel(etiqueta), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        p.add(campo, gbc);
    }

    private void cargarCategorias() {
        try {
            List<Categoria> cats = categoriaDAO.listarTodas();
            cbCategoria.removeAllItems();
            for (Categoria c : cats) cbCategoria.addItem(c);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                "No se pudieron cargar las categorías:\n" + ex.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cargarDatos() {
        txtCodigo.setText(productoEditar.getCodigo());
        txtNombre.setText(productoEditar.getNombre());
        txtDescripcion.setText(productoEditar.getDescripcion());
        txtPrecio.setText(productoEditar.getPrecio().toPlainString());
        txtStock.setText(String.valueOf(productoEditar.getStock()));
        chkActivo.setSelected(productoEditar.isActivo());

        for (int i = 0; i < cbCategoria.getItemCount(); i++) {
            if (cbCategoria.getItemAt(i).getId() == productoEditar.getCategoriaId()) {
                cbCategoria.setSelectedIndex(i);
                break;
            }
        }
    }

    private void guardar() {
        String codigo = txtCodigo.getText().trim();
        String nombre = txtNombre.getText().trim();
        Categoria catSel = (Categoria) cbCategoria.getSelectedItem();

        if (codigo.isEmpty() || nombre.isEmpty() || catSel == null) {
            mostrarError("Código, nombre y categoría son obligatorios.");
            return;
        }
        if (codigo.length() > 20) { mostrarError("El código no puede superar 20 caracteres."); return; }
        if (nombre.length() > 100) { mostrarError("El nombre no puede superar 100 caracteres."); return; }

        BigDecimal precio;
        try {
            precio = new BigDecimal(txtPrecio.getText().trim().replace(",", "."));
            if (precio.compareTo(BigDecimal.ZERO) < 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            mostrarError("Precio inválido. Ingresa un número positivo (ej: 12500.00).");
            txtPrecio.requestFocus();
            return;
        }

        int stock;
        try {
            stock = Integer.parseInt(txtStock.getText().trim());
            if (stock < 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            mostrarError("Stock inválido. Debe ser un número entero >= 0.");
            txtStock.requestFocus();
            return;
        }

        Producto p = (productoEditar != null) ? productoEditar : new Producto();
        p.setCodigo(codigo);
        p.setNombre(nombre);
        p.setDescripcion(txtDescripcion.getText().trim());
        p.setCategoriaId(catSel.getId());
        p.setPrecio(precio);
        p.setStock(stock);
        p.setActivo(chkActivo.isSelected());

        try {
            boolean ok = (productoEditar == null)
                ? productoDAO.insertar(p)
                : productoDAO.actualizar(p);

            if (ok) { guardado = true; dispose(); }
            else mostrarError("No se pudo guardar. Intenta de nuevo.");

        } catch (SQLException ex) {
            if (ex.getErrorCode() == 1062) {
                mostrarError("Ya existe un producto con ese código.");
            } else {
                mostrarError("Error de base de datos:\n" + ex.getMessage());
            }
        }
    }

    public boolean isGuardado() { return guardado; }

    private void mostrarError(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Error de validación",
                JOptionPane.WARNING_MESSAGE);
    }

    private void estilizar(JButton btn, Color fondo, Color texto) {
        btn.setBackground(fondo);
        btn.setForeground(texto);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setOpaque(true);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setFont(btn.getFont().deriveFont(Font.BOLD));
        btn.setBorder(BorderFactory.createEmptyBorder(8, 18, 8, 18));
    }
}
