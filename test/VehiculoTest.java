import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.Date;

public class VehiculoTest {

    @Test
    public void creoVehiculoExentoYEsNissan(){
        String marca = "Nissan";
        String modelo = "March";
        String dominio = "BBB001";
        Dueño duenio = new DueñoExento("Carlos Tevez", 32);

        Vehiculo vehiculo = new Vehiculo(dominio, marca, modelo, duenio);

        Assertions.assertEquals(marca, vehiculo.getMarca());
    }

    @Test
    public void creoVehiculoExentoYEsMarch(){
        String marca = "Nissan";
        String modelo = "March";
        String dominio = "BBB001";
        Dueño duenio = new DueñoExento("Carlos Tevez", 32);

        Vehiculo vehiculo = new Vehiculo(dominio, marca, modelo, duenio);

        Assertions.assertEquals(modelo, vehiculo.getModelo());
    }

    @Test
    public void creoVehiculoExentoYDominioEsCorrecto(){
        String marca = "Nissan";
        String modelo = "March";
        String dominio = "BBB001";
        Dueño duenio = new DueñoExento("Carlos Tevez", 32);

        Vehiculo vehiculo = new Vehiculo(dominio, marca, modelo, duenio);

        Assertions.assertEquals(dominio, vehiculo.getDominio());
    }

    @Test
    public void creoVehiculoExentoYDuenioExento(){
        String marca = "Nissan";
        String modelo = "March";
        String dominio = "BBB001";
        Dueño duenio = new DueñoExento("Carlos Tevez", 32);

        Vehiculo vehiculo = new Vehiculo(dominio, marca, modelo, duenio);

        Assertions.assertEquals("Dueño Exento", vehiculo.getTipoDuenio());
    }

    @Test
    public void creoVehiculoNoExentoYEsNissan(){
        EstadoVTV estado = new EstadoVTVApto();
        String marca = "Nissan";
        String modelo = "March";
        String dominio = "BBB001";
        Dueño duenio = new DueñoComun("Carlos Tevez", 32);

        Vehiculo vehiculo = new Vehiculo(dominio, marca, modelo, duenio);

        Assertions.assertEquals("Nissan", vehiculo.getMarca());
    }

    @Test
    public void creoVehiculoNoExentoYEsMarch(){
        String marca = "Nissan";
        String modelo = "March";
        String dominio = "BBB001";
        Dueño duenio = new DueñoComun("Carlos Tevez", 32);

        Vehiculo vehiculo = new Vehiculo(dominio, marca, modelo, duenio);

        Assertions.assertEquals(modelo, vehiculo.getModelo());
    }

    @Test
    public void creoVehiculoNoExentoYDominioEsCorrecto(){
        String marca = "Nissan";
        String modelo = "March";
        String dominio = "BBB001";
        Dueño duenio = new DueñoComun("Carlos Tevez", 32);

        Vehiculo vehiculo = new Vehiculo(dominio, marca, modelo, duenio);

        Assertions.assertEquals(dominio, vehiculo.getDominio());
    }

    @Test
    public void creoVehiculoNoExentoYDuenioComun(){
        String marca = "Nissan";
        String modelo = "March";
        String dominio = "BBB001";
        Dueño duenio = new DueñoComun("Carlos Tevez", 32);

        Vehiculo vehiculo = new Vehiculo(dominio, marca, modelo, duenio);

        Assertions.assertEquals("Dueño común", vehiculo.getTipoDuenio());
    }
}
