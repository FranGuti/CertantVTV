import java.sql.Date;
import java.util.List;
import java.util.Map;

public class Inspeccion {
    private final Vehiculo vehiculo;
    private Dueño duenio;
    private Date fecha;
    private EstadoVTV estado;
    private String inspector;
    private Map<String, String> observaciones;
    private Map<String, String> mediciones;



    public Inspeccion(String dominio, String marca, String modelo, String duenio) {
        this.vehiculo = new Vehiculo(dominio, marca, modelo, duenio);
    }

    public Inspeccion(String marca, String modelo, String dominio, String titular, int documento,
                      boolean exento, Date fecha, String estado, String nombreInspector, Map<String, String> observaciones,
                      Map<String, String> mediciones) {

        this.vehiculo = new Vehiculo(dominio, marca, modelo, titular);

        if(exento){
            this.duenio = new DueñoExento(titular, documento);
        }
        else{
            this.duenio = new DueñoComun(titular, documento);
        }

        this.fecha = fecha;
        this.inspector = nombreInspector;
        this.observaciones = observaciones;
        this.mediciones = mediciones;

        if("apto".equals(estado)){
            this.estado = new EstadoVTVApto();
        }
        if("condicional".equals(estado)){
            this.estado = new EstadoVTVCondicional();
        }
        if("rechazado".equals(estado)){
            this.estado = new EstadoVTVRechazada();
        }

    }

    public void imprimirReporteDeAutomovil() {
        vehiculo.imprimirReporteSencillo();
    }

    public void imprimirReporteDetallado() {
        System.out.print("El vehiculo ");
        vehiculo.imprimirResumen();
        System.out.print("Cuyo titular ");
        duenio.imprimirResumen();
        System.out.println("Realizó una inspección el día " + fecha + " a cargo del inspector " + inspector);
        System.out.print("Con resultado ");
        estado.imprimirResumen();
        System.out.print(", habiéndose desempeñado de la siguiente manera: ");
        System.out.println();
        imprimirObservaciones();
        System.out.println();
        imprimirMediciones();


    }


    private void imprimirObservaciones() {
        System.out.println("Observaciones:");
        for(Map.Entry<String,String> observacion : observaciones.entrySet()){
            System.out.printf("%s: %s ", observacion.getKey(), observacion.getValue());
        }
    }

    private void imprimirMediciones() {
        System.out.println("Mediciones:");
        for(Map.Entry<String,String> medicion : mediciones.entrySet()){
            System.out.printf("%s: %s ", medicion.getKey(), medicion.getValue());
        }
    }
}
