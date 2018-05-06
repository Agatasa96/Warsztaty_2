
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


import org.mindrot.jbcrypt.BCrypt;

public class User {
	private static final String INSERT_USER_STATEMENT = "INSERT INTO WARSZTATY2.users(username, email, password) VALUES (?, ?, ?)";
	private static final String UPDATE_USER_STATEMENT = "UPDATE WARSZTATY2.users SET username=?, email=?, password=? where id = ?";
	private static final String DELETE_USER_STATEMENT = "DELETE FROM WARSZTATY2.users WHERE id= ?";
	private static final String FIND_USER_BY_ID_QUERY = "SELECT * FROM WARSZTATY2.users where id=?";
	private static final String FIND_ALL_USERS = "SELECT * FROM WARSZTATY2.users";

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

	private void saveNewUser(Connection conn) throws SQLException {
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

	private void updateUser(Connection conn) throws SQLException {

		PreparedStatement preparedStatement = conn.prepareStatement(UPDATE_USER_STATEMENT);
		preparedStatement.setString(1, this.username);
		preparedStatement.setString(2, this.email);
		preparedStatement.setString(3, this.password);
		preparedStatement.setLong(4, this.id);
		preparedStatement.executeUpdate();

	}

	public void delete(Connection conn) throws SQLException {

		if (this.id != 0) {
			PreparedStatement preparedStatement = conn.prepareStatement(DELETE_USER_STATEMENT);
			preparedStatement.setLong(1, this.id);
			preparedStatement.executeUpdate();
			this.id = 0;

		}

	}

	public void setPassword(String password) {
		this.password = BCrypt.hashpw(password, BCrypt.gensalt());
	}

	
	public static User loadUserById(Connection conn, long id) throws SQLException {

		PreparedStatement preparedStatement = conn.prepareStatement(FIND_USER_BY_ID_QUERY);
		preparedStatement.setLong(1, id);
		ResultSet resultSet = preparedStatement.executeQuery();

		if (resultSet.next()) {
			return createUser(resultSet);
		}
		return null;

	}
	
	public static User createUser(ResultSet resultSet) throws SQLException {

		String username = resultSet.getString(USERNAME_COLUMN_NAME);
		String password = resultSet.getString(PASSWORD_COLUMN_NAME);
		String email = resultSet.getString(EMAIL_COLUMN_NAME);
		User loadedUser = new User(username, email, password);
		loadedUser.id = resultSet.getLong(ID_COLUMN_NAME);

		return loadedUser;

	}


	public static User[] loadAllUsers(Connection conn) throws SQLException {

		ArrayList<User> users = new ArrayList<>();

		PreparedStatement preparedStatement = conn.prepareStatement(FIND_ALL_USERS);
		ResultSet resultSet = preparedStatement.executeQuery();

		while (resultSet.next()) {
			users.add(createUser(resultSet));
		}

		User[] uArray = new User[users.size()];
		return users.toArray(uArray);
	}

	@Override
	public String toString() {
		return "User: id = " + id + ", username = " + username + ", email = " + email + ", password = " + password
				+ ", userGroupId = " + userGroupId + "/n";
	}

	public static void main(String[] args) throws SQLException {

		try (Connection conn = DBUtils.createConnection()) {
			// dodwanie nowego Usera
			// User u1 = new User("username1", "email1@wp.pl", "123");
			// u1.saveNewUser(conn);
			// User[] users = User.loadAllUsers(conn);
			// System.out.println(Arrays.toString(users));
		}
	}

}
