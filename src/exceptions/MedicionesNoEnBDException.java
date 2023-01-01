package exceptions;

public class MedicionesNoEnBDException extends RuntimeException {
    public MedicionesNoEnBDException(String s) {
        super((s));
    }
}
