package administrative_programs;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Scanner;
import connection.DBUtils;
import models.Exercises;


public class Main2 {

	public static void main(String[] args) throws SQLException {
		// TODO Auto-generated method stub

		String add = "add";
		String edit = "edit";
		String delete = "delete";
		String quit = "quit";

		try (Connection conn = DBUtils.createConnection()) {

			Scanner sc = new Scanner(System.in);
			String answer = "";

			while (!answer.equals(quit)) {
				System.out.println(Arrays.toString(Exercises.loadAllExercises(conn)));
				System.out.println(
						"Wybierz jedną z opcji: " + add + " - dodanie użytkownika, " + edit + " - edycja użytkownika, "
								+ delete + " - edycja użytkownika, " + quit + " - zakończenie programu");
				answer = sc.next();

				if (answer.equals(add)) {
					System.out.println("Podaj tytuł:");
					String title = sc.next();
					System.out.println("Podaj opis:");
					String description = sc.next();

					Exercises e1 = new Exercises(title, description);
					e1.save(conn);

				} else if (answer.equals(edit)) {
					try {
						System.out.println("Podaj id: ");
						int id = sc.nextInt();
						Exercises e1 = Exercises.loadExerciseById(conn, id);
						System.out.println("Podaj tytuł:");
						e1.setTitle(sc.next());
						System.out.println("Podaj opis:");
						e1.setDescription(sc.next());

						e1.save(conn);
					} catch (NullPointerException e) {
						System.out.println("Próbujesz edytować cwiczenie, które nie istnieje :(");
					}

				} else if (answer.equals(delete)) {
					try {
						System.out.println("Podaj id: ");
						int id = sc.nextInt();
						Exercises e1 = Exercises.loadExerciseById(conn, id);
						e1.delete(conn);
					} catch (NullPointerException e) {
						System.out.println("Próbujesz usunąć ćwiczenie, które nie istnieje :(");
					}

				} else if (answer.equals(quit)) {
					System.out.println("Bye!");
				} else {
					System.out.println("Nie ma takiej opcji");
				}

			}

			sc.close();
		}

	}

}
