package produccion;

import excepciones.ColaboradorNoAptoException;
import inventario.Bodega;
import personal.Colaborador;
import personal.Tostador;

public class EtapaTostado implements Etapa {
    private final double mermaClaro = 0.15;
    private final double mermaMedio = 0.17;
    private final double mermaOscuro = 0.20;

    @Override
    public boolean validarRequisitos(Lote l, Colaborador c, Bodega b) {
        return c instanceof Tostador && ((Tostador) c).puedeTostar();
    }

    @Override
    public void ejecutar(Lote l, Colaborador c, Bodega b) throws ColaboradorNoAptoException {
        if (!(c instanceof Tostador)) {
            throw new ColaboradorNoAptoException("Solo un Tostador puede tostar un lote.");
        }
        if (!validarRequisitos(l, c, b)) {
            throw new ColaboradorNoAptoException("El tostador " + c.getNombre() + " no está certificado.");
        }
        Tostador t = (Tostador) c;
        // 1 hora por cada 10 kg, redondeando hacia arriba (con el peso antes de la merma)
        double horas = Math.ceil(l.getPesoActualKg() / 10.0);
        double nuevoPeso = l.getPesoActualKg() * (1 - merma(l.getPerfil()));
        l.setPesoActualKg(Math.round(nuevoPeso * 100) / 100.0);
        l.sumarCosto(t.getTarifaHora() * horas);
        t.sumarHoras(horas);
        l.cambiarEstado(EstadoLote.CONTROL_CALIDAD);
    }

    private double merma(String perfil) {
        if (perfil.equals("CLARO")) {
            return mermaClaro;
        }
        if (perfil.equals("MEDIO")) {
            return mermaMedio;
        }
        return mermaOscuro;
    }
}
