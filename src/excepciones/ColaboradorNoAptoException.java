package excepciones;

/** Se lanza cuando un colaborador no puede ejecutar una etapa (tipo o certificación incorrectos). */
public class ColaboradorNoAptoException extends Exception {
    public ColaboradorNoAptoException(String msg) {
        super(msg);
    }
}
