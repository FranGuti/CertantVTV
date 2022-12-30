import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;


public class Vtv {
    private static final Conexion con = new Conexion("certantvtv");
    private static Connection server;
    LocalDate objDia = LocalDate.now();
    DateTimeFormatter formateador = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private final String dia = objDia.format(formateador);

    public static void main(String[] args) throws SQLException {
        server = con.conectar();

        System.out.println("**************************************************");
        System.out.println("********************CERTANT-VTV*******************");
        System.out.println("**************************************************");
        System.out.println("Que movimiento desea realizar?");

        //Ciclo infinito hasta que no se le indique salida
        while(true){
            System.out.println("(1) Ver reportes, (2) Agregar/Quitar/Modificar, (3) Salir");
            Scanner scanner = new Scanner(System.in);
            String respuesta = scanner.nextLine();

            while(!"1".equals(respuesta) && !"2".equals(respuesta) && !"3".equals(respuesta)){         // Si es inválido el comando
                System.out.println("Comando inválido, ingrese el movimiento nuevamente: ");
                respuesta = scanner.nextLine();
            }

            if("1".equals(respuesta)){

                System.out.println("(1) Reporte de automóviles, (2) Reporte de inspecciones, (3) Reporte Personal");
                String respuestaDeReportes = scanner.nextLine();

                //Chequeo la validez del comando
                while(!"1".equals(respuestaDeReportes) && !"2".equals(respuestaDeReportes) && !"3".equals(respuestaDeReportes)){
                    System.out.println("Comando inválido, ingrese el movimiento nuevamente: ");
                    respuestaDeReportes = scanner.nextLine();
                }

                if("1".equals(respuestaDeReportes)){
                    imprimirReporteDeAutomoviles();
                }
                if("2".equals(respuestaDeReportes)){
                    imprimirReporteDeInspecciones();
                }
                if("3".equals(respuestaDeReportes)){
                    System.out.println("Ingrese el número de DNI: ");
                    String dni = scanner.nextLine();
                    imprimirReportePersonal(dni);
                }
            }

            if("2".equals(respuesta)){

                System.out.println("(1) Agregar inspección (2) Quitar Inspección (3) Modificar Inspección");
                String respuestaABM = scanner.nextLine();

                //Chequeo la validez del comando
                while(!"1".equals(respuestaABM) && !"2".equals(respuestaABM) && !"3".equals(respuestaABM)){
                    System.out.println("Comando inválido, ingrese el movimiento nuevamente: ");
                    respuesta = scanner.nextLine();
                }

                if("1".equals(respuestaABM)){
                    agregarInspeccion();
                }
                if("2".equals(respuestaABM)){
                    quitarInspeccion();
                }
                if("3".equals(respuestaABM)){
                    modificarInspeccion();
                }
            }

            if("3".equals(respuesta)){
                System.out.println("Hasta luego");
                break;
            }
        }


    }

    private static void agregarInspeccion() {
        System.out.println("Se agregó la inspección");
    }

    private static void modificarInspeccion() {
        System.out.println("Se modificó la inspección");
    }

    private static void quitarInspeccion() {
        System.out.println("Se borró la inspección");
    }


    private static void imprimirReporteDeAutomoviles() throws SQLException {
        System.out.println("Reporte de Automóviles");

        Statement stmtReporteAutos =  server.createStatement();

        String consultarVehiculos = "SELECT v.*, d.nombre FROM vehiculos v INNER JOIN dueños d on d.id = v.dueño;";
        ResultSet rs = stmtReporteAutos.executeQuery(consultarVehiculos);

        while(rs.next()){
            imprimirReporteDeAutomovilesPara(rs, "nombre");
        }
    }

