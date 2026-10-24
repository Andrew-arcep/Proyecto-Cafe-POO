package servicio;

import excepciones.ColaboradorNoAptoException;
import excepciones.InsumoInsuficienteException;
import inventario.Bodega;
import inventario.GranoVerde;
import inventario.Insumo;
import inventario.MaterialEmpaque;
import java.util.ArrayList;
import java.util.List;
import personal.Catador;
import personal.Colaborador;
import personal.OperarioEmpaque;
import personal.Tostador;
import produccion.Etapa;
import produccion.EtapaCalidad;
import produccion.EtapaEmpaque;
import produccion.EtapaTostado;
import produccion.EstadoLote;
import produccion.Lote;

/** Coordina personal, bodega y lotes. Aquí vive la lógica de aplicación. */
public class ServicioProduccion {
    private final List<Colaborador> colaboradores = new ArrayList<>();
    private final List<Lote> lotes = new ArrayList<>();
    private final Bodega bodega = new Bodega();

    private final Etapa etapaTostado = new EtapaTostado();
    private final Etapa etapaCalidad = new EtapaCalidad();
    private final Etapa etapaEmpaque = new EtapaEmpaque();

    // ---------- Colaboradores ----------

    public void registrarColaborador(Colaborador c) {
        if (buscarColaborador(c.getCedula()) != null) {
            throw new IllegalArgumentException("Ya existe un colaborador con la cédula " + c.getCedula() + ".");
        }
        colaboradores.add(c);
    }

    /** Devuelve el colaborador o null si no existe. */
    public Colaborador buscarColaborador(String cedula) {
        for (Colaborador c : colaboradores) {
            if (c.getCedula().equals(cedula == null ? "" : cedula.trim())) {
                return c;
            }
        }
        return null;
    }

    public void modificarColaborador(String cedula, String nombre, double nuevaTarifa) {
        Colaborador c = colaboradorObligatorio(cedula);
        c.setNombre(nombre);
        c.actualizarTarifa(nuevaTarifa);
    }

    public void certificarTostador(String cedula, boolean certificado) {
        Colaborador c = colaboradorObligatorio(cedula);
        if (!(c instanceof Tostador)) {
            throw new IllegalArgumentException("El colaborador " + cedula + " no es un Tostador.");
        }
        ((Tostador) c).setCertificado(certificado);
    }

    public void asignarBono(String cedula, double bono) {
        Colaborador c = colaboradorObligatorio(cedula);
        if (!(c instanceof Catador)) {
            throw new IllegalArgumentException("El colaborador " + cedula + " no es un Catador.");
        }
        ((Catador) c).setBono(bono);
    }

    public void eliminarColaborador(String cedula) {
        colaboradores.remove(colaboradorObligatorio(cedula));
    }

    public List<Colaborador> listarColaboradores() {
        return new ArrayList<>(colaboradores);
    }

    /** Colaboradores que cumplen los requisitos de una etapa. */
    public List<Colaborador> colaboradoresAptos(Etapa e) {
        List<Colaborador> aptos = new ArrayList<>();
        for (Colaborador c : colaboradores) {
            if (e.validarRequisitos(null, c, bodega)) {
                aptos.add(c);
            }
        }
        return aptos;
    }

    /** Devuelve la etapa que se ejecuta cuando el lote está en ese estado (null si no hay). */
    public Etapa etapaDeEstado(EstadoLote estado) {
        switch (estado) {
            case EN_TOSTADO:
                return etapaTostado;
            case CONTROL_CALIDAD:
                return etapaCalidad;
            case EN_EMPAQUE:
            case BLOQUEADO:
                return etapaEmpaque;
            default:
                return null;
        }
    }

    /** Pago de cada colaborador. Cada uno calcula distinto gracias al polimorfismo. */
    public String planillaPagos() {
        StringBuilder sb = new StringBuilder();
        double total = 0;
        for (Colaborador c : colaboradores) {
            double pago = c.calcularPago();
            total += pago;
            sb.append(String.format("%-16s | %-20s | CRC %12.2f%n", c.getTipo(), c.getNombre(), pago));
        }
        sb.append(String.format("%-39s | CRC %12.2f", "TOTAL", total));
        return sb.toString();
    }

