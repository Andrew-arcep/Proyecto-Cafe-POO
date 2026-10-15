package cafe;

public class GranoVerde extends Insumo {
    private String origen;
    private String variedad;
    private double humedad;

    public GranoVerde(String codigo, String nombre, double cantidad, double nivelMinimo,
                      String origen, String variedad, double humedad) {
        super(codigo, nombre, cantidad, nivelMinimo);
        this.origen = origen;
        this.variedad = variedad;
        this.humedad = humedad;
    }
}
