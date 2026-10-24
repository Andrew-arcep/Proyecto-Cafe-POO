package inventario;

import util.Validador;

public class GranoVerde extends Insumo {
    private final String origen;
    private final String variedad;
    private final double humedad;
    private final double costoPorKg;

    public GranoVerde(String codigo, String nombre, double cantidadKg, double nivelMinimo,
                      String origen, String variedad, double humedad, double costoPorKg) {
        super(codigo, nombre, cantidadKg, nivelMinimo);
        Validador v = new Validador();
        if (!v.validarTexto(origen) || !v.validarTexto(variedad)) {
            throw new IllegalArgumentException("El origen y la variedad no pueden estar vacíos.");
        }
        if (!v.validarPositivo(humedad) || humedad > 100) {
            throw new IllegalArgumentException("La humedad debe ser mayor que 0 y no más de 100.");
        }
        if (!v.validarPositivo(costoPorKg)) {
            throw new IllegalArgumentException("El costo por kg debe ser mayor que 0.");
        }
        this.origen = origen.trim();
        this.variedad = variedad.trim();
        this.humedad = humedad;
        this.costoPorKg = costoPorKg;
    }

    @Override
    public String getUnidad() {
        return "kg";
    }

    public String getOrigen() {
        return origen;
    }

    public String getVariedad() {
        return variedad;
    }

    public double getHumedad() {
        return humedad;
    }

    public double getCostoPorKg() {
        return costoPorKg;
    }

    @Override
    public String toString() {
        return super.toString() + String.format(" | grano %s, %s, humedad %.1f%%, CRC %.2f/kg",
                origen, variedad, humedad, costoPorKg);
    }
}
