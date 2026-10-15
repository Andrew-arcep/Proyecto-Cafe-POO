package cafe;

public abstract class Insumo {
    private String codigo;
    private String nombre;
    private double cantidad;
    private double nivelMinimo;

    public Insumo(String codigo, String nombre, double cantidad, double nivelMinimo) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.cantidad = cantidad;
        this.nivelMinimo = nivelMinimo;
    }

    public String getCodigo() { return codigo; }
    public String getNombre() { return nombre; }
    public double getCantidad() { return cantidad; }
    public double getNivelMinimo() { return nivelMinimo; }

    public void consumir(double cant) { throw new UnsupportedOperationException("TODO"); }
    public boolean estaCritico() { throw new UnsupportedOperationException("TODO"); }
}
