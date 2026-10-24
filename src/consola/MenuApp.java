package consola;

import excepciones.ColaboradorNoAptoException;
import excepciones.InsumoInsuficienteException;
import inventario.GranoVerde;
import inventario.Insumo;
import inventario.MaterialEmpaque;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;
import personal.Catador;
import personal.Colaborador;
import personal.OperarioEmpaque;
import personal.Tostador;
import produccion.EstadoLote;
import produccion.Lote;
import servicio.ServicioProduccion;

/** Interfaz de consola: solo lee datos, muestra resultados y llama al servicio. */
public class MenuApp {
    private final ServicioProduccion servicio = new ServicioProduccion();
    private final Scanner entrada = new Scanner(System.in);

    public static void main(String[] args) {
        MenuApp app = new MenuApp();
        app.servicio.cargarDatosIniciales();
        try {
            app.mostrar();
        } catch (NoSuchElementException e) {
            System.out.println("\nSe terminó la entrada de datos. Fin del programa.");
        }
    }

    /** Menú principal. */
    public void mostrar() {
        int op = -1;
        while (op != 0) {
            System.out.println("\n===== TOSTADORA Y EMPACADORA DE CAFÉ =====");
            System.out.println("1. Personal");
            System.out.println("2. Inventario (bodega)");
            System.out.println("3. Producción (lotes)");
            System.out.println("0. Salir");
            op = leerEntero("Opción: ");
            if (op == 1) {
                menuPersonal();
            } else if (op == 2) {
                menuInventario();
            } else if (op == 3) {
                menuProduccion();
            } else if (op != 0) {
                System.out.println("Opción inválida.");
            }
        }
        System.out.println("¡Hasta pronto!");
    }

    // ---------- Personal ----------

    public void menuPersonal() {
        int op = -1;
        while (op != 0) {
            System.out.println("\n--- PERSONAL ---");
            System.out.println("1. Agregar colaborador");
            System.out.println("2. Listar colaboradores");
            System.out.println("3. Consultar por cédula");
            System.out.println("4. Modificar colaborador");
            System.out.println("5. Eliminar colaborador");
            System.out.println("6. Planilla de pagos");
            System.out.println("7. Colaboradores aptos para una etapa");
            System.out.println("0. Volver");
            op = leerEntero("Opción: ");
            try {
                switch (op) {
                    case 1:
                        agregarColaborador();
                        break;
                    case 2:
                        mostrarLista(servicio.listarColaboradores());
                        break;
                    case 3:
                        consultarColaborador();
                        break;
                    case 4:
                        modificarColaborador();
                        break;
                    case 5:
                        servicio.eliminarColaborador(leerTexto("Cédula a eliminar: "));
                        System.out.println("Colaborador eliminado.");
                        break;
                    case 6:
                        System.out.println(servicio.planillaPagos());
                        break;
                    case 7:
                        aptosParaEtapa();
                        break;
                    case 0:
                        break;
                    default:
                        System.out.println("Opción inválida.");
                }
            } catch (IllegalArgumentException | IllegalStateException e) {
                System.out.println("ERROR: " + e.getMessage());
            }
        }
    }

    private void agregarColaborador() {
        System.out.println("Tipo: 1. Tostador  2. Operario de empaque  3. Catador");
        int tipo = leerEntero("Tipo: ");
        if (tipo < 1 || tipo > 3) {
            System.out.println("Tipo inválido.");
            return;
        }
        String nombre = leerTexto("Nombre: ");
        String cedula = leerTexto("Cédula (9 dígitos): ");
        Colaborador c;
        if (tipo == 1) {
            double tarifa = leerDecimal("Tarifa por hora: ");
            boolean cert = leerSiNo("¿Está certificado? (s/n): ");
            c = new Tostador(nombre, cedula, tarifa, cert);
        } else if (tipo == 2) {
            c = new OperarioEmpaque(nombre, cedula, leerDecimal("Tarifa por kilo: "));
        } else {
            c = new Catador(nombre, cedula, leerDecimal("Salario fijo: "));
        }
        servicio.registrarColaborador(c);
        System.out.println("Colaborador registrado.");
    }

