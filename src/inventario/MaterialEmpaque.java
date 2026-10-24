package inventario;

public class MaterialEmpaque extends Insumo {
    private final String tipo;

    /** El tipo solo puede ser BOLSA o ETIQUETA. */
    public MaterialEmpaque(String codigo, String nombre, double cantidad, double nivelMinimo, String tipo) {
        super(codigo, nombre, cantidad, nivelMinimo);
        String t = tipo == null ? "" : tipo.trim().toUpperCase();
        if (!t.equals("BOLSA") && !t.equals("ETIQUETA")) {
            throw new IllegalArgumentException("El tipo debe ser BOLSA o ETIQUETA.");
        }
        this.tipo = t;
    }

    @Override
    public String getUnidad() {
        return "unidades";
    }

    public String getTipo() {
        return tipo;
    }

    @Override
    public String toString() {
        return super.toString() + " | tipo " + tipo;
    }
}
