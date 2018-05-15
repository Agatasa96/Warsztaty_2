package models;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

public class Solutions {

	private static final String INSERT_SOLUTION_STATEMENT = "INSERT INTO WARSZTATY2.solutions (created, updated, description, exercise_id, users_id) VALUES (?, ?, ?, ?, ?)";
	private static final String UPDATE_SOLUTION_STATEMENT = "UPDATE WARSZTATY2.solutions SET created=?, updated=?, description=?, exercise_id=? users_id=? where id = ?";
	private static final String DELETE_SOLUTION_STATEMENT = "DELETE FROM WARSZTATY2.solutions WHERE id= ?";
	private static final String FIND_SOLUTION_BY_ID_QUERY = "SELECT * FROM WARSZTATY2.solutions where id=?";
	private static final String FIND_ALL_SOLUTIONS = "SELECT * FROM WARSZTATY2.solutions";
	private static final String FIND_ALL_SOLUTIONS_BY_USER_ID = "SELECT * FROM WARSZTATY2.solutions where users_id = ?";
	private static final String FIND_ALL_SOLUTIONS_BY_EXERCISE_ID = "SELECT * FROM WARSZTATY2.solutions where exercise_id = ? ORDER BY created desc";

	private static final String ID_COLUMN_NAME = "ID";
	private static final String CREATED_COLUMN_NAME = "created";
	private static final String UPDATED_COLUMN_NAME = "updated";
	private static final String DESCRIPTION_COLUMN_NAME = "description";
	private static final String EXERCISE_ID_COLUMN_NAME = "exercise_id";
	private static final String USERS_ID_COLUMN_NAME = "users_id";
	
	private int id;
	private Date created;
	private Date updated;
	private String description;
	private int exercieId;
	private long usersId;
	

	public Solutions(Date created, Date updated, String description, int exerciseId, long userId) {
		this.created = created;
		this.updated = updated;
		this.description = description;
		this.exercieId = exerciseId;
		this.usersId = userId;
	}

	public void save(Connection conn) throws SQLException {

		if (this.id == 0) {
			saveNewSolutions(conn);

		} else {
			updateSolutions(conn);
		}

	}

	private void saveNewSolutions(Connection conn) throws SQLException {
		String generatedColumns[] = { ID_COLUMN_NAME };
		PreparedStatement preparedStatement;
		preparedStatement = conn.prepareStatement(INSERT_SOLUTION_STATEMENT, generatedColumns);
		preparedStatement.setDate(1, this.created);
		preparedStatement.setDate(2, this.updated);
		preparedStatement.setString(3, this.description);
		preparedStatement.setInt(4, this.exercieId);
		preparedStatement.setLong(5, this.usersId);
		preparedStatement.executeUpdate();
		ResultSet rs = preparedStatement.getGeneratedKeys();
		if (rs.next()) {
			this.id = rs.getInt(1);
		}
	}

	private void updateSolutions(Connection conn) throws SQLException {

		PreparedStatement preparedStatement = conn.prepareStatement(UPDATE_SOLUTION_STATEMENT);
		preparedStatement.setDate(1, this.created);
		preparedStatement.setDate(2, this.updated);
		preparedStatement.setString(3, this.description);
		preparedStatement.setInt(4, this.exercieId);
		preparedStatement.setLong(5, this.usersId);
		preparedStatement.setInt(6, this.id);
		preparedStatement.executeUpdate();

	}

	public void delete(Connection conn) throws SQLException {

		if (this.id != 0) {
			PreparedStatement preparedStatement = conn.prepareStatement(DELETE_SOLUTION_STATEMENT);
			preparedStatement.setInt(1, this.id);
			preparedStatement.executeUpdate();
			this.id = 0;

		}

	}

	public static Solutions loadSolutionById(Connection conn, int id) throws SQLException {

		PreparedStatement preparedStatement = conn.prepareStatement(FIND_SOLUTION_BY_ID_QUERY);
		preparedStatement.setInt(1, id);
		ResultSet resultSet = preparedStatement.executeQuery();

		if (resultSet.next()) {
			return createSolution(resultSet);
		}
		return null;

	}

	public static Solutions createSolution(ResultSet resultSet) throws SQLException {

		Date created = resultSet.getDate(CREATED_COLUMN_NAME);
		Date updated = resultSet.getDate(UPDATED_COLUMN_NAME);
		String description = resultSet.getString(DESCRIPTION_COLUMN_NAME);
		int exerciseId = resultSet.getInt(EXERCISE_ID_COLUMN_NAME);
		long userId = resultSet.getLong(USERS_ID_COLUMN_NAME);
		

		Solutions loadedSolution = new Solutions(created, updated, description, exerciseId, userId);
		loadedSolution.id = resultSet.getInt(ID_COLUMN_NAME);

		return loadedSolution;

	}

	public static Solutions[] loadAllSolutions(Connection conn) throws SQLException {

		ArrayList<Solutions> solutions = new ArrayList<>();

		PreparedStatement preparedStatement = conn.prepareStatement(FIND_ALL_SOLUTIONS);
		ResultSet resultSet = preparedStatement.executeQuery();

		while (resultSet.next()) {
			solutions.add(createSolution(resultSet));
		}

		Solutions[] sArray = new Solutions[solutions.size()];
		return solutions.toArray(sArray);
	}

	public static Solutions[] loadAllSolutionsByUserId(Connection conn, int id) throws SQLException {

		ArrayList<Solutions> solutionsByUserId = new ArrayList<>();

		PreparedStatement preparedStatement = conn.prepareStatement(FIND_ALL_SOLUTIONS_BY_USER_ID);
		preparedStatement.setInt(1, id);
		ResultSet resultSet = preparedStatement.executeQuery();

		while (resultSet.next()) {
			solutionsByUserId.add(createSolution(resultSet));
		}

		Solutions[] sByIdArray = new Solutions[solutionsByUserId.size()];
		return solutionsByUserId.toArray(sByIdArray);
	}

	public static Solutions[] loadAllSolutionsByExerciseId(Connection conn, int id) throws SQLException {

		ArrayList<Solutions> solutionsByExerciseId = new ArrayList<>();

		PreparedStatement preparedStatement = conn.prepareStatement(FIND_ALL_SOLUTIONS_BY_EXERCISE_ID);
		preparedStatement.setInt(1, id);
		ResultSet resultSet = preparedStatement.executeQuery();

		while (resultSet.next()) {
			solutionsByExerciseId.add(createSolution(resultSet));
		}

		Solutions[] sByExIdArray = new Solutions[solutionsByExerciseId.size()];
		return solutionsByExerciseId.toArray(sByExIdArray);
	}

	@Override
	public String toString() {
		return "Solutions: id = " + id + ", created = " + created + ", updated = " + updated + ", description = "
				+ description + ", exercieId = " + exercieId + ", usersId = " + usersId + "\n";
	}


}
