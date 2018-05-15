package user_program;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Scanner;
import connection.DBUtils;
import models.Solutions;

public class Main1 {

	public static void main(String[] args) throws SQLException {
		String add = "add";
		String view = "view";
		String quit = "quit";

		try (Connection conn = DBUtils.createConnection()) {
			Scanner sc = new Scanner(System.in);

			System.out.println("Podaj swoje id");
			int id = sc.nextInt();

			String answer = "";

			while (!answer.equals(quit)) {

				System.out.println(
						"Wybierz jedną z opcji: add - dodawanie rozwiązania, view - przeglądanie swoich rozwiązań, quit - zakończenie programu");
				answer = sc.next();

				if (answer.equals(add)) {
					try {
						System.out.println(Arrays.toString(Solutions.loadAllExercisesWithoutSolution(conn, id)));
						System.out.println("Podaj id zadania:");
						int exerciseId = sc.nextInt();
						Solutions s1 = Solutions.loadSolutionByExIdAndUserId(conn, id, exerciseId);
						if (s1.getDescription() == null) {
							Date updated = Date.valueOf(LocalDate.now());
							System.out.println("Dodaj opis: ");
							s1.setDescription(sc.next());
							s1.setUpdated(updated);
							s1.updateSolutions(conn);
						} else {
							System.out.println("To zadanie ma już rozwiązanie");
						}
					} catch (NullPointerException e) {
						System.out.println("Próbujesz odwołać się do id zadania, które nie istnieje :(");
					}

				} else if (answer.equals(view)) {

					System.out.println(Arrays.toString(Solutions.loadAllSolutionsByUserId(conn, id)));

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
