import exceptions.*;

import javax.swing.plaf.nimbus.State;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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


        if(exento){
            this.duenio = new DueñoExento(titular, documento);
        }
        else{
            this.duenio = new DueñoComun(titular, documento);
        }

        this.vehiculo = new Vehiculo(dominio, marca, modelo, this.duenio);

        this.fecha = fecha;
        this.inspector = nombreInspector;
        this.observaciones = observaciones;
        this.mediciones = mediciones;

        asignarEstadoYVencimiento(estado);


    }


    public Inspeccion(String dominio, String marca, String modelo, String duenio, Date fecha, String estado) {
        this.duenio = new DueñoComun(duenio, 0);
        this.vehiculo = new Vehiculo(dominio, marca, modelo, this.duenio);
        this.fecha = fecha;
        asignarEstadoYVencimiento(estado);
    }

    public Inspeccion(Vehiculo vehiculo, String estado, Map<String, String> observaciones,
                      Map<String, String> mediciones, String nombreInspector) {
        this.fecha = new Date();
        asignarEstadoYVencimiento(estado);
        this.vehiculo = vehiculo;
        this.observaciones = observaciones;
        this.mediciones = mediciones;
        this.inspector = nombreInspector;
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


    public void agregarADataBase(Connection con) throws SQLException {
        Statement stmtAlta = con.createStatement();
        try{
            stmtAlta.executeUpdate("INSERT INTO inspecciones (fecha, estado, inspector, vehiculo, observacion, medicion)" +
                " VALUES ('" + fecha.toString() + "', '" + estado.getEstado() + "', " + inspectorGetID(con) + ", " +
                vehiculo.getID(con) + ", " + observacionesGetID(con) + ", " + medicionesGetID(con) + ");");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (InspectorNoEnDBException e){
            agregarInspectorADataBase(con);
            agregarADataBase(con);
        }catch (VehiculoNoEnDBException e) {
            vehiculo.agregarADB(con);
            agregarADataBase(con);
        } catch (ObservacionesNoEnDBException e) {
            agregarObservacionesADataBase(con);
            agregarADataBase(con);
        } catch (MedicionesNoEnBDException e){
            agregarMedicionesADataBase(con);
            agregarADataBase(con);
        }
    }

    private void agregarInspectorADataBase(Connection con) throws SQLException {
        Statement stmtAgregarInspector = con.createStatement();
        String consultaAgregarInspector = "INSERT INTO inspectores (nombre) VALUES ('" + this.inspector + "');";
        stmtAgregarInspector.executeUpdate(consultaAgregarInspector);
    }

    private void agregarMedicionesADataBase(Connection con) throws SQLException {
        Statement stmtAgregarMediciones = con.createStatement();
        String consultaAgregarMed = "INSERT " +
                "INTO mediciones(suspension, dirección_y_tren_delantero, frenos, contaminacion) " +
                "VALUES ('" + mediciones.get("suspension") + "', '" + mediciones.get("dirección y tren delantero") + "', '" +
                mediciones.get("frenos") + "', '" + mediciones.get("contaminacion") +
                "');";
        stmtAgregarMediciones.executeUpdate(consultaAgregarMed);
    }

    private void agregarObservacionesADataBase(Connection con) throws SQLException {
        Statement stmtAgregarObservaciones = con.createStatement();
        String consultaAgregarObs = "INSERT " +
                "INTO observaciones(luces, patente, espejos, chasis, vidrios, seguridad_y_emergencia) " +
                "VALUES ('" + observaciones.get("luces") + "', '" + observaciones.get("patente") + "', '" +
                observaciones.get("espejos") + "', '" + observaciones.get("chasis") + "', '" +
                observaciones.get("vidrios") + "', '" + observaciones.get("seguridad y emergencia") +
                "');";
        stmtAgregarObservaciones.executeUpdate(consultaAgregarObs);
    }

    private String medicionesGetID(Connection con) throws SQLException {
        Statement stmtMedicionesID = con.createStatement();
        String consultaMedID = "SELECT m.id FROM mediciones m WHERE " +
                "m.suspension = '" + mediciones.get("suspension") +
                "' AND m.dirección_y_tren_delantero = '" + mediciones.get("dirección y tren delantero") +
                "' AND m.frenos = '" + mediciones.get("frenos") + "' AND m.contaminacion = '" +
                mediciones.get("contaminacion") + "';";
        ResultSet rs = stmtMedicionesID.executeQuery(consultaMedID);
        if(!rs.next()){
            throw new MedicionesNoEnBDException("No se encontraron las mediciones en la base de datos");
        }
        int id = rs.getInt("id");
        return Integer.toString(id);
    }

    private String observacionesGetID(Connection con) throws SQLException, ObservacionesNoEnDBException {
        Statement stmtObservacionesID = con.createStatement();
        String consultaObsID = "SELECT obs.id FROM observaciones obs WHERE " +
                "obs.luces = '" + observaciones.get("luces") + "' AND obs.patente = '" + observaciones.get("patente") +
                "' AND obs.espejos = '" + observaciones.get("espejos") + "' AND obs.chasis = '" +
                observaciones.get("chasis") + "' AND obs.vidrios = '" + observaciones.get("vidrios") +
                "' AND obs.seguridad_y_emergencia = '" + observaciones.get("seguridad y emergencia") + "';";
        ResultSet rs = stmtObservacionesID.executeQuery(consultaObsID);
        if(!rs.next()){
            throw new ObservacionesNoEnDBException("No se encontraron las observaciones en la base de datos");
        }
        int id = rs.getInt("id");
        return Integer.toString(id);
    }

    private String inspectorGetID(Connection con) throws SQLException {
        Statement stmtIDInspector = con.createStatement();
        String consultarIDInspector = "SELECT i.id FROM inspectores i WHERE i.nombre = '" + inspector + "';";
        ResultSet rs = stmtIDInspector.executeQuery(consultarIDInspector);
        if(!rs.next()){
            throw new InspectorNoEnDBException("El inspector solicitado no está en la base de datos");
        }
        return Integer.toString(rs.getInt("id"));
    }

    public void actualizar(Connection server, int numero) throws SQLException {

        try {
            Statement stmtActualizar = server.createStatement();
            String consultaActualizar = "UPDATE inspecciones SET ";

            if(this.inspector != null){
                consultaActualizar = consultaActualizar + "inspector = " + inspectorGetID(server);
            }

            if(this.estado != null && this.inspector != null){
                consultaActualizar = consultaActualizar + ", estado = '" + this.estado.getEstado() +
                "', observacion = " + observacionesGetID(server) + ", medicion = " + medicionesGetID(server);
            }
            if(this.estado != null && this.inspector == null){
                consultaActualizar = consultaActualizar + "estado = '" + this.estado.getEstado() +
                        "', observacion = " + observacionesGetID(server) + ", medicion = " + medicionesGetID(server);
            }

            if(this.vehiculo != null && this.inspector != null || this.vehiculo != null && this.estado != null){
                consultaActualizar = consultaActualizar + ", vehiculo = " + this.vehiculo.getID(server);
            }
            if(this.vehiculo != null && this.inspector == null && this.estado == null){
                consultaActualizar = consultaActualizar + "vehiculo = " + this.vehiculo.getID(server);
            }

            consultaActualizar = consultaActualizar + " WHERE id = " + numero + ";";

            stmtActualizar.executeUpdate(consultaActualizar);
        } catch (ObservacionesNoEnDBException e) {
            agregarObservacionesADataBase(server);
            actualizar(server, numero);
        } catch (MedicionesNoEnBDException e) {
            agregarMedicionesADataBase(server);
            actualizar(server, numero);
        } catch (VehiculoNoEnDBException e) {
            this.vehiculo.agregarADB(server);
            actualizar(server, numero);
        } catch (InspectorNoEnDBException e) {
            agregarInspectorADataBase(server);
            actualizar(server, numero);
        }

    }
}
