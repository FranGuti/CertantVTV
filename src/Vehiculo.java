import exceptions.DueñoNoEnDBException;
import exceptions.VehiculoNoEnDBException;

import java.sql.*;

public class Vehiculo {
    EstadoVTV estado;
    String marca;
    String modelo;
    String dominio;
    Dueño duenio;
    Date fecha;

    public Vehiculo(String dominio, String marca, String modelo, Dueño duenio) {
        this.dominio = dominio;
        this.marca = marca;
        this. modelo = modelo;
        this.duenio = duenio;
    }


    public void imprimirReporteSencillo() {
        System.out.print("DOMINIO: "+dominio + "  |  MARCA: " + marca + "  | MODELO: " + modelo +
                "  | PROPIETARIO: ");
        this.duenio.imprimirNombre();
    }

    public void imprimirResumen() {
        System.out.printf("marca '%s', modelo '%s', patente '%s'%n", marca, modelo, dominio);
    }

    public void agregarADB(Connection con) throws SQLException {
        Statement stmtAgregarVehiculo = con.createStatement();
        try {
            String consultaAgregar = "INSERT INTO vehiculos (marca, modelo, dominio, dueño) " +
                    "VALUES ('" + this.marca + "', '" + this.modelo + "', '" + this.dominio +
                    "', " + this.duenio.getID(con) + ");";
            stmtAgregarVehiculo.executeUpdate(consultaAgregar);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (DueñoNoEnDBException e){
            duenio.agregarADataBase(con);
            agregarADB(con);
        }

    }

    public String getID(Connection con) throws SQLException, VehiculoNoEnDBException {
        Statement stmtVehiculoID = con.createStatement();
        String consultaVehiculoID = "SELECT v.id FROM vehiculos v WHERE v.dominio = '" + dominio + "';";
        ResultSet rs = stmtVehiculoID.executeQuery(consultaVehiculoID);

        if(!rs.next()){
            throw new VehiculoNoEnDBException("El vehiculo solicitado no está en la Base de Datos");
        }

        int id = rs.getInt("id");
        return Integer.toString(id);
    }
}
