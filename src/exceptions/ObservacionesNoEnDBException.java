package exceptions;

public class ObservacionesNoEnDBException extends RuntimeException {
    public ObservacionesNoEnDBException(String s) {
        super((s));
    }
}