    private void consultarColaborador() {
        Colaborador c = servicio.buscarColaborador(leerTexto("Cédula: "));
        System.out.println(c == null ? "No se encontró el colaborador." : c);
    }

    private void modificarColaborador() {
        String cedula = leerTexto("Cédula del colaborador: ");
        Colaborador c = servicio.buscarColaborador(cedula);
        if (c == null) {
            System.out.println("No se encontró el colaborador.");
            return;
        }
        System.out.println("Actual: " + c);
        String nombre = leerTexto("Nuevo nombre: ");
        double tarifa = leerDecimal("Nueva tarifa o salario base: ");
        servicio.modificarColaborador(cedula, nombre, tarifa);
        if (c instanceof Tostador) {
            servicio.certificarTostador(cedula, leerSiNo("¿Está certificado? (s/n): "));
        } else if (c instanceof Catador) {
            servicio.asignarBono(cedula, leerDecimal("Bono (0 si no tiene): ", true));
        }
        System.out.println("Colaborador modificado: " + servicio.buscarColaborador(cedula));
    }

    private void aptosParaEtapa() {
        System.out.println("Etapa: 1. Tostado  2. Calidad  3. Empaque");
        int op = leerEntero("Etapa: ");
        EstadoLote[] estados = {EstadoLote.EN_TOSTADO, EstadoLote.CONTROL_CALIDAD, EstadoLote.EN_EMPAQUE};
        if (op < 1 || op > 3) {
            System.out.println("Etapa inválida.");
            return;
        }
        List<Colaborador> aptos = servicio.colaboradoresAptos(servicio.etapaDeEstado(estados[op - 1]));
        System.out.println("Colaboradores aptos:");
        mostrarLista(aptos);
    }

    // ---------- Inventario ----------

    public void menuInventario() {
        int op = -1;
        while (op != 0) {
            System.out.println("\n--- INVENTARIO ---");
            System.out.println("1. Agregar insumo");
            System.out.println("2. Listar insumos");
            System.out.println("3. Consultar por código");
            System.out.println("4. Modificar insumo");
            System.out.println("5. Eliminar insumo");
            System.out.println("6. Reponer insumo");
            System.out.println("7. Alertas: insumos críticos");
            System.out.println("0. Volver");
            op = leerEntero("Opción: ");
            try {
                switch (op) {
                    case 1:
                        agregarInsumo();
                        break;
                    case 2:
                        mostrarLista(servicio.listarInsumos());
                        break;
                    case 3:
                        Insumo i = servicio.buscarInsumo(leerTexto("Código: "));
                        System.out.println(i == null ? "No se encontró el insumo." : i);
                        break;
                    case 4:
                        modificarInsumo();
                        break;
                    case 5:
                        servicio.eliminarInsumo(leerTexto("Código a eliminar: "));
                        System.out.println("Insumo eliminado.");
                        break;
                    case 6:
                        String cod = leerTexto("Código: ");
                        servicio.reponerInsumo(cod, leerDecimal("Cantidad a reponer: "));
                        System.out.println("Repuesto. " + servicio.buscarInsumo(cod));
                        break;
                    case 7:
                        List<Insumo> criticos = servicio.insumosCriticos();
                        System.out.println(criticos.isEmpty() ? "No hay insumos críticos." : "¡ALERTA! Insumos críticos:");
                        mostrarLista(criticos);
                        break;
                    case 0:
                        break;
                    default:
                        System.out.println("Opción inválida.");
                }
            } catch (IllegalArgumentException | IllegalStateException e) {
                System.out.println("ERROR: " + e.getMessage());
            }
        }
    }

