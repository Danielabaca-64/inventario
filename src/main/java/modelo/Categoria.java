package modelo;

/**
 * Representa una fila de la tabla `categorias`.
 */
public class Categoria {

    private int    id;
    private String nombre;
    private String descripcion;

    public Categoria() {}

    public Categoria(int id, String nombre, String descripcion) {
        this.id          = id;
        this.nombre      = nombre;
        this.descripcion = descripcion;
    }

    // ── Getters ─────────────────────────────────────────────
    public int    getId()          { return id; }
    public String getNombre()      { return nombre; }
    public String getDescripcion() { return descripcion; }

    // ── Setters ─────────────────────────────────────────────
    public void setId(int id)                   { this.id = id; }
    public void setNombre(String nombre)        { this.nombre = nombre; }
    public void setDescripcion(String desc)     { this.descripcion = desc; }

    /** Usado por JComboBox para mostrar el nombre de la categoría. */
    @Override
    public String toString() { return nombre; }
}
