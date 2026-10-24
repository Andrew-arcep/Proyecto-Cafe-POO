# Sistema de tostado y empaque de café (Proyecto 1 POO, IC-2101)

Aplicación de consola en Java 17 (sin bibliotecas externas ni base de datos; los datos viven en memoria).
Maneja tres áreas: **personal**, **inventario (bodega)** y **producción (lotes)**.

## Estructura

```
src/
  personal/      Colaborador, Tostador, OperarioEmpaque, Catador
  inventario/    Insumo, GranoVerde, MaterialEmpaque, Bodega
  produccion/    Lote, EstadoLote, Etapa, EtapaTostado, EtapaCalidad, EtapaEmpaque
  servicio/      ServicioProduccion
  consola/       MenuApp (contiene main)
  util/          Validador
  excepciones/   InsumoInsuficienteException, ColaboradorNoAptoException
pruebas/         Entradas de ejemplo para recorrer el menú
```

## Compilar y ejecutar (sin IDE)

Requiere JDK 17 o superior. Desde la carpeta del proyecto:

**Linux / macOS / Git Bash**
```bash
mkdir -p out
javac -encoding UTF-8 -d out $(find src -name "*.java")
java -cp out consola.MenuApp
```

**Windows (PowerShell)**
```powershell
New-Item -ItemType Directory -Force out | Out-Null
javac -encoding UTF-8 -d out (Get-ChildItem -Recurse src -Filter *.java).FullName
java -Dfile.encoding=UTF-8 -Dstdout.encoding=UTF-8 -cp out consola.MenuApp
```

Si en Windows se ven mal las tildes, ejecute antes `chcp 65001`.

### Probar con entradas de ejemplo

```bash
java -cp out consola.MenuApp < pruebas/entrada1.txt
java -cp out consola.MenuApp < pruebas/entrada2.txt
```

`entrada1.txt` recorre producción (tostador no certificado, operario en etapa equivocada, puntaje faltante,
rechazo y reproceso, bloqueo por falta de etiquetas, reposición y empaque final, entradas inválidas).
`entrada2.txt` prueba rechazo doble y descarte, y el CRUD de personal e inventario con sus errores.

## Datos iniciales

- **Personal:** Marco Vargas (tostador certificado, cédula 111111111), Luis Mora (tostador **no** certificado, 222222222),
  Ana Rojas (333333333) y Pedro Solís (444444444) operarios de empaque, Sofía Jiménez (catadora, 555555555).
- **Bodega:** GV-01 Tarrazú, GV-02 Naranjo, MP-01 bolsas (500) y MP-02 etiquetas (solo 50, es insumo crítico).
- **Lotes:** L-001 en `RECEPCION`, L-002 en `CONTROL_CALIDAD`, L-003 en `EN_EMPAQUE`
  (necesita ~83 etiquetas, así que al empacarlo se **bloquea**).

## Guion rápido de demostración

1. Producción → *Avanzar lote* `L-003` con la cédula `444444444`: queda `BLOQUEADO` y dice que faltan etiquetas.
2. Inventario → *Reponer* `MP-02` con 100: ya no está crítico.
3. Producción → *Avanzar lote* `L-003` otra vez: pasa a `FINALIZADO`.
4. *Avanzar* `L-001` (acepta el lote), luego con `222222222` (tostador no certificado): error. Con `111111111`: tuesta.
5. En `L-002`: *Registrar puntaje* (ej. 70), *avanzar* con `555555555`: `RECHAZADO`; *reprocesar*; repetir con 50 y ver que solo se puede descartar.
6. Personal → *Planilla de pagos*.

## Reglas de negocio principales

- Flujo: `RECEPCION → EN_TOSTADO → CONTROL_CALIDAD → EN_EMPAQUE → FINALIZADO` (con `RECHAZADO` y `BLOQUEADO`).
- Tostado: solo tostador certificado; merma 15 % claro / 17 % medio / 20 % oscuro; 1 hora por cada 10 kg (hacia arriba).
- Calidad: solo catador; puntaje mínimo 80; un lote rechazado se reprocesa una sola vez.
- Empaque: solo operario; 1 bolsa y 1 etiqueta por kg (hacia arriba); si falta material el lote se bloquea.
- Cédula de 9 dígitos y única; códigos únicos; cantidades, pesos y tarifas mayores que 0; textos no vacíos.

Los cambios respecto al diagrama de clases están en `CAMBIOS_AL_DIAGRAMA.md`.