    // ---------- Insumos ----------

    public void registrarInsumo(Insumo i) {
        bodega.agregar(i);
    }

    public Insumo buscarInsumo(String cod) {
        return bodega.buscar(cod);
    }

    public void modificarInsumo(String cod, String nombre, double nivelMinimo) {
        Insumo i = insumoObligatorio(cod);
        i.setNombre(nombre);
        i.setNivelMinimo(nivelMinimo);
    }

    public void eliminarInsumo(String cod) {
        bodega.eliminar(cod);
    }

    public void reponerInsumo(String cod, double cant) {
        bodega.reponer(cod, cant);
    }

    public List<Insumo> listarInsumos() {
        return bodega.listar();
    }

    public List<Insumo> insumosCriticos() {
        return bodega.insumosCriticos();
    }

    // ---------- Lotes ----------

    /**
     * Registra el lote, descuenta de la bodega los kg del grano verde usado
     * y deja como costo inicial el costo de ese grano.
     */
    public void registrarLote(Lote l, String codGrano) throws InsumoInsuficienteException {
        if (buscarLote(l.getCodigo()) != null) {
            throw new IllegalArgumentException("Ya existe un lote con el código " + l.getCodigo() + ".");
        }
        GranoVerde grano = buscarGrano(codGrano);
        bodega.consumir(grano.getCodigo(), l.getPesoInicialKg());
        l.sumarCosto(l.getPesoInicialKg() * grano.getCostoPorKg());
        lotes.add(l);
    }

    /** Devuelve el grano verde con ese código, o falla si no existe o no es grano. */
    public GranoVerde buscarGrano(String codGrano) {
        Insumo i = insumoObligatorio(codGrano);
        if (!(i instanceof GranoVerde)) {
            throw new IllegalArgumentException("El insumo " + codGrano + " no es un grano verde.");
        }
        return (GranoVerde) i;
    }

    /** Devuelve el lote o null si no existe. */
    public Lote buscarLote(String cod) {
        for (Lote l : lotes) {
            if (l.getCodigo().equalsIgnoreCase(cod == null ? "" : cod.trim())) {
                return l;
            }
        }
        return null;
    }

    public List<Lote> listarLotes() {
        return new ArrayList<>(lotes);
    }

    public void registrarPuntaje(String cod, int puntaje) {
        Lote l = loteObligatorio(cod);
        if (l.getEstado() != EstadoLote.CONTROL_CALIDAD) {
            throw new IllegalStateException("El lote no está en CONTROL_CALIDAD, está en " + l.getEstado() + ".");
        }
        l.registrarPuntaje(puntaje);
    }

    /**
     * Avanza el lote a la siguiente etapa según su estado actual.
     * La cédula es del colaborador que ejecuta la etapa (no se usa en RECEPCION).
     */
    public void avanzarLote(String cod, String cedula)
            throws ColaboradorNoAptoException, InsumoInsuficienteException {
        Lote l = loteObligatorio(cod);
        if (l.getEstado() == EstadoLote.RECEPCION) {
            l.cambiarEstado(EstadoLote.EN_TOSTADO);
            return;
        }
        if (l.getEstado() == EstadoLote.FINALIZADO) {
            throw new IllegalStateException("El lote ya está FINALIZADO.");
        }
        if (l.getEstado() == EstadoLote.RECHAZADO) {
            throw new IllegalStateException("El lote está RECHAZADO: use reprocesar o descartar.");
        }
        Etapa etapa = etapaDeEstado(l.getEstado());
        Colaborador c = colaboradorObligatorio(cedula);
        etapa.ejecutar(l, c, bodega);
    }

