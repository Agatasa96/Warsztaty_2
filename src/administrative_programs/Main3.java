package administrative_programs;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Scanner;
import connection.DBUtils;
import models.UserGroups;

public class Main3 {

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
				System.out.println(Arrays.toString(UserGroups.loadAllUserGroups(conn)));
				System.out.println(
						"Wybierz jedną z opcji: " + add + " - dodanie użytkownika, " + edit + " - edycja użytkownika, "
								+ delete + " - edycja użytkownika, " + quit + " - zakończenie programu");
				answer = sc.next();

				if (answer.equals(add)) {
					System.out.println("Podaj nazwe:");
					String name = sc.next();
					UserGroups ug1 = new UserGroups(name);
					ug1.save(conn);

				} else if (answer.equals(edit)) {
					try {
						System.out.println("Podaj id: ");
						int id = sc.nextInt();
						UserGroups ug1 = UserGroups.loadUserGroupById(conn, id);
						System.out.println("Podaj nazwę:");
						ug1.setName(sc.next());
						ug1.save(conn);
					} catch (NullPointerException e) {
						System.out.println("Próbujesz edytować grupę, która nie istnieje :(");
					}

				} else if (answer.equals(delete)) {
					try {
						System.out.println("Podaj id: ");
						int id = sc.nextInt();
						UserGroups ug1 = UserGroups.loadUserGroupById(conn, id);
						ug1.delete(conn);
					} catch (NullPointerException e) {
						System.out.println("Próbujesz usunąć grupę, która nie istnieje :(");
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
