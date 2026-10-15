package cafe;

import java.util.ArrayList;
import java.util.List;

public class ServicioProduccion {
    private List<Colaborador> colaboradores = new ArrayList<>();
    private List<Lote> lotes = new ArrayList<>();
    private Bodega bodega = new Bodega();

    public Bodega getBodega() { return bodega; }

    public void registrarLote(Lote l) { throw new UnsupportedOperationException("TODO"); }
    public void avanzarLote(String cod) { throw new UnsupportedOperationException("TODO"); }
    public double costoLote(String cod) { throw new UnsupportedOperationException("TODO"); }
    public List<Lote> lotesBloqueados() { throw new UnsupportedOperationException("TODO"); }
}
