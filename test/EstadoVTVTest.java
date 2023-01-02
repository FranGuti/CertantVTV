import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class EstadoVTVTest {
    @Test
    public void creoEstadoVTVAptoYEsApto(){
        EstadoVTV estado = new EstadoVTVApto();

        Assertions.assertEquals("apto", estado.getEstado());
    }

    @Test
    public void creoEstadoVTVCondicionalYEsCondicional(){
        EstadoVTV estado = new EstadoVTVCondicional();

        Assertions.assertEquals("condicional", estado.getEstado());
    }

    @Test
    public void creoEstadoVTVRechazadoYEsRechazado(){
        EstadoVTV estado = new EstadoVTVRechazada();

        Assertions.assertEquals("rechazado", estado.getEstado());
    }
}
