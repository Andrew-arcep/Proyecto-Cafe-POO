package inventario;

import excepciones.InsumoInsuficienteException;
import util.Validador;

/** Cualquier cosa que se guarda en bodega y se consume en producción. */
public abstract class Insumo {
    private final String codigo;
    private String nombre;
    private double cantidad;
    private double nivelMinimo;

    public Insumo(String codigo, String nombre, double cantidad, double nivelMinimo) {
        Validador v = new Validador();
        if (!v.validarTexto(codigo) || !v.validarTexto(nombre)) {
            throw new IllegalArgumentException("El código y el nombre no pueden estar vacíos.");
        }
        if (!v.validarPositivo(cantidad)) {
            throw new IllegalArgumentException("La cantidad debe ser mayor que 0.");
        }
        if (!v.validarPositivo(nivelMinimo)) {
            throw new IllegalArgumentException("El nivel mínimo debe ser mayor que 0.");
        }
        this.codigo = codigo.trim();
        this.nombre = nombre.trim();
        this.cantidad = cantidad;
        this.nivelMinimo = nivelMinimo;
    }

    /** Unidad en que se mide la cantidad (kg o unidades). */
    public abstract String getUnidad();

    /** Resta cantidad del inventario; falla si no alcanza. */
    public void consumir(double cant) throws InsumoInsuficienteException {
        if (!new Validador().validarPositivo(cant)) {
            throw new IllegalArgumentException("La cantidad a consumir debe ser mayor que 0.");
        }
        if (cant > cantidad) {
            throw new InsumoInsuficienteException(String.format(
                    "Insumo insuficiente de %s: se necesitan %.2f %s y solo hay %.2f.",
                    nombre, cant, getUnidad(), cantidad));
        }
        cantidad -= cant;
    }

    /** Suma cantidad al inventario. */
    public void reponer(double cant) {
        if (!new Validador().validarPositivo(cant)) {
            throw new IllegalArgumentException("La cantidad a reponer debe ser mayor que 0.");
        }
        cantidad += cant;
    }

    /** El insumo está crítico si la cantidad llegó al nivel mínimo o menos. */
    public boolean estaCritico() {
        return cantidad <= nivelMinimo;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        if (!new Validador().validarTexto(nombre)) {
            throw new IllegalArgumentException("El nombre no puede estar vacío.");
        }
        this.nombre = nombre.trim();
    }

    public double getCantidad() {
        return cantidad;
    }

    public double getNivelMinimo() {
        return nivelMinimo;
    }

    public void setNivelMinimo(double nivelMinimo) {
        if (!new Validador().validarPositivo(nivelMinimo)) {
            throw new IllegalArgumentException("El nivel mínimo debe ser mayor que 0.");
        }
        this.nivelMinimo = nivelMinimo;
    }

    @Override
    public String toString() {
        return String.format("%-8s | %-22s | %8.2f %-9s | mínimo %.2f%s", codigo, nombre, cantidad,
                getUnidad(), nivelMinimo, estaCritico() ? "  [CRÍTICO]" : "");
    }
}
