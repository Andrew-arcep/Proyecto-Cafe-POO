package cafe;

public abstract class Colaborador {
    private String nombre;
    private String cedula;

    public Colaborador(String nombre, String cedula) {
        this.nombre = nombre;
        this.cedula = cedula;
    }

    public String getNombre() { return nombre; }
    public String getCedula() { return cedula; }

    public abstract double calcularPago();
}
