package vista;

import dao.CategoriaDAO;
import modelo.Categoria;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.sql.SQLException;

public class DialogoCategoria extends JDialog {

    private final JTextField txtNombre      = new JTextField(25);
    private final JTextArea  txtDescripcion = new JTextArea(3, 25);
    private final JButton    btnGuardar     = new JButton("Guardar");
    private final JButton    btnCancelar    = new JButton("Cancelar");

    private final CategoriaDAO dao   = new CategoriaDAO();
    private Categoria categoriaEditar = null; // null → modo creación
    private boolean    guardado       = false;

    public DialogoCategoria(Frame padre) {
        super(padre, "Nueva Categoría", true);
        construirUI();
    }

    public DialogoCategoria(Frame padre, Categoria categoria) {
        super(padre, "Editar Categoría", true);
        this.categoriaEditar = categoria;
        construirUI();
        cargarDatos();
    }

    private void construirUI() {
        setResizable(false);
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(new EmptyBorder(16, 20, 12, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets  = new Insets(6, 4, 6, 4);
        gbc.anchor  = GridBagConstraints.WEST;
        gbc.fill    = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Nombre *"), gbc);
        gbc.gridx = 1;
        panel.add(txtNombre, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.anchor = GridBagConstraints.NORTHWEST;
        panel.add(new JLabel("Descripción"), gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST;
        txtDescripcion.setLineWrap(true);
        txtDescripcion.setWrapStyleWord(true);
        panel.add(new JScrollPane(txtDescripcion), gbc);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        estilizar(btnGuardar,  new Color(0x2563EB), Color.WHITE);
        estilizar(btnCancelar, new Color(0xE5E7EB), new Color(0x374151));
        panelBotones.add(btnCancelar);
        panelBotones.add(btnGuardar);

        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.EAST; gbc.fill = GridBagConstraints.NONE;
        panel.add(panelBotones, gbc);

        add(panel);
        pack();
        setLocationRelativeTo(getOwner());

        btnGuardar.addActionListener(e -> guardar());
        btnCancelar.addActionListener(e -> dispose());
        getRootPane().setDefaultButton(btnGuardar);
    }

    private void cargarDatos() {
        txtNombre.setText(categoriaEditar.getNombre());
        txtDescripcion.setText(categoriaEditar.getDescripcion());
    }

    private void guardar() {
        String nombre = txtNombre.getText().trim();
        if (nombre.isEmpty()) {
            mostrarError("El nombre es obligatorio.");
            txtNombre.requestFocus();
            return;
        }
        if (nombre.length() > 50) {
            mostrarError("El nombre no puede superar 50 caracteres.");
            return;
        }

        Categoria c = (categoriaEditar != null) ? categoriaEditar : new Categoria();
        c.setNombre(nombre);
        c.setDescripcion(txtDescripcion.getText().trim());

        try {
            boolean ok = (categoriaEditar == null)
                ? dao.insertar(c)
                : dao.actualizar(c);

            if (ok) {
                guardado = true;
                dispose();
            } else {
                mostrarError("No se pudo guardar. Intenta de nuevo.");
            }
        } catch (SQLException ex) {
            if (ex.getErrorCode() == 1062) { // Duplicate entry
                mostrarError("Ya existe una categoría con ese nombre.");
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