    private void agregarInsumo() {
        System.out.println("Tipo: 1. Grano verde  2. Material de empaque");
        int tipo = leerEntero("Tipo: ");
        if (tipo != 1 && tipo != 2) {
            System.out.println("Tipo inválido.");
            return;
        }
        String codigo = leerTexto("Código: ");
        String nombre = leerTexto("Nombre: ");
        double cantidad = leerDecimal(tipo == 1 ? "Cantidad (kg): " : "Cantidad (unidades): ");
        double minimo = leerDecimal("Nivel mínimo: ");
        if (tipo == 1) {
            String origen = leerTexto("Origen: ");
            String variedad = leerTexto("Variedad: ");
            double humedad = leerDecimal("Humedad (%): ");
            double costo = leerDecimal("Costo por kg: ");
            servicio.registrarInsumo(new GranoVerde(codigo, nombre, cantidad, minimo, origen, variedad, humedad, costo));
        } else {
            String t = leerTexto("Tipo (BOLSA o ETIQUETA): ");
            servicio.registrarInsumo(new MaterialEmpaque(codigo, nombre, cantidad, minimo, t));
        }
        System.out.println("Insumo registrado.");
    }

    private void modificarInsumo() {
        String cod = leerTexto("Código del insumo: ");
        Insumo i = servicio.buscarInsumo(cod);
        if (i == null) {
            System.out.println("No se encontró el insumo.");
            return;
        }
        System.out.println("Actual: " + i);
        String nombre = leerTexto("Nuevo nombre: ");
        double minimo = leerDecimal("Nuevo nivel mínimo: ");
        servicio.modificarInsumo(cod, nombre, minimo);
        System.out.println("Insumo modificado: " + servicio.buscarInsumo(cod));
    }

    // ---------- Producción ----------

    public void menuProduccion() {
        int op = -1;
        while (op != 0) {
            System.out.println("\n--- PRODUCCIÓN ---");
            System.out.println("1. Registrar lote");
            System.out.println("2. Listar lotes");
            System.out.println("3. Consultar lote");
            System.out.println("4. Registrar puntaje de calidad");
            System.out.println("5. Avanzar lote");
            System.out.println("6. Reprocesar lote rechazado");
            System.out.println("7. Descartar lote");
            System.out.println("8. Costo de un lote");
            System.out.println("9. Lotes bloqueados");
            System.out.println("0. Volver");
            op = leerEntero("Opción: ");
            try {
                switch (op) {
                    case 1:
                        registrarLote();
                        break;
                    case 2:
                        mostrarLista(servicio.listarLotes());
                        break;
                    case 3:
                        Lote l = servicio.buscarLote(leerTexto("Código del lote: "));
                        System.out.println(l == null ? "No se encontró el lote." : l);
                        break;
                    case 4:
                        String cp = leerTexto("Código del lote: ");
                        servicio.registrarPuntaje(cp, leerEntero("Puntaje (0 a 100): "));
                        System.out.println("Puntaje registrado. Ahora puede avanzar el lote con un catador.");
                        break;
                    case 5:
                        avanzarLote();
                        break;
                    case 6:
                        String cr = leerTexto("Código del lote: ");
                        servicio.reprocesarLote(cr);
                        System.out.println("Lote enviado de nuevo a CONTROL_CALIDAD (segunda y última cata).");
                        break;
                    case 7:
                        servicio.descartarLote(leerTexto("Código del lote: "));
                        System.out.println("Lote descartado.");
                        break;
                    case 8:
                        String cc = leerTexto("Código del lote: ");
                        System.out.printf("Costo total del lote %s: CRC %.2f%n", cc, servicio.costoLote(cc));
                        break;
                    case 9:
                        List<Lote> bloqueados = servicio.lotesBloqueados();
                        if (bloqueados.isEmpty()) {
                            System.out.println("No hay lotes bloqueados.");
                        }
                        for (Lote b : bloqueados) {
                            System.out.println(b + "\n   Motivo: " + b.getMotivoBloqueo());
                        }
                        break;
                    case 0:
                        break;
                    default:
                        System.out.println("Opción inválida.");
                }
            } catch (IllegalArgumentException | IllegalStateException | InsumoInsuficienteException
                     | ColaboradorNoAptoException e) {
                System.out.println("ERROR: " + e.getMessage());
            }
        }
    }

