package connection;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBUtils {

	public static Connection createConnection() throws SQLException {
		
		return DriverManager.getConnection("jdbc:mysql://localhost:3306/?useSSL=false", "root", "coderslab");
	}

}
