package produccion;

import util.Validador;

/** Un lote de café que avanza por las etapas de producción. */
public class Lote {
    private final String codigo;
    private final String origenGrano;
    private final double pesoInicialKg;
    private double pesoActualKg;
    private double costoTotal;
    private final String perfil;
    private EstadoLote estado;
    private int puntajeCalidad;      // -1 significa "todavía no registrado"
    private int reprocesos;
    private String motivoBloqueo;    // qué falta cuando el lote está BLOQUEADO

    public Lote(String codigo, String origenGrano, double pesoInicialKg, String perfil) {
        Validador v = new Validador();
        if (!v.validarTexto(codigo) || !v.validarTexto(origenGrano)) {
            throw new IllegalArgumentException("El código y el origen no pueden estar vacíos.");
        }
        if (!v.validarPositivo(pesoInicialKg)) {
            throw new IllegalArgumentException("El peso debe ser mayor que 0.");
        }
        String p = perfil == null ? "" : perfil.trim().toUpperCase();
        if (!p.equals("CLARO") && !p.equals("MEDIO") && !p.equals("OSCURO")) {
            throw new IllegalArgumentException("El perfil debe ser CLARO, MEDIO u OSCURO.");
        }
        this.codigo = codigo.trim();
        this.origenGrano = origenGrano.trim();
        this.pesoInicialKg = pesoInicialKg;
        this.pesoActualKg = pesoInicialKg;
        this.costoTotal = 0;
        this.perfil = p;
        this.estado = EstadoLote.RECEPCION;
        this.puntajeCalidad = -1;
        this.reprocesos = 0;
        this.motivoBloqueo = "";
    }

    public void cambiarEstado(EstadoLote e) {
        this.estado = e;
    }

    public void sumarCosto(double monto) {
        if (monto < 0) {
            throw new IllegalArgumentException("El costo a sumar no puede ser negativo.");
        }
        costoTotal += monto;
    }

    /** Guarda el puntaje de la cata (0 a 100) antes de avanzar el lote. */
    public void registrarPuntaje(int p) {
        if (p < 0 || p > 100) {
            throw new IllegalArgumentException("El puntaje debe estar entre 0 y 100.");
        }
        this.puntajeCalidad = p;
    }

    /** Quita el puntaje usado, para que una segunda cata exija uno nuevo. */
    public void limpiarPuntaje() {
        this.puntajeCalidad = -1;
    }

    public void sumarReproceso() {
        reprocesos++;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getOrigenGrano() {
        return origenGrano;
    }

    public double getPesoInicialKg() {
        return pesoInicialKg;
    }

    public double getPesoActualKg() {
        return pesoActualKg;
    }

    public void setPesoActualKg(double pesoActualKg) {
        this.pesoActualKg = pesoActualKg;
    }

    public double getCostoTotal() {
        return costoTotal;
    }

    public String getPerfil() {
        return perfil;
    }

    public EstadoLote getEstado() {
        return estado;
    }

    public int getPuntajeCalidad() {
        return puntajeCalidad;
    }

    public int getReprocesos() {
        return reprocesos;
    }

    public String getMotivoBloqueo() {
        return motivoBloqueo;
    }

    public void setMotivoBloqueo(String motivoBloqueo) {
        this.motivoBloqueo = motivoBloqueo;
    }

    @Override
    public String toString() {
        return String.format("%-6s | %-10s | %-7s | %7.2f -> %7.2f kg | costo CRC %.2f | %s | reprocesos: %d",
                codigo, origenGrano, perfil, pesoInicialKg, pesoActualKg, costoTotal, estado, reprocesos);
    }
}
