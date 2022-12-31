import java.util.Calendar;
import java.util.Date;
import java.util.Map;

public class Inspeccion {
    private final Vehiculo vehiculo;
    private Dueño duenio;
    private Date fecha;
    private EstadoVTV estado;
    private String inspector;
    private Map<String, String> observaciones;
    private Map<String, String> mediciones;
    private boolean vencido = false;



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

        asignarEstadoYVencimiento(estado);


    }


    public Inspeccion(String dominio, String marca, String modelo, String duenio, Date fecha, String estado) {
        this.vehiculo = new Vehiculo(dominio, marca, modelo, duenio);
        this.fecha = fecha;
        asignarEstadoYVencimiento(estado);
    }

    private void asignarEstadoYVencimiento(String estado) {
        if("apto".equals(estado)){

            if(!aptoEnFecha()){
                this.vencido = true;
            }
            this.estado = new EstadoVTVApto();

        }

        if("condicional".equals(estado)){

            if(!condicionalEnFecha()){
                this.vencido = true;
            }
            this.estado = new EstadoVTVCondicional();

        }

        if("rechazado".equals(estado)){
            this.estado = new EstadoVTVRechazada();
        }
    }

    private boolean estaVencida(){
        return vencido;
    }

    private boolean condicionalEnFecha() {
        Date hoy = new Date();
        return fecha.compareTo(hoy) == 0;
    }

    private boolean aptoEnFecha() {
        Date hoy = new Date();
        Date haceUnAnio = restarUnAnio(hoy);
        return fecha.compareTo(haceUnAnio) >= 0;
    }

    private Date restarUnAnio(Date hoy) {
        Calendar calendario = Calendar.getInstance();
        calendario.setTime(hoy);
        calendario.add(calendario.YEAR, -1);
        return calendario.getTime();
    }


    public void imprimirReporteSencilloConVencimiento() {
        if(!vencido){
            vehiculo.imprimirReporteSencillo();
        }
    }

    public void imprimirReporteSencilloSinVencimiento() {
        vehiculo.imprimirReporteSencillo();
    }

    public void imprimirReporteDetallado() {
        System.out.println();
        System.out.println("-----------------------------------------------------------------------------------");
        System.out.println();
        System.out.println(fecha);
        System.out.print("El vehiculo ");
        vehiculo.imprimirResumen();
        System.out.print("Cuyo titular ");
        duenio.imprimirResumen();
        System.out.println("Realizó una inspección a cargo del inspector " + inspector);
        System.out.print("Con resultado ");
        estado.imprimirResumen();
        System.out.print(", habiéndose desempeñado de la siguiente manera: ");
        System.out.println();
        imprimirObservaciones();
        System.out.println();
        imprimirMediciones();
        System.out.println();
        System.out.println("-----------------------------------------------------------------------------------");
        System.out.println();
    }


    private void imprimirObservaciones() {
        System.out.println("Observaciones:");
        for(Map.Entry<String,String> observacion : observaciones.entrySet()){
            System.out.printf("%s: %s. ", observacion.getKey(), observacion.getValue());
        }
    }

    private void imprimirMediciones() {
        System.out.println("Mediciones:");
        for(Map.Entry<String,String> medicion : mediciones.entrySet()){
            System.out.printf("%s: %s. ", medicion.getKey(), medicion.getValue());
        }
    }


}
