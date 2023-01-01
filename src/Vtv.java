import exceptions.InspectorEnDBException;

import java.sql.*;
import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


public class Vtv {
    private static final Conexion con = new Conexion("certantvtv");
    private static Connection server;


    public static void main(String[] args) throws SQLException, ParseException {
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
                String respuestaDeReportes = consultarInput(scanner, "1", "2", "3", "");

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
                String respuestaABM = consultarInput(scanner, "1", "2", "3", "");

                if("1".equals(respuestaABM)){
                    System.out.println("(1) Agregar una Inspección (2) Agregar un Inspector");
                    String respuestaAlta = consultarInput(scanner, "1", "2", "", "");

                    if("1".equals(respuestaAlta)){
                        Inspeccion inspeccion = pedirInspeccion(scanner);
                        if(inspeccion != null) {
                            inspeccion.agregarADataBase(server);
                        }
                    }
                    if("2".equals(respuestaAlta)){
                        agregarInspector(scanner);
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

    private static Inspeccion pedirInspeccion(Scanner scanner) throws SQLException, ParseException {
        System.out.println("Ingrese la fecha de inspección en formato YYYY-MM-DD: ");
        Date fecha = pedirFecha(scanner);

        System.out.println("Marca del vehículo:");
        String marca = scanner.nextLine();
        System.out.println();

        System.out.println("Modelo del vehículo:");
        String modelo = scanner.nextLine();
        System.out.println();

        System.out.println("Dominio:");
        String dominio = scanner.nextLine();
        System.out.println();

        System.out.println("Nombre del propietario: ");
        String titular = scanner.nextLine();
        System.out.println();

        System.out.println("DNI:");
        int dni = Integer.parseInt(scanner.nextLine());
        System.out.println();

        System.out.println("(1) Dueño común  (2) Dueño exento");
        String respuestaTipo = consultarInput(scanner, "1", "2", "", "");
        boolean exento = !"1".equals(respuestaTipo);
        System.out.println();

        System.out.println("Ingrese número de matricula del inspector a cargo:");
        String nombreInspector = consultarInspector(scanner);
        if(nombreInspector.isEmpty()){
            System.out.println("Parece que el inspector no se encuentra registrado");
            System.out.println("Desea: (1) Registrar nuevo inspector  (2) Salir");
            String respuesta = consultarInput(scanner, "1", "2", "", "");
            if("1".equals(respuesta)){
                nombreInspector = agregarInspector(scanner);
                System.out.println("Continuemos con los datos de inspección");
            }
            if("2".equals(respuesta)){
                System.out.println();
                return null;
            }
        }
        System.out.println();

        String estado = "apto";

        System.out.println("Observaciones:");
        Map<String, String> observaciones = new HashMap<>();
        observaciones.put("luces" , "");
        observaciones.put("patente", "");
        observaciones.put("espejos", "");
        observaciones.put("chasis", "");
        observaciones.put("vidrios", "");
        observaciones.put("seguridad y emergencia", "");

        String estadoObservacion = pedirYVerificarEstados(observaciones, scanner);
        System.out.println();

        System.out.println("Mediciones:");
        Map<String, String> mediciones = new HashMap<>();
        mediciones.put("suspension" , "");
        mediciones.put("dirección y tren delantero", "");
        mediciones.put("frenos", "");
        mediciones.put("contaminacion", "");

        String estadoMedicion = pedirYVerificarEstados(mediciones, scanner);
        System.out.println();

        if("apto".equals(estadoObservacion) && "condicional".equals(estadoMedicion) ||
        "condicional".equals(estadoObservacion) && "apto".equals(estadoMedicion) ||
        "condicional".equals(estadoObservacion) && "condicional".equals(estadoMedicion)){
            estado = "condicional";
        }

        if("rechazado".equals(estadoMedicion) || "rechazado".equals(estadoObservacion)){
            estado = "rechazado";
        }

        return new Inspeccion(marca, modelo, dominio, titular, dni,
        exento, fecha, estado, nombreInspector, observaciones, mediciones);

    }

    private static String pedirYVerificarEstados(Map<String, String> map, Scanner scanner){
        String estado = "apto";
        for(Map.Entry<String,String> entry : map.entrySet()){
            System.out.printf("El resultado de %s fué (1) Apto  (2) Condicional (3) Rechazado%n",
                    entry.getKey());
            String respuesta = consultarInput(scanner, "1", "2", "3", "");
            String resultado = parseRespuestaEstado(respuesta);
            map.replace(entry.getKey(), resultado);
            if("condicional".equals(resultado) && "apto".equals(estado)){
                estado = "condicional";
            }
            if("rechazado".equals(resultado)){
                estado = "rechazado";
            }
        }
        return estado;
    }

    private static String parseRespuestaEstado(String respuesta) {
        if("1".equals(respuesta)){
            return "apto";
        }
        if("2".equals(respuesta)){
            return "condicional";
        }
        return "rechazado";
    }

    private static String agregarInspector(Scanner scanner) throws SQLException {
        System.out.println("Ingrese el nombre del inspector: ");
        String inspector = scanner.nextLine();
        if(!inspectorEnDataBase(inspector)){
            Statement stmtAgregarInspector = server.createStatement();
            String consultaAgregarInspector = "INSERT INTO inspectores(nombre) VALUES ('" + inspector + "');";
            stmtAgregarInspector.executeUpdate(consultaAgregarInspector);
        }
        return inspector;
    }

    private static boolean inspectorEnDataBase(String inspector) throws SQLException {
        Statement stmtInspectorNuevo = server.createStatement();
        String consultaEstaInspector = "SELECT * FROM inspectores i WHERE i.nombre = '" + inspector + "';";
        ResultSet rs = stmtInspectorNuevo.executeQuery(consultaEstaInspector);
        return rs.next();
    }


    private static String consultarEstado(Scanner scanner) {
        String input = consultarInput(scanner, "1", "2", "3", "");
        if("1".equals(input)){
            return "apto";
        }
        if("2".equals(input)){
            return "condicional";
        }
        else{
            return "rechazado";
        }
    }

    private static Date pedirFecha(Scanner scanner) throws ParseException {
        String input = scanner.nextLine();

        while(!esFechaValida(input)){
            System.out.println("Formato de fecha inválido, ingresela nuevamente de la forma YYYY-MM-DD");
            input = scanner.nextLine();
        }

        java.util.Date dia = new SimpleDateFormat("yyyy-MM-dd").parse(input);

        return new Date(dia.getTime());
    }

    private static boolean esFechaValida(String fecha) {
        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setLenient(false);
        try {
            sdf.parse(fecha);
        } catch (ParseException e) {
            return false;
        }
        return true;
    }

    private static String consultarInspector(Scanner scanner) throws SQLException {
        int respuesta = Integer.parseInt(scanner.nextLine());

        Statement stmtInspector =  server.createStatement();
        String consultarInspectores = "SELECT * FROM inspectores i WHERE i.id = " + respuesta + ";";
        ResultSet rs = stmtInspector.executeQuery(consultarInspectores);

        if(!rs.next()){
            return "";
        }

        return rs.getString("nombre");

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
