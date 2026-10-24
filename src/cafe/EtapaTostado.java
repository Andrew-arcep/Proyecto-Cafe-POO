package cafe;

public class EtapaTostado implements Etapa {
    private double mermaClaro;
    private double mermaMedio;
    private double mermaOscuro;

    public EtapaTostado(double mermaClaro, double mermaMedio, double mermaOscuro) {
        this.mermaClaro = mermaClaro;
        this.mermaMedio = mermaMedio;
        this.mermaOscuro = mermaOscuro;
    }

    @Override
    public boolean validarRequisitos(Lote l, Colaborador c, Bodega b) { throw new UnsupportedOperationException("TODO"); }
    @Override
    public void ejecutar(Lote l, Colaborador c, Bodega b) { throw new UnsupportedOperationException("TODO"); }
}
