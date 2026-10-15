package cafe;

public class EtapaEmpaque implements Etapa {
    private double bolsasPorKilo;
    private double etiquetasPorKilo;

    public EtapaEmpaque(double bolsasPorKilo, double etiquetasPorKilo) {
        this.bolsasPorKilo = bolsasPorKilo;
        this.etiquetasPorKilo = etiquetasPorKilo;
    }

    @Override
    public boolean validarRequisitos(Lote l, Colaborador c, Bodega b) { throw new UnsupportedOperationException("TODO"); }
    @Override
    public void ejecutar(Lote l, Colaborador c, Bodega b) { throw new UnsupportedOperationException("TODO"); }
}
