package exceptions;

public class VehiculoNoEnDBException extends RuntimeException {
    public VehiculoNoEnDBException(String s) {
        super((s));
    }
}
