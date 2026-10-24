package excepciones;

/** Se lanza cuando se intenta consumir más cantidad de la que hay en bodega. */
public class InsumoInsuficienteException extends Exception {
    public InsumoInsuficienteException(String msg) {
        super(msg);
    }
}
