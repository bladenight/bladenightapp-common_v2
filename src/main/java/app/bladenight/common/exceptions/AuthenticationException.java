package app.bladenight.common.exceptions;

public class AuthenticationException extends Exception {
    private static final long serialVersionUID = -7689511108768691734L;
    public AuthenticationException() {
        super();
    }
    public AuthenticationException(String message) {
        super(message);
    }
    public AuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }
    public AuthenticationException(Throwable cause) {
        super(cause);
    }
}
