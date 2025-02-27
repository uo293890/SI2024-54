package giis.demo.util;

import java.sql.SQLException;

public class UnexpectedException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    // Constructor que acepta un mensaje y una causa
    public UnexpectedException(String message, Throwable cause) {
        super(message, cause);
    }

    // Constructor que acepta solo un mensaje
    public UnexpectedException(String message) {
        super(message);
    }

    // Constructor que acepta solo una causa
    public UnexpectedException(Throwable cause) {
        super(cause);
    }
}
