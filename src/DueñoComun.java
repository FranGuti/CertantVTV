public class DueñoComun extends Dueño{
    public DueñoComun(String titular, int documento) {
        nombre = titular;
        dni = documento;
    }

    @Override
    public void imprimirResumen() {
        System.out.printf("%s, dni %d, no se encuentra exento de pago%n", nombre, dni);
    }
}
