package modelo;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * Representa una fila de la tabla `productos`.
 */
public class Producto {

    private int        id;
    private String     codigo;
    private String     nombre;
    private String     descripcion;
    private int        categoriaId;
    private String     categoriaNombre; // para mostrar en tabla sin JOIN extra
    private BigDecimal precio;
    private int        stock;
    private boolean    activo;
    private Timestamp  fechaRegistro;

    public Producto() {}

    public Producto(String codigo, String nombre, String descripcion,
                    int categoriaId, BigDecimal precio, int stock) {
        this.codigo      = codigo;
        this.nombre      = nombre;
        this.descripcion = descripcion;
        this.categoriaId = categoriaId;
        this.precio      = precio;
        this.stock       = stock;
        this.activo      = true;
    }

    // ── Getters ──────────────────────────────────────────────
    public int        getId()              { return id; }
    public String     getCodigo()          { return codigo; }
    public String     getNombre()          { return nombre; }
    public String     getDescripcion()     { return descripcion; }
    public int        getCategoriaId()     { return categoriaId; }
    public String     getCategoriaNombre() { return categoriaNombre; }
    public BigDecimal getPrecio()          { return precio; }
    public int        getStock()           { return stock; }
    public boolean    isActivo()           { return activo; }
    public Timestamp  getFechaRegistro()   { return fechaRegistro; }

    // ── Setters ──────────────────────────────────────────────
    public void setId(int id)                          { this.id = id; }
    public void setCodigo(String codigo)               { this.codigo = codigo; }
    public void setNombre(String nombre)               { this.nombre = nombre; }
    public void setDescripcion(String descripcion)     { this.descripcion = descripcion; }
    public void setCategoriaId(int categoriaId)        { this.categoriaId = categoriaId; }
    public void setCategoriaNombre(String cn)          { this.categoriaNombre = cn; }
    public void setPrecio(java.math.BigDecimal precio) { this.precio = precio; }
    public void setStock(int stock)                    { this.stock = stock; }
    public void setActivo(boolean activo)              { this.activo = activo; }
    public void setFechaRegistro(Timestamp ts)         { this.fechaRegistro = ts; }
}
