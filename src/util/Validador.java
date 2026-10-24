package util;

/** Validaciones simples que devuelven true si el dato es correcto. */
public class Validador {

    /** La cédula debe tener exactamente 9 dígitos (supuesto del equipo). */
    public boolean validarCedula(String c) {
        return c != null && c.matches("\\d{9}");
    }

    /** El número debe ser mayor que 0. */
    public boolean validarPositivo(double n) {
        return !Double.isNaN(n) && n > 0;
    }

    /** El texto no puede ser nulo ni vacío. */
    public boolean validarTexto(String t) {
        return t != null && !t.trim().isEmpty();
    }
}
