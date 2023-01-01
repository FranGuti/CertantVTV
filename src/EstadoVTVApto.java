public class EstadoVTVApto implements EstadoVTV{
    @Override
    public void imprimirResumen() {
        System.out.print("apto");
    }

    @Override
    public String getEstado() {
        return "apto";
    }
}
