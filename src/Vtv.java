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

                System.out.println("(1) Reporte de automóviles, (2) Reporte Personal");
                String respuestaDeReportes = consultarInput(scanner, "1", "2", "", "");

                if("1".equals(respuestaDeReportes)){

                    System.out.println("(1) Aptos, (2) Condicionales, (3) Rechazados, (4) Todos");

                    String respuestaDeAutomoviles = consultarInput(scanner, "1", "2", "3", "4");
                    imprimirReporteDeAutomoviles(respuestaDeAutomoviles);
                }

                if("2".equals(respuestaDeReportes)){
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
                    System.out.println("(1) Modificar datos de Vehiculo (2) Modificar datos de Inspector " +
                            "(3) Modificar datos de inspección");
                    String respuestaModificacion = consultarInput(scanner, "1", "2", "3", "");

                    if("1".equals(respuestaModificacion)){
                        System.out.println("Ingrese el dominio del Vehiculo que desea modificar: ");
                        String dominio = pedirDominio(scanner);
                        System.out.println();
                        if(!dominio.isEmpty()) {
                            modificarVehiculo(scanner, dominio);
                        }
                    }

                    if("2".equals(respuestaModificacion)){
                        System.out.println("Ingrese el nombre del inspector que desea modificar: ");
                        String nombre = scanner.nextLine();
                        if(!nombre.isEmpty()){
                            modificarInspector(scanner, nombre);
                        }

                    }

                    if("3".equals(respuestaModificacion)){
                        System.out.println("Ingrese el número de inspección que desea modificar: ");
                        int numero = Integer.parseInt(scanner.nextLine()); //explota si no me pasan un entero
                        modificarInspeccion(scanner, numero);
                    }

                }
            }

            if("3".equals(respuesta)){
                System.out.println("Hasta luego");
                break;
            }
        }


    }

    private static void modificarInspector(Scanner scanner, String nombre) throws SQLException {
        System.out.println("Escriba el nuevo nombre del inspector: ");
        String nuevoNombre = scanner.nextLine();
        while(inspectorEnDataBase(nuevoNombre)){
            System.out.println("Ese inspector ya se encuentra en el sistema");
            System.out.println("Desea (1) Intentar con otro (2) Volver");
            String respuesta = consultarInput(scanner, "1", "2", "", "");
            if("2".equals(respuesta)){
                return;
            }
            System.out.println("Ingrese el nuevo nombre: ");
            nuevoNombre = scanner.nextLine();
        }
        Statement stmtUpdateInspector = server.createStatement();
        String consultaUpdateInspector = "UPDATE inspectores SET nombre = '" + nuevoNombre + "' WHERE nombre = '" +
                nombre + "';";
        stmtUpdateInspector.executeUpdate(consultaUpdateInspector);

    }

    private static String pedirDominio(Scanner scanner) throws SQLException {
        String dominio = scanner.nextLine();

        Statement stmtPedirDominio = server.createStatement();
        String consultaDominios = "SELECT v.dominio FROM vehiculos v WHERE v.dominio = '" + dominio + "';";
        ResultSet rs = stmtPedirDominio.executeQuery(consultaDominios);

        while(!rs.next()){
            System.out.println("No se encontró el dominio en el sistema");
            System.out.println("Desea (1) Ingresar otro (2) Volver");
            String resultado = consultarInput(scanner, "1", "2", "", "");
            if("2".equals(resultado)){
                return "";
            }
            System.out.println("Ingrese el dominio: ");
            dominio = scanner.nextLine();
            consultaDominios = "SELECT v.dominio FROM vehiculos v WHERE v.dominio = '" + dominio + "';";
            rs = stmtPedirDominio.executeQuery(consultaDominios);
        }
        return rs.getString("dominio");
    }

    private static void modificarVehiculo(Scanner scanner, String dominio) throws SQLException {
        Vehiculo vehiculoAModificar = new Vehiculo(dominio);

        System.out.println("En caso de no querer modificar el campo dejar vacío");
        System.out.println("Ingrese la nueva marca: ");
        String marca = scanner.nextLine();
        if(!marca.isEmpty()){
            vehiculoAModificar.cambiarMarca(marca);
        }
        System.out.println();

        System.out.println("Ingrese el nuevo modelo: ");
        String modelo = scanner.nextLine();
        if(!modelo.isEmpty()){
            vehiculoAModificar.cambiarModelo(modelo);
        }System.out.println();

        System.out.println("Ingrese nuevo dominio: ");
        String inputDominio = pedirDominioNoUsado(scanner);
        if(!inputDominio.isEmpty()){
            vehiculoAModificar.cambiarDominio(inputDominio);
        }System.out.println();

        Dueño nuevoDuenio = pedirDuenio(scanner);
        if(nuevoDuenio != null){
            vehiculoAModificar.cambiarDuenio(nuevoDuenio);
        }

        vehiculoAModificar.actualizar(server, dominio);
    }

    private static String pedirDominioNoUsado(Scanner scanner) throws SQLException {
        String dominio = scanner.nextLine();
        Statement stmtDominioNoUsado = server.createStatement();
        String consultaDominio = "SELECT dominio FROM vehiculos WHERE dominio = '" + dominio + "';";
        ResultSet rs = stmtDominioNoUsado.executeQuery(consultaDominio);
        while (rs.next()){
            System.out.println("Ya hay un vehiculo registrado bajo ese dominio");
            System.out.println("Quiere (1) Ingresar otro (2) Saltar");
            String respuesta = consultarInput(scanner, "1", "2", "", "");
            if("2".equals(respuesta)){
                return "";
            }
            System.out.println("Ingrese el dominio:");
            String nuevoDominio = scanner.nextLine();
            consultaDominio = "SELECT dominio FROM vehiculos WHERE dominio = '" + nuevoDominio + "';";
            rs = stmtDominioNoUsado.executeQuery(consultaDominio);
        }
        return dominio;
    }

    private static Dueño pedirDuenio(Scanner scanner) throws SQLException {
        System.out.println("Ingrese el nuevo duenio:");
        String duenio = scanner.nextLine();


        if(!duenio.isEmpty()){
            System.out.println("Ingrese el DNI del nuevo dueño:");
            String dni = scanner.nextLine();

            Dueño nuevoDuenio = buscarEnDB(dni);

            if(nuevoDuenio != null){
                System.out.println("Ese titular está en nuestros sistemas, será asignado");
                return nuevoDuenio;
            }

            System.out.println("Es (1) Dueño Común (2) Dueño Exento:");
            String exento = consultarInput(scanner, "1", "2", "", "");
            if("1".equals(exento)){
                nuevoDuenio = new DueñoComun(duenio, Integer.parseInt(dni));
            }
            else{
                nuevoDuenio = new DueñoExento(duenio, Integer.parseInt(dni));
            }
            return nuevoDuenio;
        }

        return null;
    }

    private static Dueño buscarEnDB(String dni) throws SQLException {
        Statement stmtBuscarDuenio = server.createStatement();
        String consultaDuenio = "SELECT * FROM dueños WHERE dni = " + dni + ";";
        ResultSet rs = stmtBuscarDuenio.executeQuery(consultaDuenio);
        if(rs.next()){
            if(rs.getBoolean("exento")){
                return new DueñoExento(rs.getString("nombre"), Integer.parseInt(dni));
            }
            return new DueñoComun(rs.getString("nombre"), Integer.parseInt(dni));
        }
        return null;
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

        Map<String, String> observaciones = new HashMap<>();
        String estadoObservacion = pedirObservaciones(scanner, observaciones);

        Map<String, String> mediciones = new HashMap<>();
        String estadoMedicion = pedirMediciones(scanner, mediciones);

        String estado = concluirEstado(estadoObservacion, estadoMedicion);

        return new Inspeccion(marca, modelo, dominio, titular, dni,
        exento, fecha, estado, nombreInspector, observaciones, mediciones);

    }

    private static String concluirEstado(String estadoObservacion, String estadoMedicion) {
        if("apto".equals(estadoObservacion) && "condicional".equals(estadoMedicion) ||
                "condicional".equals(estadoObservacion) && "apto".equals(estadoMedicion) ||
                "condicional".equals(estadoObservacion) && "condicional".equals(estadoMedicion)){
            return "condicional";
        }

        if("rechazado".equals(estadoMedicion) || "rechazado".equals(estadoObservacion)){
            return "rechazado";
        }
        return "apto";
    }

    private static String pedirMediciones(Scanner scanner, Map<String, String> mediciones) {
        System.out.println("Mediciones:");
        mediciones.put("suspension" , "");
        mediciones.put("dirección y tren delantero", "");
        mediciones.put("frenos", "");
        mediciones.put("contaminacion", "");
        System.out.println();
        return pedirYVerificarEstados(mediciones, scanner);
    }

    private static String pedirObservaciones(Scanner scanner, Map<String, String> observaciones) {
        System.out.println("Observaciones:");
        observaciones.put("luces" , "");
        observaciones.put("patente", "");
        observaciones.put("espejos", "");
        observaciones.put("chasis", "");
        observaciones.put("vidrios", "");
        observaciones.put("seguridad y emergencia", "");
        System.out.println();
        return pedirYVerificarEstados(observaciones, scanner);
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


    private static void modificarInspeccion(Scanner scanner, int numero) throws SQLException, ParseException {
        Statement stmtModificarInspeccion = server.createStatement();
        String consultarInspeccion = "SELECT * FROM inspecciones WHERE id = " + numero + ";";
        ResultSet rs = stmtModificarInspeccion.executeQuery(consultarInspeccion);
        if(!rs.next()){
            System.out.println("No se encontró la inspección");
            return;
        }

        Inspeccion nuevaInspeccion = pedirinspeccionParaEditar(scanner);
        nuevaInspeccion.actualizar(server, numero);

    }

    private static Inspeccion pedirinspeccionParaEditar(Scanner scanner) throws SQLException {
        System.out.println("Si desea no modificar algún campo simplemente dejelo vacío");
        System.out.println("Ingrese el dominio del nuevo vehículo: ");
        String dominio = pedirDominio(scanner);
        Vehiculo vehiculo = null;
        if(!dominio.isEmpty()){
            vehiculo = new Vehiculo(dominio);
        }

        Map<String, String> observaciones = new HashMap<>();
        Map<String, String> mediciones = new HashMap<>();
        String estadoObservacion;
        String estadoMedicion;
        String estado = null;

        System.out.println("Ingrese una tecla cualquiera para modificar las observaciones y mediciones: ");
        String cambio = scanner.nextLine();

        if(!cambio.isEmpty()){
             estadoObservacion = pedirObservaciones(scanner, observaciones);
            estadoMedicion = pedirMediciones(scanner, mediciones);
            estado = concluirEstado(estadoObservacion, estadoMedicion);
        }

        System.out.println("Ingrese el nuevo inspector: ");
        String nombreInspector = scanner.nextLine();
        boolean salir = false;
        if(!nombreInspector.isEmpty()){
            while(!estaEnDB(nombreInspector)){
                System.out.println("El inspector no se encuentra en el sistema");
                System.out.println("Desea (1) Ingresar otro (2) Volver");
                String respuesta = consultarInput(scanner, "1", "2", "", "");
                if("2".equals(respuesta)){
                    salir = true;
                    break;
                }
                nombreInspector = scanner.nextLine();
            }
            if(salir){
               nombreInspector = "";
            }
        }
        return new Inspeccion(vehiculo, estado, observaciones, mediciones, nombreInspector);

    }

    private static boolean estaEnDB(String nombreInspector) throws SQLException {
        Statement stmtBuscarInspector = server.createStatement();
        String consultaInspector = "SELECT * FROM inspectores WHERE nombre = '" + nombreInspector + "';";
        ResultSet rs = stmtBuscarInspector.executeQuery(consultaInspector);
        return rs.next();
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
