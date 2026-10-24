package personal;

public class Tostador extends Colaborador {
    private double tarifaHora;
    private double horas;
    private boolean certificado;

    public Tostador(String nombre, String cedula, double tarifaHora, boolean certificado) {
        super(nombre, cedula);
        exigirPositivo(tarifaHora, "La tarifa por hora");
        this.tarifaHora = tarifaHora;
        this.horas = 0;
        this.certificado = certificado;
    }

    @Override
    public double calcularPago() {
        return tarifaHora * horas;
    }

    @Override
    public void actualizarTarifa(double nueva) {
        exigirPositivo(nueva, "La tarifa por hora");
        this.tarifaHora = nueva;
    }

    @Override
    public String getTipo() {
        return "Tostador";
    }

    /** Solo un tostador certificado puede tostar. */
    public boolean puedeTostar() {
        return certificado;
    }

    public void sumarHoras(double h) {
        this.horas += h;
    }

    public double getTarifaHora() {
        return tarifaHora;
    }

    public double getHoras() {
        return horas;
    }

    public void setCertificado(boolean certificado) {
        this.certificado = certificado;
    }

    @Override
    public String toString() {
        return super.toString() + String.format(" | CRC %.2f/h | %.1f h | certificado: %s",
                tarifaHora, horas, certificado ? "sí" : "no");
    }
}
