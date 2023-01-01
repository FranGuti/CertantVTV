public class EstadoVTVCondicional implements EstadoVTV{
    @Override
    public void imprimirResumen() {
        System.out.print("condicional");
    }

    @Override
    public String getEstado() {
        return "condicional";
    }
}
