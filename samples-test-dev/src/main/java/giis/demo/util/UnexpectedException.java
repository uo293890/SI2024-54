package giis.demo.util;

/**
 * Excepción producida por la aplicación antes situaciones incontroladas
 * (excepciones al acceder a la base de datos o al utilizar métodos que declaran excepciones throwable, etc)
 */
@SuppressWarnings("serial")
public class UnexpectedException extends RuntimeException {
    public UnexpectedException(Throwable e) {
        super(e);
    }
    public UnexpectedException(String s) {
        super(s);
    }
}