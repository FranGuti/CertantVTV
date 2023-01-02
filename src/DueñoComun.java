import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DueñoComun extends Dueño{
    public DueñoComun(String titular, int documento) {
        nombre = titular;
        dni = documento;
    }

    @Override
    public void imprimirResumen() {
        System.out.printf("%s, dni %d, no se encuentra exento de pago%n", nombre, dni);
    }

    @Override
    public void agregarADataBase(Connection con) throws SQLException {
        Statement stmtAgregarExento = con.createStatement();
        String consultaAgregarExento = "INSERT INTO dueños(nombre, dni, exento) " +
                "VALUES ('" + this.nombre + "', " + this.dni + ", false);";
        stmtAgregarExento.executeUpdate(consultaAgregarExento);
    }

    @Override
    public String getTipo() {
        return "Dueño común";
    }
}
