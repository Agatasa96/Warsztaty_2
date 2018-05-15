package administrative_programs;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Scanner;
import connection.DBUtils;
import models.Exercises;
import models.Solutions;
import models.User;


public class Main4 {

	public static void main(String[] args) throws SQLException {
		// TODO Auto-generated method stub

		String add = "add";
		String view = "view";
		String quit = "quit";

		try (Connection conn = DBUtils.createConnection()) {

			Scanner sc = new Scanner(System.in);
			String answer = "";

			while (!answer.equals(quit)) {

				System.out.println(
						"Wybierz jedną z opcji: add - przypisywanie zadań do użytkowników, view - przeglądanie rozwiązań danego użytkownika, quit - zakończenie programu");
				answer = sc.next();

				if (answer.equals(add)) {
					try {
						System.out.println(Arrays.toString(User.loadAllUsers(conn)));
						System.out.println("Podaj id użytkownika:");
						int userId = sc.nextInt();
						System.out.println(Arrays.toString(Exercises.loadAllExercises(conn)));
						System.out.println("Podaj id cwiczenia:");
						int exerciseId = sc.nextInt();
						String description = null;
						Date updated = null;
						Date created = Date.valueOf(LocalDate.now());
						Solutions s1 = new Solutions(created, updated, description, exerciseId, userId);
						s1.save(conn);
					} catch (NullPointerException e) {
						System.out.println("Próbujesz odwołać się do id, które nie istnieje :(");
					}

				} else if (answer.equals(view)) {
					try {
						System.out.println("Podaj id użytkownika: ");
						int userId = sc.nextInt();
						Solutions.loadAllSolutionsByUserId(conn, userId);
					} catch (NullPointerException e) {
						System.out.println("Próbujesz zobaczyć rozwiązania użyktownika, który nie istnieje :(");
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
