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
    public Vehiculo(String dominio){
        this.dominio = dominio;
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

    public void cambiarMarca(String marca) {
        this.marca = marca;
    }

    public void cambiarModelo(String modelo) {
        this.modelo = modelo;
    }

    public void cambiarDominio(String inputDominio) {
        this.dominio = inputDominio;
    }

    public void cambiarDuenio(Dueño nuevoDuenio) {
        this.duenio = nuevoDuenio;
    }

    public void actualizar(Connection server, String dominioABuscar) throws SQLException {
        Statement stmtActualizar = server.createStatement();
        String consultarId = "SELECT * FROM vehiculos WHERE dominio = '" + dominioABuscar + "';";
        ResultSet rs = stmtActualizar.executeQuery(consultarId);

        if(!rs.next()){
            throw new VehiculoNoEnDBException("No se encontró el vehiculo en la base de datos");
        }

        String id = Integer.toString(rs.getInt("id"));
        String marca = rs.getString("marca");
        String modelo = rs.getString("modelo");

        try {
            if(this.marca == null){
                this.marca = marca;
            }
            if(this.modelo == null){
                this.modelo = modelo;
            }

            String consultaActualizar = "UPDATE vehiculos SET dominio = '" + this.dominio +
                    "', marca = '" + this.marca + "', modelo = '" + this.modelo + "'";

            if(this.duenio != null){
                consultaActualizar = consultaActualizar + ", dueño = " + this.duenio.getID(server);
            }

            consultaActualizar = consultaActualizar + " WHERE id = " + id + ";";

            stmtActualizar.executeUpdate(consultaActualizar);
        } catch (DueñoNoEnDBException e){
            this.duenio.agregarADataBase(server);
            actualizar(server, dominioABuscar);
        }
    }

    public String getMarca(){
        return this.marca;
    }

    public String getEstado() {
        return this.estado.getEstado();
    }

    public String getModelo() {
        return this.modelo;
    }

    public String getDominio() {
        return this.dominio;
    }

    public String getTipoDuenio() {
        return this.duenio.getTipo();
    }
}
