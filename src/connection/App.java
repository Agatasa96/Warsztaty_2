package connection;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.SQLException;

public class App {

	private static final String CREATE_DATABSE = "CREATE DATABASE WARSZTATY2";
	private static final String CREATE_TABLE_USER_GROUPS_STATEMENT = "CREATE TABLE WARSZTATY2.user_groups("
			+ "id INT AUTO_INCREMENT NOT NULL, " + "name VARCHAR(255) NOT NULL, " + "PRIMARY KEY(id))";
	private static final String CREATE_TABLE_EXERCISES_STATEMENT = "CREATE TABLE WARSZTATY2.exercises("
			+ "id INT AUTO_INCREMENT NOT NULL, " + "title VARCHAR(255), " + "description TEXT, " + "PRIMARY KEY(id))";
	private static final String CREATE_TABLE_SOLUTIONS_STATEMENT = "CREATE TABLE WARSZTATY2.solutions("
			+ "id INT AUTO_INCREMENT, " + "created DATETIME, " + "updated DATETIME, " + "description VARCHAR(255), "
			+ "exercise_id INT, " + "users_id BIGINT, " + "PRIMARY KEY (id), "
			+ "FOREIGN KEY (users_id) REFERENCES WARSZTATY2.users(id),"
			+ "FOREIGN KEY (exercise_id) REFERENCES WARSZTATY2.exercises(id))";
	private static final String CREATE_TABLE_USERS_STATEMENT = "CREATE TABLE WARSZTATY2.users("
			+ "id BIGINT(20) NOT NULL AUTO_INCREMENT, " + "username VARCHAR(255) NOT NULL, "
			+ "email VARCHAR(255) NOT NULL UNIQUE , " + "password VARCHAR(245) NOT NULL, "
			+ "user_group_id INT(11) NOT NULL, " + "PRIMARY KEY (id), "
			+ "FOREIGN KEY (user_group_id) REFERENCES WARSZTATY2.user_groups(id))";

	public static void main(String[] args) throws SQLException {

		try (Connection conn = DBUtils.createConnection(); Statement st = conn.createStatement()) {

			
					st.execute(CREATE_DATABSE);
					st.execute(CREATE_TABLE_USER_GROUPS_STATEMENT);
					st.execute(CREATE_TABLE_EXERCISES_STATEMENT);
					st.execute(CREATE_TABLE_USERS_STATEMENT);
					st.execute(CREATE_TABLE_SOLUTIONS_STATEMENT);
					
		
		}
	}
}
