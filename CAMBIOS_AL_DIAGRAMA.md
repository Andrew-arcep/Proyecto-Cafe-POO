# Cambios al diagrama de clases

Lista de diferencias entre `Diagrama_Clases_Cafe.drawio` y el código, para actualizar el diagrama antes de documentar
(el profesor indicó que el diagrama se queda corto en las acciones a documentar).

## A. Pedidos por el instructivo

| # | Clase | Cambio | Justificación |
|---|-------|--------|---------------|
| 1 | `Lote` | Agregar `-puntajeCalidad: int` y `+registrarPuntaje(p: int): void` | `Etapa.ejecutar` no recibe el puntaje; se guarda en el lote antes de avanzar. Vale -1 mientras no se haya registrado. |
| 2 | `Lote` | Agregar `-reprocesos: int` | Controla que un lote rechazado se reprocese solo una vez. |

## B. Atributos y métodos nuevos en el modelo

| # | Clase | Cambio | Justificación |
|---|-------|--------|---------------|
| 3 | `Lote` | `-motivoBloqueo: String` con getter y setter | `lotesBloqueados` debe indicar qué insumo falta; el motivo se guarda al bloquear. |
| 4 | `Lote` | `+limpiarPuntaje()`, `+sumarReproceso()`, getters y `setPesoActualKg` | Tras usar el puntaje se borra para que la segunda cata exija uno nuevo. La merma cambia el peso actual. |
| 5 | `GranoVerde` | `-costoPorKg: double` | El costo inicial del lote es el costo del grano; el diagrama no tenía ese dato. |
| 6 | `Insumo` | `+reponer(cant: double): void`, `+getUnidad(): String` (abstracto), getters, `setNombre`, `setNivelMinimo` | `Bodega.reponer` necesita operar sobre el insumo; las unidades (kg / unidades) se muestran en pantalla; modificar insumos. |
| 7 | `Colaborador` | `+actualizarTarifa(nueva: double)` (abstracto), `+getTipo(): String` (abstracto), `getNombre`, `getCedula`, `setNombre` | Modificar colaboradores de forma polimórfica: cada subclase actualiza su tarifa (hora, kilo o salario). |
| 8 | `Tostador` | `+sumarHoras(h)`, `+setCertificado(b)`, getters | Registrar horas trabajadas (regla de tostado) y poder certificar/descertificar. |
| 9 | `OperarioEmpaque` | `+sumarKilos(k)`, getters | Registrar kilos empacados (regla de empaque). |
| 10 | `Catador` | `+setBono(b)`, getters | El bono forma parte del pago y debe poder asignarse. |
| 11 | `Bodega` | `+eliminar(cod: String): void`, `+buscarMaterial(tipo: String): MaterialEmpaque` | Eliminar insumos (CRUD). El empaque busca la bolsa y la etiqueta por tipo, no por código. |
| 12 | `Etapa` | `ejecutar` declara `throws ColaboradorNoAptoException, InsumoInsuficienteException` | Las excepciones son verificadas (extienden `Exception`); además `EtapaCalidad` lanza `IllegalStateException` si falta el puntaje. |

## C. Cambios en `ServicioProduccion`

| # | Cambio | Justificación |
|---|--------|---------------|
| 13 | `avanzarLote(cod)` pasa a `avanzarLote(cod, cedula)` | La etapa necesita saber qué colaborador la ejecuta. En `RECEPCION` la cédula no se usa. |
| 14 | `registrarLote(l)` pasa a `registrarLote(l, codGrano)` | Hay que saber de qué `GranoVerde` descontar los kg y calcular el costo inicial. |
| 15 | Nuevo `registrarPuntaje(cod, puntaje)` | Llama a `Lote.registrarPuntaje`; solo vale si el lote está en `CONTROL_CALIDAD`. |
| 16 | Nuevos `buscarColaborador(cedula)`, `modificarColaborador(cedula, nombre, tarifa)`, `eliminarColaborador(cedula)`, `certificarTostador(cedula, b)`, `asignarBono(cedula, bono)`, `listarColaboradores()` | CRUD de colaboradores que faltaba en el diagrama. |
| 17 | Nuevos `buscarInsumo(cod)`, `modificarInsumo(cod, nombre, nivelMinimo)`, `eliminarInsumo(cod)`, `reponerInsumo(cod, cant)`, `listarInsumos()`, `insumosCriticos()`, `buscarGrano(cod)` | CRUD de insumos y alerta de críticos (el menú solo habla con el servicio, no con `Bodega`). |
| 18 | Nuevo `listarLotes()` | Consulta de lotes. |
| 19 | Nuevo `planillaPagos(): String` | Planilla que usa polimorfismo con `calcularPago()` sobre la lista de `Colaborador`. |
| 20 | Nuevo `etapaDeEstado(estado): Etapa` | Devuelve la etapa que corresponde a un estado; se usa en `avanzarLote` y para consultar aptos desde el menú. |
| 21 | El servicio crea y tiene tres atributos: `-etapaTostado`, `-etapaCalidad`, `-etapaEmpaque` (de tipo `Etapa`) | Muestra la relación `ServicioProduccion` → `Etapa` (agregación/composición) que no aparece como atributo. |

## D. Cambios en `MenuApp`

| # | Cambio | Justificación |
|---|--------|---------------|
| 22 | Atributo `-entrada: Scanner` | Lectura de teclado. |
| 23 | Métodos privados auxiliares: `leerDecimal`, `leerTexto`, `leerSiNo`, `mostrarLista` y un método privado por cada acción larga | Mantener los menús cortos. |

## E. Validaciones y decisiones (supuestos)

- `Validador.validarCedula` exige exactamente **9 dígitos** (supuesto del equipo).
- `Lote.perfil` sigue siendo `String` (CLARO, MEDIO u OSCURO); se valida en el constructor (el diagrama lo tiene como String).
- `MaterialEmpaque.tipo` solo acepta BOLSA o ETIQUETA (se valida en el constructor).
- `descartarLote` **elimina** el lote del sistema; no se permite descartar un lote `FINALIZADO`.
- Las horas de tostado se calculan con el peso **antes** de la merma (1 hora por cada 10 kg, hacia arriba).
- El peso tras la merma se redondea a 2 decimales.
- El empaque calcula kilos = peso actual redondeado hacia arriba; un lote bloqueado no consume nada hasta que haya material para ambos (bolsas y etiquetas).
- Las excepciones `IllegalArgumentException` e `IllegalStateException` (no verificadas) se usan para datos inválidos y operaciones fuera de estado; `MenuApp` las captura y vuelve al menú.
- Moneda mostrada como "CRC" (colones), solo informativa.
