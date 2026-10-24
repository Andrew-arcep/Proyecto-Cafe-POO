package personal;

public class Catador extends Colaborador {
    private double salarioFijo;
    private double bono;

    public Catador(String nombre, String cedula, double salarioFijo) {
        super(nombre, cedula);
        exigirPositivo(salarioFijo, "El salario fijo");
        this.salarioFijo = salarioFijo;
        this.bono = 0;
    }

    @Override
    public double calcularPago() {
        return salarioFijo + bono;
    }

    @Override
    public void actualizarTarifa(double nueva) {
        exigirPositivo(nueva, "El salario fijo");
        this.salarioFijo = nueva;
    }

    @Override
    public String getTipo() {
        return "Catador";
    }

    public void setBono(double bono) {
        if (bono < 0) {
            throw new IllegalArgumentException("El bono no puede ser negativo.");
        }
        this.bono = bono;
    }

    public double getSalarioFijo() {
        return salarioFijo;
    }

    public double getBono() {
        return bono;
    }

    @Override
    public String toString() {
        return super.toString() + String.format(" | salario CRC %.2f | bono CRC %.2f", salarioFijo, bono);
    }
}
