package models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


public class Exercises {

	private static final String INSERT_EXERCISE_STATEMENT = "INSERT INTO WARSZTATY2.exercises (title, description) VALUES (?,?)";
	private static final String UPDATE_EXERCISE_STATEMENT = "UPDATE WARSZTATY2.exercises SET title=?, description=? where id = ?";
	private static final String DELETE_EXERCISE_STATEMENT = "DELETE FROM WARSZTATY2.exercises WHERE id= ?";
	private static final String FIND_EXERCISE_BY_ID_QUERY = "SELECT * FROM WARSZTATY2.exercises where id=?";
	private static final String FIND_ALL_EXERCISES = "SELECT * FROM WARSZTATY2.exercises";

	private static final String TITLE_COLUMN_NAME = "title";
	private static final String ID_COLUMN_NAME = "ID";
	private static final String DESCRIPTION_COLUMN_NAME = "description";

	private int id;
	private String title;
	private String description;

	public Exercises(String title, String description) {
		this.title = title;
		this.description = description;
	}

	public void save(Connection conn) throws SQLException {

		if (this.id == 0) {
			saveNewExercise(conn);

		} else {
			updateExercise(conn);
		}

	}

	private void saveNewExercise(Connection conn) throws SQLException {
		String generatedColumns[] = { ID_COLUMN_NAME };
		PreparedStatement preparedStatement;
		preparedStatement = conn.prepareStatement(INSERT_EXERCISE_STATEMENT, generatedColumns);
		preparedStatement.setString(1, this.title);
		preparedStatement.setString(2, this.description);
		preparedStatement.executeUpdate();
		ResultSet rs = preparedStatement.getGeneratedKeys();
		if (rs.next()) {
			this.id = rs.getInt(1);
		}
	}

	private void updateExercise(Connection conn) throws SQLException {

		PreparedStatement preparedStatement = conn.prepareStatement(UPDATE_EXERCISE_STATEMENT);
		preparedStatement.setString(1, this.title);
		preparedStatement.setString(2, this.description);
		preparedStatement.setInt(3, this.id);
		preparedStatement.executeUpdate();

	}

	public void delete(Connection conn) throws SQLException {

		if (this.id != 0) {
			PreparedStatement preparedStatement = conn.prepareStatement(DELETE_EXERCISE_STATEMENT);
			preparedStatement.setInt(1, this.id);
			preparedStatement.executeUpdate();
			this.id = 0;

		}

	}

	public static Exercises loadExerciseById(Connection conn, int id) throws SQLException {

		PreparedStatement preparedStatement = conn.prepareStatement(FIND_EXERCISE_BY_ID_QUERY);
		preparedStatement.setInt(1, id);
		ResultSet resultSet = preparedStatement.executeQuery();

		if (resultSet.next()) {
			return createExercise(resultSet);
		}
		return null;

	}

	public static Exercises createExercise(ResultSet resultSet) throws SQLException {

		String title = resultSet.getString(TITLE_COLUMN_NAME);
		String description = resultSet.getString(DESCRIPTION_COLUMN_NAME);

		Exercises loadedExercise = new Exercises(title, description);
		loadedExercise.id = resultSet.getInt(ID_COLUMN_NAME);

		return loadedExercise;

	}

	public static Exercises[] loadAllExercises(Connection conn) throws SQLException {

		ArrayList<Exercises> exercises = new ArrayList<>();

		PreparedStatement preparedStatement = conn.prepareStatement(FIND_ALL_EXERCISES);
		ResultSet resultSet = preparedStatement.executeQuery();

		while (resultSet.next()) {
			exercises.add(createExercise(resultSet));
		}

		Exercises[] eArray = new Exercises[exercises.size()];
		return exercises.toArray(eArray);
	}
	
	

	@Override
	public String toString() {
		return "Exercises: id = " + id + ", title = " + title + ", description = " + description+ "\n";
	}
	
	

	public int getId() {
		return id;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setDescription(String description) {
		this.description = description;
	}


}
