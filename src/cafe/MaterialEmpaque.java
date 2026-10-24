package cafe;

public class MaterialEmpaque extends Insumo {
    private String tipo;

    public MaterialEmpaque(String codigo, String nombre, double cantidad, double nivelMinimo, String tipo) {
        super(codigo, nombre, cantidad, nivelMinimo);
        this.tipo = tipo;
    }
}
