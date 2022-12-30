public class Inspeccion {
    private String dominio;
    private String marca;
    private String modelo;
    private String duenio;

    public Inspeccion(String dominio, String marca, String modelo, String duenio) {
        this.dominio = dominio;
        this.marca = marca;
        this.modelo = modelo;
        this.duenio = duenio;
    }

    public void imprimirReporteDeAutomovil() {
        System.out.println("DOMINIO: "+dominio + "  |  MARCA: " + marca + "  | MODELO: " + modelo +
                            "  | PROPIETARIO: " + duenio);
    }
}
