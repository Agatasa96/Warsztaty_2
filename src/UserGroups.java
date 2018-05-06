import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

public class UserGroups {

	private static final String INSERT_USERGROUP_STATEMENT = "INSERT INTO WARSZTATY2.user_groups (name) VALUES (?)";
	private static final String UPDATE_USERGROUP_STATEMENT = "UPDATE WARSZTATY2.user_groups SET name=? where id = ?";
	private static final String DELETE_USERGROUP_STATEMENT = "DELETE FROM WARSZTATY2.user_groups WHERE id= ?";
	private static final String FIND_USERGROUP_BY_ID_QUERY = "SELECT * FROM WARSZTATY2.user_groups where id=?";
	private static final String FIND_ALL_USERGROUPS = "SELECT * FROM WARSZTATY2.user_groups";

	private static final String ID_COLUMN_NAME = "ID";
	private static final String NAME_COLUMN_NAME = "name";

	private int id;
	private String name;

	public UserGroups(String name) {
		this.name = name;

	}

	public void save(Connection conn) throws SQLException {

		if (this.id == 0) {
			saveNewUserGroup(conn);

		} else {
			updateUserGroup(conn);
		}

	}

	private void saveNewUserGroup(Connection conn) throws SQLException {
		String generatedColumns[] = { ID_COLUMN_NAME };
		PreparedStatement preparedStatement;
		preparedStatement = conn.prepareStatement(INSERT_USERGROUP_STATEMENT, generatedColumns);
		preparedStatement.setString(1, this.name);

		preparedStatement.executeUpdate();
		ResultSet rs = preparedStatement.getGeneratedKeys();
		if (rs.next()) {
			this.id = rs.getInt(1);
		}
	}

	private void updateUserGroup(Connection conn) throws SQLException {

		PreparedStatement preparedStatement = conn.prepareStatement(UPDATE_USERGROUP_STATEMENT);
		preparedStatement.setString(1, this.name);
		preparedStatement.setInt(2, this.id);
		preparedStatement.executeUpdate();

	}

	public void delete(Connection conn) throws SQLException {

		if (this.id != 0) {
			PreparedStatement preparedStatement = conn.prepareStatement(DELETE_USERGROUP_STATEMENT);
			preparedStatement.setInt(1, this.id);
			preparedStatement.executeUpdate();
			this.id = 0;

		}

	}

	public static UserGroups loadUserGroupById(Connection conn, int id) throws SQLException {

		PreparedStatement preparedStatement = conn.prepareStatement(FIND_USERGROUP_BY_ID_QUERY);
		preparedStatement.setInt(1, id);
		ResultSet resultSet = preparedStatement.executeQuery();

		if (resultSet.next()) {
			return createUserGroup(resultSet);
		}
		return null;

	}

	public static UserGroups createUserGroup(ResultSet resultSet) throws SQLException {

		String name = resultSet.getString(NAME_COLUMN_NAME);

		UserGroups loadedUserGroup = new UserGroups(name);
		loadedUserGroup.id = resultSet.getInt(ID_COLUMN_NAME);

		return loadedUserGroup;

	}

	public static UserGroups[] loadAllUserGroups(Connection conn) throws SQLException {

		ArrayList<UserGroups> userGroups = new ArrayList<>();

		PreparedStatement preparedStatement = conn.prepareStatement(FIND_ALL_USERGROUPS);
		ResultSet resultSet = preparedStatement.executeQuery();

		while (resultSet.next()) {
			userGroups.add(createUserGroup(resultSet));
		}

		UserGroups[] ugArray = new UserGroups[userGroups.size()];
		return userGroups.toArray(ugArray);
	}

	public static void main(String[] args) throws SQLException {

		try (Connection conn = DBUtils.createConnection()) {

			UserGroups[] userGroups = UserGroups.loadAllUserGroups(conn);
			System.out.println(Arrays.toString(userGroups));
		}
	}

}
