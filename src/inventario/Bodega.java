package inventario;

import excepciones.InsumoInsuficienteException;
import java.util.ArrayList;
import java.util.List;

/** Guarda todos los insumos y ofrece las operaciones sobre ellos. */
public class Bodega {
    private final List<Insumo> insumos = new ArrayList<>();

    public void agregar(Insumo i) {
        if (buscar(i.getCodigo()) != null) {
            throw new IllegalArgumentException("Ya existe un insumo con el código " + i.getCodigo() + ".");
        }
        insumos.add(i);
    }

    /** Devuelve el insumo o null si no existe. */
    public Insumo buscar(String cod) {
        String limpio = cod == null ? "" : cod.trim();
        for (Insumo i : insumos) {
            if (i.getCodigo().equalsIgnoreCase(limpio)) {
                return i;
            }
        }
        return null;
    }

    /** Devuelve el primer material de empaque del tipo pedido (BOLSA o ETIQUETA), o null. */
    public MaterialEmpaque buscarMaterial(String tipo) {
        for (Insumo i : insumos) {
            if (i instanceof MaterialEmpaque && ((MaterialEmpaque) i).getTipo().equals(tipo)) {
                return (MaterialEmpaque) i;
            }
        }
        return null;
    }

    public void eliminar(String cod) {
        insumos.remove(buscarObligatorio(cod));
    }

    public void reponer(String cod, double cant) {
        buscarObligatorio(cod).reponer(cant);
    }

    public void consumir(String cod, double cant) throws InsumoInsuficienteException {
        buscarObligatorio(cod).consumir(cant);
    }

    public boolean hayStock(String cod, double cant) {
        Insumo i = buscar(cod);
        return i != null && i.getCantidad() >= cant;
    }

    public List<Insumo> insumosCriticos() {
        List<Insumo> criticos = new ArrayList<>();
        for (Insumo i : insumos) {
            if (i.estaCritico()) {
                criticos.add(i);
            }
        }
        return criticos;
    }

    public List<Insumo> listar() {
        return new ArrayList<>(insumos);
    }

    private Insumo buscarObligatorio(String cod) {
        Insumo i = buscar(cod);
        if (i == null) {
            throw new IllegalArgumentException("No existe un insumo con el código " + cod + ".");
        }
        return i;
    }
}
