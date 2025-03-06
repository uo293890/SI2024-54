package giis.demo.util;

@SuppressWarnings("serial")
public class ApplicationException extends RuntimeException {
    public ApplicationException(Throwable e) {
        super(e);
    }
    public ApplicationException(String s) {
        super(s);
    }
    // Añadir este nuevo constructor
    public ApplicationException(String message, Throwable cause) {
        super(message, cause);
    }
}