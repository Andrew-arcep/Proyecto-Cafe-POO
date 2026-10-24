package produccion;

import excepciones.ColaboradorNoAptoException;
import inventario.Bodega;
import personal.Catador;
import personal.Colaborador;

public class EtapaCalidad implements Etapa {
    private final int puntajeMinimo = 80;

    @Override
    public boolean validarRequisitos(Lote l, Colaborador c, Bodega b) {
        return c instanceof Catador;
    }

    @Override
    public void ejecutar(Lote l, Colaborador c, Bodega b) throws ColaboradorNoAptoException {
        if (!validarRequisitos(l, c, b)) {
            throw new ColaboradorNoAptoException("Solo un Catador puede hacer el control de calidad.");
        }
        if (l.getPuntajeCalidad() < 0) {
            throw new IllegalStateException("Primero registre el puntaje de calidad del lote.");
        }
        if (l.getPuntajeCalidad() < puntajeMinimo) {
            l.cambiarEstado(EstadoLote.RECHAZADO);
        } else {
            l.cambiarEstado(EstadoLote.EN_EMPAQUE);
        }
        l.limpiarPuntaje();
    }
}
