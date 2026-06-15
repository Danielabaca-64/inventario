package dao;

import modelo.Categoria;
import util.Conexion;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Operaciones CRUD sobre la tabla `categorias`.
 * Usa PreparedStatement para prevenir SQL Injection.
 */
public class CategoriaDAO {

    // ── CREATE ────────────────────────────────────────────────
    public boolean insertar(Categoria c) throws SQLException {
        String sql = "INSERT INTO categorias (nombre, descripcion) VALUES (?, ?)";
        try (PreparedStatement ps = Conexion.getConexion().prepareStatement(sql)) {
            ps.setString(1, c.getNombre());
            ps.setString(2, c.getDescripcion());
            return ps.executeUpdate() > 0;
        }
    }

    // ── READ (todos) ──────────────────────────────────────────
    public List<Categoria> listarTodas() throws SQLException {
        List<Categoria> lista = new ArrayList<>();
        String sql = "SELECT id, nombre, descripcion FROM categorias ORDER BY nombre";
        try (Statement st = Conexion.getConexion().createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(mapear(rs));
            }
        }
        return lista;
    }

    // ── READ (por id) ─────────────────────────────────────────
    public Categoria buscarPorId(int id) throws SQLException {
        String sql = "SELECT id, nombre, descripcion FROM categorias WHERE id = ?";
        try (PreparedStatement ps = Conexion.getConexion().prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapear(rs);
            }
        }
        return null;
    }

    // ── UPDATE ────────────────────────────────────────────────
    public boolean actualizar(Categoria c) throws SQLException {
        String sql = "UPDATE categorias SET nombre = ?, descripcion = ? WHERE id = ?";
        try (PreparedStatement ps = Conexion.getConexion().prepareStatement(sql)) {
            ps.setString(1, c.getNombre());
            ps.setString(2, c.getDescripcion());
            ps.setInt(3, c.getId());
            return ps.executeUpdate() > 0;
        }
    }

    // ── DELETE ───────────────────────────────────────────────
    /**
     * Elimina la categoría. Lanza SQLException si tiene productos asociados
     * (restricción FOREIGN KEY ON DELETE RESTRICT del esquema).
     */
    public boolean eliminar(int id) throws SQLException {
        String sql = "DELETE FROM categorias WHERE id = ?";
        try (PreparedStatement ps = Conexion.getConexion().prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    // ── Auxiliar ──────────────────────────────────────────────
    private Categoria mapear(ResultSet rs) throws SQLException {
        return new Categoria(
            rs.getInt("id"),
            rs.getString("nombre"),
            rs.getString("descripcion")
        );
    }
}
