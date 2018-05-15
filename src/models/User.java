package models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.mindrot.jbcrypt.BCrypt;

public class User {
	private static final String INSERT_USER_STATEMENT = "INSERT INTO WARSZTATY2.users(username, email, password, user_group_id) VALUES (?, ?, ?, ?)";
	private static final String UPDATE_USER_STATEMENT = "UPDATE WARSZTATY2.users SET username=?, email=?, password=?, user_group_id=? where id = ?";
	private static final String DELETE_USER_STATEMENT = "DELETE FROM WARSZTATY2.users WHERE id= ?";
	private static final String FIND_USER_BY_ID_QUERY = "SELECT * FROM WARSZTATY2.users where id=?";
	private static final String FIND_ALL_USERS = "SELECT * FROM WARSZTATY2.users";
	private static final String FIND_USER_BY_GROUP_ID = "SELECT * FROM WARSZTATY2.users where warsztaty2.users.user_group_id =?";

	private static final String PASSWORD_COLUMN_NAME = "password";
	private static final String ID_COLUMN_NAME = "ID";
	private static final String USERNAME_COLUMN_NAME = "username";
	private static final String EMAIL_COLUMN_NAME = "email";
	private static final String USER_GROUP_COLUMN_NAME = "user_group_id";

	private long id;
	private String username;
	private String email;
	private String password;
	private int userGroupId;

	public User(String username, String email, String password, int userGroupId) {
		this.username = username;
		this.email = email;
		setPassword(password);
		this.userGroupId = userGroupId;

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
		preparedStatement.setInt(4, this.userGroupId);
		preparedStatement.executeUpdate();
		ResultSet rs = preparedStatement.getGeneratedKeys(); // zwraca
																// wygenerowane
																// klucze
		if (rs.next()) {
			this.id = rs.getLong(1); // pobieramy indeks wygenerowany auto w
										// DB
		}
	}

	private void updateUser(Connection conn) throws SQLException {

		PreparedStatement preparedStatement = conn.prepareStatement(UPDATE_USER_STATEMENT);
		preparedStatement.setString(1, this.username);
		preparedStatement.setString(2, this.email);
		preparedStatement.setString(3, this.password);
		preparedStatement.setInt(4, this.userGroupId);
		preparedStatement.setLong(5, this.id);
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
		int userGroupId = resultSet.getInt(USER_GROUP_COLUMN_NAME);
		User loadedUser = new User(username, email, password, userGroupId);
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

	public static User[] loadAllUsersByGroupId(Connection conn, int id) throws SQLException {

		ArrayList<User> usersByGroupId = new ArrayList<>();

		PreparedStatement preparedStatement = conn.prepareStatement(FIND_USER_BY_GROUP_ID);
		preparedStatement.setInt(1, id);
		ResultSet resultSet = preparedStatement.executeQuery();

		while (resultSet.next()) {
			usersByGroupId.add(createUser(resultSet));
		}

		User[] uByGrIdArray = new User[usersByGroupId.size()];
		return usersByGroupId.toArray(uByGrIdArray);
	}

	@Override
	public String toString() {
		return "User: id = " + id + ", username = " + username + ", email = " + email + ", password = " + password
				+ ", userGroupId = " + userGroupId + "\n";
	}

	public long getId() {
		return id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getUserGroupId() {
		return userGroupId;
	}

	public void setUserGroupId(int userGroupId) {
		this.userGroupId = userGroupId;
	}

	public String getPassword() {
		return password;
	}

	public void setId(long id) {
		this.id = id;
	}

	
}
