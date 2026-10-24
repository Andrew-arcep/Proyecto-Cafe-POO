package produccion;

import excepciones.ColaboradorNoAptoException;
import excepciones.InsumoInsuficienteException;
import inventario.Bodega;
import personal.Colaborador;

/** Una etapa del proceso. ServicioProduccion las usa todas igual (polimorfismo). */
public interface Etapa {
    /** Dice si el colaborador es del tipo correcto para esta etapa. */
    boolean validarRequisitos(Lote l, Colaborador c, Bodega b);

    /** Ejecuta la etapa y cambia el estado del lote. */
    void ejecutar(Lote l, Colaborador c, Bodega b) throws ColaboradorNoAptoException, InsumoInsuficienteException;
}
