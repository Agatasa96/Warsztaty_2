

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.mindrot.jbcrypt.BCrypt;

public class User {
	private static final String INSERT_USER_STATEMENT = "INSERT INTO WARSZTATY2.users(username, email, password) VALUES (?, ?, ?)";

	private static final String PASSWORD_COLUMN_NAME = "password";
	private static final String ID_COLUMN_NAME = "ID";
	private static final String USERNAME_COLUMN_NAME = "username";
	private static final String EMAIL_COLUMN_NAME = "email";

	private long id;
	private String username;
	private String email;
	private String password;
	private int userGroupId;

	public User(String username, String email, String password) {
		this.username = username;
		this.email = email;
		setPassword(password);

	}


	public void save(Connection conn) throws SQLException {

		if (this.id == 0) {
			saveNewUser(conn);

		} else {
			updateUser(conn);
		}

	}

	public void saveNewUser(Connection conn) throws SQLException {
			String generatedColumns[] = { ID_COLUMN_NAME };
			PreparedStatement preparedStatement;
			preparedStatement = conn.prepareStatement(INSERT_USER_STATEMENT, generatedColumns);
			preparedStatement.setString(1, this.username);
			preparedStatement.setString(2, this.email);
			preparedStatement.setString(3, this.password);
			preparedStatement.executeUpdate();
			ResultSet rs = preparedStatement.getGeneratedKeys(); // zwraca
																	// wygenerowane
																	// klucze
			if (rs.next()) {
				this.id = rs.getInt(1); // pobieramy indeks wygenerowany auto w
										// DB
			}
		}
	

	public void setPassword(String password) {
		this.password = BCrypt.hashpw(password, BCrypt.gensalt());
	}

	public static void main(String[] args) throws SQLException {

		try (Connection conn = DBUtils.createConnection()) {
			// dodwanie nowego Usera
			//User u1 = new User("username1", "email1@wp.pl", "123");
			 //u1.saveNewUser(conn);
		}
	}

}
