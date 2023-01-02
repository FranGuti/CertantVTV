import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class DueñoTest {
    @Test
    public void creoDuenioExentoYNombreEsCorrecto(){
        String nombre = "Carlos Tevez";
        int dni = 32;

        Dueño dueño = new DueñoExento(nombre, dni);

        Assertions.assertEquals(nombre, dueño.getNombre());
    }

    @Test
    public void creoDuenioExentoYDNIEsCorrecto(){
        String nombre = "Carlos Tevez";
        int dni = 32;

        Dueño dueño = new DueñoExento(nombre, dni);

        Assertions.assertEquals(dni, dueño.getDNI());
    }

    @Test
    public void creoDuenioComunYNombreEsCorrecto(){
        String nombre = "Carlos Tevez";
        int dni = 32;

        Dueño dueño = new DueñoComun(nombre, dni);

        Assertions.assertEquals(nombre, dueño.getNombre());
    }

    @Test
    public void creoDuenioComunYDNIEsCorrecto(){
        String nombre = "Carlos Tevez";
        int dni = 32;

        Dueño dueño = new DueñoComun(nombre, dni);

        Assertions.assertEquals(dni, dueño.getDNI());
    }
}
