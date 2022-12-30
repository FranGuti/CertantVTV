import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class Conexion {
    String bd = "";
    String url = "jdbc:mysql://localhost:3306/";
    String user = "root";
    String pass = "mailen_123";
    String driver = "com.mysql.cj.jdbc.Driver";
    Connection con;

    public Conexion(String bd){
        this.bd = bd;
    }

    public Connection conectar(){
        try {
            Class.forName(driver);
            con = DriverManager.getConnection(url+bd, user, pass);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return con;
    }
}
