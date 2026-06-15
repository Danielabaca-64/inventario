public class Producto {

    private int id;
    private String codigo;
    private String nombre;
    private String descripcion;
    private int categoriaId;
    private double precio;
    private int stock;

    public Producto() {
    }

    public Producto(
            String codigo,
            String nombre,
            String descripcion,
            int categoriaId,
            double precio,
            int stock) {

        this.codigo = codigo;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.categoriaId = categoriaId;
        this.precio = precio;
        this.stock = stock;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public int getCategoriaId() {
        return categoriaId;
    }

    public double getPrecio() {
        return precio;
    }

    public int getStock() {
        return stock;
    }
}