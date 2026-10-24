package cafe;

public class EtapaCalidad implements Etapa {
    private int puntajeMinimo;

    public EtapaCalidad(int puntajeMinimo) { this.puntajeMinimo = puntajeMinimo; }

    @Override
    public boolean validarRequisitos(Lote l, Colaborador c, Bodega b) { throw new UnsupportedOperationException("TODO"); }
    @Override
    public void ejecutar(Lote l, Colaborador c, Bodega b) { throw new UnsupportedOperationException("TODO"); }
}
