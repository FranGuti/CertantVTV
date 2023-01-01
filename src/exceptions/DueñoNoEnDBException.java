package exceptions;

public class DueñoNoEnDBException extends RuntimeException {
    public DueñoNoEnDBException(String s) {
        super((s));
    }
}
