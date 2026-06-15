package dao;

import modelo.Producto;
import util.Conexion;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Operaciones CRUD sobre la tabla `productos`.
 */
public class ProductoDAO {

    private static final String SELECT_BASE =
        "SELECT p.id, p.codigo, p.nombre, p.descripcion, p.categoria_id, " +
        "       c.nombre AS categoria_nombre, p.precio, p.stock, p.activo, p.fecha_registro " +
        "FROM productos p " +
        "JOIN categorias c ON p.categoria_id = c.id ";

    // ── CREATE ────────────────────────────────────────────────
    public boolean insertar(Producto p) throws SQLException {
        String sql = "INSERT INTO productos " +
                     "(codigo, nombre, descripcion, categoria_id, precio, stock, activo) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = Conexion.getConexion().prepareStatement(sql)) {
            ps.setString(1, p.getCodigo());
            ps.setString(2, p.getNombre());
            ps.setString(3, p.getDescripcion());
            ps.setInt(4, p.getCategoriaId());
            ps.setBigDecimal(5, p.getPrecio());
            ps.setInt(6, p.getStock());
            ps.setBoolean(7, p.isActivo());
            return ps.executeUpdate() > 0;
        }
    }

    // ── READ (todos) ──────────────────────────────────────────
    public List<Producto> listarTodos() throws SQLException {
        List<Producto> lista = new ArrayList<>();
        String sql = SELECT_BASE + "ORDER BY p.nombre";
        try (Statement st = Conexion.getConexion().createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) lista.add(mapear(rs));
        }
        return lista;
    }

    // ── READ (buscar por código o nombre) ─────────────────────
    public List<Producto> buscar(String termino) throws SQLException {
        List<Producto> lista = new ArrayList<>();
        String sql = SELECT_BASE +
                     "WHERE p.codigo LIKE ? OR p.nombre LIKE ? " +
                     "ORDER BY p.nombre";
        try (PreparedStatement ps = Conexion.getConexion().prepareStatement(sql)) {
            String like = "%" + termino + "%";
            ps.setString(1, like);
            ps.setString(2, like);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(mapear(rs));
            }
        }
        return lista;
    }

    // ── READ (por id) ─────────────────────────────────────────
    public Producto buscarPorId(int id) throws SQLException {
        String sql = SELECT_BASE + "WHERE p.id = ?";
        try (PreparedStatement ps = Conexion.getConexion().prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapear(rs);
            }
        }
        return null;
    }

    // ── UPDATE ────────────────────────────────────────────────
    public boolean actualizar(Producto p) throws SQLException {
        String sql = "UPDATE productos SET codigo=?, nombre=?, descripcion=?, " +
                     "categoria_id=?, precio=?, stock=?, activo=? WHERE id=?";
        try (PreparedStatement ps = Conexion.getConexion().prepareStatement(sql)) {
            ps.setString(1, p.getCodigo());
            ps.setString(2, p.getNombre());
            ps.setString(3, p.getDescripcion());
            ps.setInt(4, p.getCategoriaId());
            ps.setBigDecimal(5, p.getPrecio());
            ps.setInt(6, p.getStock());
            ps.setBoolean(7, p.isActivo());
            ps.setInt(8, p.getId());
            return ps.executeUpdate() > 0;
        }
    }

    // ── DELETE ───────────────────────────────────────────────
    public boolean eliminar(int id) throws SQLException {
        String sql = "DELETE FROM productos WHERE id = ?";
        try (PreparedStatement ps = Conexion.getConexion().prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    // ── Auxiliar ──────────────────────────────────────────────
    private Producto mapear(ResultSet rs) throws SQLException {
        Producto p = new Producto();
        p.setId(rs.getInt("id"));
        p.setCodigo(rs.getString("codigo"));
        p.setNombre(rs.getString("nombre"));
        p.setDescripcion(rs.getString("descripcion"));
        p.setCategoriaId(rs.getInt("categoria_id"));
        p.setCategoriaNombre(rs.getString("categoria_nombre"));
        p.setPrecio(rs.getBigDecimal("precio"));
        p.setStock(rs.getInt("stock"));
        p.setActivo(rs.getBoolean("activo"));
        p.setFechaRegistro(rs.getTimestamp("fecha_registro"));
        return p;
    }
}
