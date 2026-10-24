package cafe;

public class OperarioEmpaque extends Colaborador {
    private double tarifaKilo;
    private double kilosEmpacados;

    public OperarioEmpaque(String nombre, String cedula, double tarifaKilo, double kilosEmpacados) {
        super(nombre, cedula);
        this.tarifaKilo = tarifaKilo;
        this.kilosEmpacados = kilosEmpacados;
    }

    @Override
    public double calcularPago() { throw new UnsupportedOperationException("TODO"); }
}
