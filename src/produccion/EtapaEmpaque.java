package produccion;

import excepciones.ColaboradorNoAptoException;
import excepciones.InsumoInsuficienteException;
import inventario.Bodega;
import inventario.MaterialEmpaque;
import personal.Colaborador;
import personal.OperarioEmpaque;

public class EtapaEmpaque implements Etapa {
    private final double bolsasPorKilo = 1;
    private final double etiquetasPorKilo = 1;

    @Override
    public boolean validarRequisitos(Lote l, Colaborador c, Bodega b) {
        return c instanceof OperarioEmpaque;
    }

    @Override
    public void ejecutar(Lote l, Colaborador c, Bodega b)
            throws ColaboradorNoAptoException, InsumoInsuficienteException {
        if (!validarRequisitos(l, c, b)) {
            throw new ColaboradorNoAptoException("Solo un Operario de empaque puede empacar un lote.");
        }
        double kilos = Math.ceil(l.getPesoActualKg());
        double bolsas = Math.ceil(kilos * bolsasPorKilo);
        double etiquetas = Math.ceil(kilos * etiquetasPorKilo);
        MaterialEmpaque bolsa = b.buscarMaterial("BOLSA");
        MaterialEmpaque etiqueta = b.buscarMaterial("ETIQUETA");

        String falta = "";
        if (bolsa == null || bolsa.getCantidad() < bolsas) {
            falta += String.format("BOLSA: se necesitan %.0f y hay %.0f. ",
                    bolsas, bolsa == null ? 0 : bolsa.getCantidad());
        }
        if (etiqueta == null || etiqueta.getCantidad() < etiquetas) {
            falta += String.format("ETIQUETA: se necesitan %.0f y hay %.0f. ",
                    etiquetas, etiqueta == null ? 0 : etiqueta.getCantidad());
        }
        if (!falta.isEmpty()) {
            l.setMotivoBloqueo("Falta material -> " + falta.trim());
            l.cambiarEstado(EstadoLote.BLOQUEADO);
            return;
        }
        bolsa.consumir(bolsas);
        etiqueta.consumir(etiquetas);
        ((OperarioEmpaque) c).sumarKilos(kilos);
        l.setMotivoBloqueo("");
        l.cambiarEstado(EstadoLote.FINALIZADO);
    }
}
