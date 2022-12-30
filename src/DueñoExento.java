public class DueñoExento extends Dueño{
    public DueñoExento(String titular, int documento) {
        nombre = titular;
        dni = documento;
    }

    @Override
    public void imprimirResumen() {
        System.out.printf("%s, dni: %d se encuentra exento de pago%n", nombre, dni);
    }
}
