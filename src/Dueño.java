import exceptions.DueñoNoEnDBException;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public abstract class Dueño {
    protected String nombre;
    protected int dni;

    public abstract void imprimirResumen();

    public String getID(Connection con) throws SQLException {
        Statement stmtDuenioId = con.createStatement();
        String consultaIDDuenio = "SELECT d.id FROM dueños d WHERE d.dni = " + this.dni + ";";
        ResultSet rs = stmtDuenioId.executeQuery(consultaIDDuenio);
        if(!rs.next()){
            throw new DueñoNoEnDBException("El dueño solicitado no se encuentra en la base de datos");
        }
        int id = rs.getInt("id");
        return Integer.toString(id);
    }

    public abstract void agregarADataBase(Connection con) throws SQLException;

    public void imprimirNombre() {
        System.out.println(this.nombre);
    }

    public abstract String getTipo();

    public String getNombre() {
        return this.nombre;
    }

    public int getDNI() {
        return this.dni;
    }
}
