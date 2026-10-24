package personal;

public class OperarioEmpaque extends Colaborador {
    private double tarifaKilo;
    private double kilosEmpacados;

    public OperarioEmpaque(String nombre, String cedula, double tarifaKilo) {
        super(nombre, cedula);
        exigirPositivo(tarifaKilo, "La tarifa por kilo");
        this.tarifaKilo = tarifaKilo;
        this.kilosEmpacados = 0;
    }

    @Override
    public double calcularPago() {
        return tarifaKilo * kilosEmpacados;
    }

    @Override
    public void actualizarTarifa(double nueva) {
        exigirPositivo(nueva, "La tarifa por kilo");
        this.tarifaKilo = nueva;
    }

    @Override
    public String getTipo() {
        return "Operario empaque";
    }

    public void sumarKilos(double kilos) {
        this.kilosEmpacados += kilos;
    }

    public double getTarifaKilo() {
        return tarifaKilo;
    }

    public double getKilosEmpacados() {
        return kilosEmpacados;
    }

    @Override
    public String toString() {
        return super.toString() + String.format(" | CRC %.2f/kg | %.1f kg empacados", tarifaKilo, kilosEmpacados);
    }
}
