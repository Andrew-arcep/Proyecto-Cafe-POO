package cafe;

public class Catador extends Colaborador {
    private double salarioFijo;
    private double bono;

    public Catador(String nombre, String cedula, double salarioFijo, double bono) {
        super(nombre, cedula);
        this.salarioFijo = salarioFijo;
        this.bono = bono;
    }

    @Override
    public double calcularPago() { throw new UnsupportedOperationException("TODO"); }
}
