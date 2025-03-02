package giis.demo.util;

/**
 * Excepción producida por la aplicación antes situaciones que no deberían ocurrir pero que son controladas
 * y por tanto, la aplicación se puede recuperar (validación de datos, prerequisitos que no se cumplen, etc)
 */
@SuppressWarnings("serial")
public class ApplicationException extends RuntimeException {
    public ApplicationException(Throwable e) {
        super(e);
    }
    public ApplicationException(String s) {
        super(s);
    }
}