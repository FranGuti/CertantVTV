public class Vehiculo {
    EstadoVTV estado;
    String marca;
    String modelo;
    String dominio;

    public Vehiculo(String estado, String marca, String modelo, String dominio){
        if(estado.equals("apto")){
            this.estado = new EstadoVTVApto();
        }
        if(estado.equals("rechazado")){
            this.estado = new EstadoVTVRechazada();
        }
        if(estado.equals("condicional")){
            this.estado = new EstadoVTVCondicional();
        }
        this.marca = marca;
        this.modelo = modelo;
        this.dominio = dominio;
    }

}
