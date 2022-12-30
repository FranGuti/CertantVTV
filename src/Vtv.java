import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


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

            while(!respuesta.equals("1") && !respuesta.equals("2") && !respuesta.equals("3")){         // Si es inválido el comando
                System.out.println("Comando inválido, ingrese el movimiento nuevamente: ");
                respuesta = scanner.nextLine();
            }

            if(respuesta.equals("1")){

                System.out.println("(1) Reporte de automóviles, (2) Reporte de inspecciones, (3) Reporte Personal");
                String respuestaDeReportes = scanner.nextLine();

                //Chequeo la validez del comando
                while(!respuestaDeReportes.equals("1") && !respuestaDeReportes.equals("2") && !respuestaDeReportes.equals("3")){
                    System.out.println("Comando inválido, ingrese el movimiento nuevamente: ");
                    respuestaDeReportes = scanner.nextLine();
                }

                if(respuestaDeReportes.equals("1")){
                    imprimirReporteDeAutomoviles();
                }
                if(respuestaDeReportes.equals("2")){
                    imprimirReporteDeInspecciones();
                }
                if(respuestaDeReportes.equals("3")){
                    System.out.println("Ingrese el número de DNI: ");
                    String dni = scanner.nextLine();
                    imprimirReportePersonal(dni);
                }
            }

            if(respuesta.equals("2")){

                System.out.println("(1) Agregar inspección (2) Quitar Inspección (3) Modificar Inspección");
                String respuestaABM = scanner.nextLine();

                //Chequeo la validez del comando
                while(!respuestaABM.equals("1") && !respuestaABM.equals("2") && !respuestaABM.equals("3")){
                    System.out.println("Comando inválido, ingrese el movimiento nuevamente: ");
                    respuesta = scanner.nextLine();
                }

                if(respuestaABM.equals("1")){
                    agregarInspeccion();
                }
                if(respuestaABM.equals("2")){
                    quitarInspeccion();
                }
                if(respuestaABM.equals("3")){
                    modificarInspeccion();
                }
            }

            if(respuesta.equals("3")){
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

            String dominio = rs.getString("dominio");
            String marca = rs.getString("marca");
            String modelo = rs.getString("modelo");
            String duenio = rs.getString("nombre");


            System.out.println("DOMINIO: "+dominio + "  |  MARCA: " + marca + "  | MODELO: " + modelo + "  | PROPIETARIO: " + duenio);
        }
    }

    private static void imprimirReporteDeInspecciones() {
        System.out.println("Reporte de inspecciones");
    }

    private static void imprimirReportePersonal(String dni) throws SQLException {
        Statement stmtReportePersonal = server.createStatement();

        String consultarVehiculos =
                "SELECT * " +
                "FROM vehiculos v " +
                    "INNER JOIN dueños d " +
                        "on d.id = v.dueño " +
                    "INNER JOIN inspecciones i " +
                        "ON v.id = i.vehiculo " +
                    "INNER JOIN inspectores ins " +
                        "ON i.inspector = ins.id " +
                         "WHERE " + dni + ";";

        ResultSet rs = stmtReportePersonal.executeQuery(consultarVehiculos);
        List<Vehiculo> vehiculos = new ArrayList<Vehiculo>();

        while(rs.next()){

        }

    }

}