    private void registrarLote() throws InsumoInsuficienteException {
        String codigo = leerTexto("Código del lote: ");
        String codGrano = leerTexto("Código del grano verde a usar (ej. GV-01): ");
        double peso = leerDecimal("Peso (kg): ");
        String perfil = leerTexto("Perfil de tueste (CLARO, MEDIO u OSCURO): ");
        GranoVerde grano = servicio.buscarGrano(codGrano);
        servicio.registrarLote(new Lote(codigo, grano.getOrigen(), peso, perfil), codGrano);
        System.out.println("Lote registrado: " + servicio.buscarLote(codigo));
    }

    private void avanzarLote() throws ColaboradorNoAptoException, InsumoInsuficienteException {
        String cod = leerTexto("Código del lote: ");
        Lote l = servicio.buscarLote(cod);
        if (l == null) {
            System.out.println("No se encontró el lote.");
            return;
        }
        System.out.println("Estado actual: " + l.getEstado());
        String cedula = "";
        if (l.getEstado() != EstadoLote.RECEPCION) {
            cedula = leerTexto("Cédula del colaborador que ejecuta la etapa: ");
        }
        servicio.avanzarLote(cod, cedula);
        System.out.println("Nuevo estado: " + l.getEstado());
        if (l.getEstado() == EstadoLote.BLOQUEADO) {
            System.out.println("¡LOTE BLOQUEADO! " + l.getMotivoBloqueo());
            System.out.println("Reponga el material en Inventario y vuelva a avanzar el lote.");
        } else if (l.getEstado() == EstadoLote.RECHAZADO) {
            System.out.println(l.getReprocesos() == 0
                    ? "El lote no alcanzó el puntaje mínimo. Puede reprocesarlo una vez."
                    : "El lote fue rechazado otra vez. Solo se puede descartar.");
        }
        System.out.println(l);
    }

    // ---------- Lectura de datos ----------

    /** Pide un entero y repite hasta que el usuario escriba uno válido. */
    public int leerEntero(String msg) {
        while (true) {
            System.out.print(msg);
            String linea = entrada.nextLine().trim();
            try {
                return Integer.parseInt(linea);
            } catch (NumberFormatException e) {
                System.out.println("Debe escribir un número entero.");
            }
        }
    }

    private double leerDecimal(String msg) {
        return leerDecimal(msg, false);
    }

    private double leerDecimal(String msg, boolean permiteCero) {
        while (true) {
            System.out.print(msg);
            String linea = entrada.nextLine().trim().replace(',', '.');
            try {
                double n = Double.parseDouble(linea);
                if (n < 0 || (n == 0 && !permiteCero) || Double.isNaN(n) || Double.isInfinite(n)) {
                    System.out.println(permiteCero ? "El número no puede ser negativo." : "El número debe ser mayor que 0.");
                } else {
                    return n;
                }
            } catch (NumberFormatException e) {
                System.out.println("Debe escribir un número (ejemplo: 12.5).");
            }
        }
    }

    private String leerTexto(String msg) {
        System.out.print(msg);
        return entrada.nextLine().trim();
    }

    private boolean leerSiNo(String msg) {
        while (true) {
            String r = leerTexto(msg).toLowerCase();
            if (r.equals("s") || r.equals("si") || r.equals("sí")) {
                return true;
            }
            if (r.equals("n") || r.equals("no")) {
                return false;
            }
            System.out.println("Responda s o n.");
        }
    }

    private void mostrarLista(List<?> elementos) {
        if (elementos.isEmpty()) {
            System.out.println("(no hay elementos)");
        }
        for (Object o : elementos) {
            System.out.println(o);
        }
    }
}
