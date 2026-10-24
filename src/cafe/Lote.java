package cafe;

public class Lote {
    private String codigo;
    private String origenGrano;
    private double pesoInicialKg;
    private double pesoActualKg;
    private double costoTotal;
    private String perfil;
    private EstadoLote estado;

    public Lote(String codigo, String origenGrano, double pesoInicialKg, String perfil) {
        this.codigo = codigo;
        this.origenGrano = origenGrano;
        this.pesoInicialKg = pesoInicialKg;
        this.pesoActualKg = pesoInicialKg;
        this.perfil = perfil;
        this.estado = EstadoLote.RECEPCION;
    }

    public String getCodigo() { return codigo; }
    public EstadoLote getEstado() { return estado; }
    public double getCostoTotal() { return costoTotal; }
    public double getPesoActualKg() { return pesoActualKg; }

    public void cambiarEstado(EstadoLote e) { throw new UnsupportedOperationException("TODO"); }
    public void sumarCosto(double monto) { throw new UnsupportedOperationException("TODO"); }
}