    //Recibe un ResulSet con un vehiculo y su dueño e imprime por consola su correspondiente Reporte automotor
    private static void imprimirReporteDeAutomovilesPara(ResultSet rs, String duenio_nombre) throws SQLException {
        String dominio = rs.getString("dominio");
        String marca = rs.getString("marca");
        String modelo = rs.getString("modelo");
        String duenio = rs.getString(duenio_nombre);

        Inspeccion inspeccion = new Inspeccion(dominio, marca, modelo, duenio);
        inspeccion.imprimirReporteDeAutomovil();
    }

    private static void imprimirReporteDeInspecciones() {
        System.out.println("Reporte de inspecciones");
    }

    private static void imprimirReportePersonal(String dni) throws SQLException {
        Statement stmtReportePersonal = server.createStatement();

        String consultarVehiculos =
                "SELECT v.*, d.nombre AS titular, d.dni, d.exento, i.*, ins.nombre AS nombre_inspector, o.*, m.* " +
                "FROM vehiculos v "+
                    "INNER JOIN dueños d " +
                        "ON v.dueño = d.id " +
                    "INNER JOIN inspecciones i " +
                        "ON v.id = i.vehiculo " +
                    "INNER JOIN inspectores ins " +
                        "ON i.inspector = ins.id " +
                    "INNER JOIN observaciones o " +
                        "ON i.observacion = o.id " +
                    "INNER JOIN mediciones m " +
                        "ON i.medicion = m.id " +
                        "WHERE d.dni = " + dni + ";";

        ResultSet rs = stmtReportePersonal.executeQuery(consultarVehiculos);

        System.out.println("De que vehiculo desea observar el reporte?");
        System.out.println();

        List<String> patentes = new ArrayList<String>();

        while(rs.next()){
            String dominio = rs.getString("dominio");
            patentes.add(dominio);
            imprimirReporteDeAutomovilesPara(rs, "titular");
        }

        System.out.println("Indique el dominio del vehiculo: ");

        Scanner scanner = new Scanner(System.in);
        String respuesta = scanner.nextLine();

        while(!patentes.contains(respuesta)){

            System.out.println("Dominio inválido, ingréselo nuevamente: ");
            respuesta = scanner.nextLine();

        }

        rs = stmtReportePersonal.executeQuery(consultarVehiculos);

        while(rs.next()){
            if(respuesta.equals(rs.getString("dominio"))){

                String marca = rs.getString("marca");
                String modelo = rs.getString("modelo");
                String dominio = rs.getString("dominio");

                String titular = rs.getString("titular");
                int documento = rs.getInt("dni");
                boolean exento = rs.getBoolean("exento");

                Date fecha = rs.getDate("fecha");
                String estado = rs.getString("estado");
                String nombreInspector = rs.getString("nombre_inspector");


                Map<String, String> observaciones = new HashMap<String, String>();

                String luces = rs.getString("luces");
                String patente = rs.getString("patente");
                String espejos = rs.getString("espejos");
                String chasis = rs.getString("chasis");
                String vidrios = rs.getString("vidrios");
                String seguridadEmergencia = rs.getString("seguridad_y_emergencia");

                observaciones.put("luces", luces);
                observaciones.put("patente", patente);
                observaciones.put("espejos", espejos);
                observaciones.put("chasis", chasis);
                observaciones.put("vidrios", vidrios);
                observaciones.put("seguridadEmergencia", seguridadEmergencia);

                Map<String, String> mediciones = new HashMap<String, String>();

                String suspension = rs.getString("suspension");
                String direccionTrenDelantero = rs.getString("dirección_y_tren_delantero");
                String frenos = rs.getString("frenos");
                String contaminacion = rs.getString("contaminacion");

                mediciones.put("suspension", suspension);
                mediciones.put("direccionTrenDelantero", direccionTrenDelantero);
                mediciones.put("frenos", frenos);
                mediciones.put("contaminacion", contaminacion);

                Inspeccion inspeccion = new Inspeccion(marca, modelo, dominio, titular, documento, exento,
                                        fecha, estado, nombreInspector, observaciones, mediciones);

                inspeccion.imprimirReporteDetallado();

                break;

            }
        }


    }

}