    /** Un lote rechazado se reprocesa una sola vez y vuelve a CONTROL_CALIDAD. */
    public void reprocesarLote(String cod) {
        Lote l = loteObligatorio(cod);
        if (l.getEstado() != EstadoLote.RECHAZADO) {
            throw new IllegalStateException("Solo se puede reprocesar un lote RECHAZADO.");
        }
        if (l.getReprocesos() >= 1) {
            throw new IllegalStateException("El lote ya fue reprocesado una vez: solo se puede descartar.");
        }
        l.sumarReproceso();
        l.cambiarEstado(EstadoLote.CONTROL_CALIDAD);
    }

    /** Elimina el lote del sistema. No se permite descartar uno ya FINALIZADO. */
    public void descartarLote(String cod) {
        Lote l = loteObligatorio(cod);
        if (l.getEstado() == EstadoLote.FINALIZADO) {
            throw new IllegalStateException("No se puede descartar un lote FINALIZADO.");
        }
        lotes.remove(l);
    }

    public double costoLote(String cod) {
        return loteObligatorio(cod).getCostoTotal();
    }

    public List<Lote> lotesBloqueados() {
        List<Lote> bloqueados = new ArrayList<>();
        for (Lote l : lotes) {
            if (l.getEstado() == EstadoLote.BLOQUEADO) {
                bloqueados.add(l);
            }
        }
        return bloqueados;
    }

    // ---------- Datos iniciales ----------

    public void cargarDatosIniciales() {
        registrarColaborador(new Tostador("Marco Vargas", "111111111", 3500, true));
        registrarColaborador(new Tostador("Luis Mora", "222222222", 3000, false));
        registrarColaborador(new OperarioEmpaque("Ana Rojas", "333333333", 400));
        registrarColaborador(new OperarioEmpaque("Pedro Solís", "444444444", 380));
        registrarColaborador(new Catador("Sofía Jiménez", "555555555", 450000));

        registrarInsumo(new GranoVerde("GV-01", "Café verde Tarrazú", 500, 100, "Tarrazú", "Caturra", 11, 2800));
        registrarInsumo(new GranoVerde("GV-02", "Café verde Naranjo", 300, 100, "Naranjo", "Catuaí", 10.5, 2500));
        registrarInsumo(new MaterialEmpaque("MP-01", "Bolsa 1 kg", 500, 100, "BOLSA"));
        registrarInsumo(new MaterialEmpaque("MP-02", "Etiqueta 1 kg", 50, 100, "ETIQUETA"));

        try {
            // L-001: se queda en RECEPCION
            registrarLote(new Lote("L-001", "Tarrazú", 50, "MEDIO"), "GV-01");

            // L-002: llega a CONTROL_CALIDAD
            registrarLote(new Lote("L-002", "Naranjo", 80, "CLARO"), "GV-02");
            avanzarLote("L-002", null);
            avanzarLote("L-002", "111111111");

            // L-003: llega a EN_EMPAQUE (pesa unos 83 kg y solo hay 50 etiquetas)
            registrarLote(new Lote("L-003", "Tarrazú", 100, "MEDIO"), "GV-01");
            avanzarLote("L-003", null);
            avanzarLote("L-003", "111111111");
            registrarPuntaje("L-003", 90);
            avanzarLote("L-003", "555555555");
        } catch (ColaboradorNoAptoException | InsumoInsuficienteException e) {
            throw new IllegalStateException("Error al cargar los datos iniciales: " + e.getMessage());
        }
    }

    // ---------- Auxiliares ----------

    private Colaborador colaboradorObligatorio(String cedula) {
        Colaborador c = buscarColaborador(cedula);
        if (c == null) {
            throw new IllegalArgumentException("No existe un colaborador con la cédula " + cedula + ".");
        }
        return c;
    }

    private Insumo insumoObligatorio(String cod) {
        Insumo i = bodega.buscar(cod);
        if (i == null) {
            throw new IllegalArgumentException("No existe un insumo con el código " + cod + ".");
        }
        return i;
    }

    private Lote loteObligatorio(String cod) {
        Lote l = buscarLote(cod);
        if (l == null) {
            throw new IllegalArgumentException("No existe un lote con el código " + cod + ".");
        }
        return l;
    }
}
