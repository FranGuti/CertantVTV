import java.sql.*;
import java.sql.Date;
import java.util.*;


public class Vtv {
    private static final Conexion con = new Conexion("certantvtv");
    private static Connection server;


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
            String respuesta = consultarInput(scanner, "1", "2", "3", "");

            if("1".equals(respuesta)){

                System.out.println("(1) Reporte de automóviles, (2) Reporte de inspecciones, (3) Reporte Personal");
                String respuestaDeReportes = consultarInput(scanner, "1", "2", "3", "");;

                if("1".equals(respuestaDeReportes)){

                    System.out.println("(1) Aptos, (2) Condicionales, (3) Rechazados, (4) Todos");

                    String respuestaDeAutomoviles = consultarInput(scanner, "1", "2", "3", "4");
                    imprimirReporteDeAutomoviles(respuestaDeAutomoviles);
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

                System.out.println("(1) Agregar (2) Quitar (3) Modificar");
                String respuestaABM = scanner.nextLine();

                //Chequeo la validez del comando
                while(!"1".equals(respuestaABM) && !"2".equals(respuestaABM) && !"3".equals(respuestaABM)){
                    System.out.println("Comando inválido, ingrese el movimiento nuevamente: ");
                    respuestaABM = scanner.nextLine();
                }

                if("1".equals(respuestaABM)){
                    System.out.println("(1) Agregar una Inspección (2) Agregar un Inspector");
                    String respuestaAlta = scanner.nextLine();

                    //Chequeo la validez del comando
                    while(!"1".equals(respuestaAlta) && !"2".equals(respuestaAlta)){
                        System.out.println("Comando inválido, ingrese el movimiento nuevamente: ");
                        respuestaAlta = scanner.nextLine();
                    }
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

    private static String consultarInput(Scanner scanner, String s1, String s2, String s3, String s4) {
        String respuesta = scanner.nextLine();
        while(!s1.equals(respuesta) && !s2.equals(respuesta) && !s3.equals(respuesta) &&!s4.equals(respuesta)){
            System.out.println("Comando inválido, ingrese el movimiento nuevamente: ");
            respuesta = scanner.nextLine();
        }
        return respuesta;
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


    private static void imprimirReporteDeAutomoviles(String pedido) throws SQLException {
        System.out.println("Reporte de Automóviles");

        Statement stmtReporteAutos =  server.createStatement();

        String consultarVehiculos = "SELECT v.*, d.nombre, MAX(i.fecha) AS fecha, i.estado " +
                "FROM vehiculos v " +
                "INNER JOIN dueños d " +
                    "ON d.id = v.dueño " +
                "INNER JOIN inspecciones i " +
                    "ON v.id = i.vehiculo " +
                "GROUP BY v.dominio";

        if("1".equals(pedido)){
            consultarVehiculos = consultarVehiculos + " HAVING i.estado = 'apto'";
        }

        if("2".equals(pedido)){
            consultarVehiculos = consultarVehiculos + " HAVING i.estado = 'condicional'";
        }

        if("3".equals(pedido)){
            consultarVehiculos = consultarVehiculos + " HAVING i.estado = 'rechazado'";
        }
        consultarVehiculos = consultarVehiculos + ";";
        // si es 4 (todos) no tengo que agregar el where


        ResultSet rs = stmtReporteAutos.executeQuery(consultarVehiculos);

        while(rs.next()){
            imprimirReporteDeAutomovilesPara(rs, "nombre", true);
        }
    }

    //Recibe un ResulSet, que contiene vehiculo y dueño, e imprime por consola los datos
    private static void imprimirReporteDeAutomovilesPara(ResultSet rs, String duenio_nombre,
                                                         Boolean conVencimiento) throws SQLException {
        String dominio = rs.getString("dominio");
        String marca = rs.getString("marca");
        String modelo = rs.getString("modelo");
        String duenio = rs.getString(duenio_nombre);
        Date fecha = rs.getDate("fecha");
        String estado = rs.getString("estado");
        Inspeccion inspeccion = new Inspeccion(dominio, marca, modelo, duenio, fecha, estado);

        if(conVencimiento){
            inspeccion.imprimirReporteSencilloConVencimiento();
        }
        else{
            inspeccion.imprimirReporteSencilloSinVencimiento();
        }

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
            if(!patentes.contains(dominio)){
                imprimirReporteDeAutomovilesPara(rs, "titular", false);
            }
            patentes.add(dominio);
        }

        System.out.println("Indique el dominio del vehiculo: ");

        Scanner scanner = new Scanner(System.in);
        String respuesta = scanner.nextLine();

        while(!patentes.contains(respuesta) && patentes.size()>0){

            System.out.println("Dominio inválido, ingréselo nuevamente: ");
            respuesta = scanner.nextLine();

        }

        rs = stmtReportePersonal.executeQuery(consultarVehiculos);

        /* Si quisiera solo la última inspección en el select pongo order by i.fecha desc
            Y dentro de este while un break, lo que haría es encontrar la primera vez q
            coincidan la patente con la que entró el user y solo levantaria ese reporte
            que es justamente el mas reciente.
         */
        while(rs.next()){
            if(respuesta.equals(rs.getString("dominio"))){

                Inspeccion inspeccion = crearInspeccion(rs);
                inspeccion.imprimirReporteDetallado();

            }
        }

    }

    private static Inspeccion crearInspeccion(ResultSet rs) throws SQLException {
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

        return new Inspeccion(marca, modelo, dominio, titular, documento, exento,
                fecha, estado, nombreInspector, observaciones, mediciones);
    }

}
