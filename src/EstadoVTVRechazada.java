public class EstadoVTVRechazada implements EstadoVTV{
    @Override
    public void imprimirResumen() {
        System.out.print("rechazado");
    }

    @Override
    public String getEstado() {
        return "rechazado";
    }
}
