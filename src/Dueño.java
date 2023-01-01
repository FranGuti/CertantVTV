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
        Statement stmtDueñoId = con.createStatement();
        String consultaIDDueño = "SELECT d.id FROM dueños d WHERE d.dni = " + this.dni + ";";
        ResultSet rs = stmtDueñoId.executeQuery(consultaIDDueño);
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
}
