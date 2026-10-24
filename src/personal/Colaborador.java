package personal;

import util.Validador;

/** Clase base de todos los trabajadores de la empresa. */
public abstract class Colaborador {
    private String nombre;
    private final String cedula;

    public Colaborador(String nombre, String cedula) {
        Validador v = new Validador();
        if (!v.validarTexto(nombre)) {
            throw new IllegalArgumentException("El nombre no puede estar vacío.");
        }
        if (!v.validarCedula(cedula)) {
            throw new IllegalArgumentException("La cédula debe tener 9 dígitos.");
        }
        this.nombre = nombre.trim();
        this.cedula = cedula;
    }

    /** Cada tipo de colaborador calcula su pago de forma distinta (polimorfismo). */
    public abstract double calcularPago();

    /** Cambia la tarifa o salario base, que depende del tipo de colaborador. */
    public abstract void actualizarTarifa(double nueva);

    /** Nombre del tipo de colaborador, para mostrar en pantalla. */
    public abstract String getTipo();

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        if (!new Validador().validarTexto(nombre)) {
            throw new IllegalArgumentException("El nombre no puede estar vacío.");
        }
        this.nombre = nombre.trim();
    }

    public String getCedula() {
        return cedula;
    }

    protected void exigirPositivo(double n, String campo) {
        if (!new Validador().validarPositivo(n)) {
            throw new IllegalArgumentException(campo + " debe ser mayor que 0.");
        }
    }

    @Override
    public String toString() {
        return String.format("%-16s | %s | %-20s", getTipo(), cedula, nombre);
    }
}
