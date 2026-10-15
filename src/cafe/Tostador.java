package cafe;

public class Tostador extends Colaborador {
    private double tarifaHora;
    private double horas;
    private boolean certificado;

    public Tostador(String nombre, String cedula, double tarifaHora, double horas, boolean certificado) {
        super(nombre, cedula);
        this.tarifaHora = tarifaHora;
        this.horas = horas;
        this.certificado = certificado;
    }

    @Override
    public double calcularPago() { throw new UnsupportedOperationException("TODO"); }

    public boolean puedeTostar() { throw new UnsupportedOperationException("TODO"); }
}
