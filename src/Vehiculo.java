import java.sql.Date;

public class Vehiculo {
    EstadoVTV estado;
    String marca;
    String modelo;
    String dominio;
    String duenio;
    Date fecha;

    public Vehiculo(String dominio, String marca, String modelo, String duenio) {
        this.dominio = dominio;
        this.marca = marca;
        this. modelo = modelo;
        this.duenio = duenio;
    }


    public void imprimirReporteSencillo() {
        System.out.println("DOMINIO: "+dominio + "  |  MARCA: " + marca + "  | MODELO: " + modelo +
                "  | PROPIETARIO: " + duenio);
    }

    public void imprimirResumen() {
        System.out.printf("marca '%s', modelo '%s', patente '%s'%n", marca, modelo, dominio);
    }
}
