import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Scanner;

import connection.DBUtils;
import models.User;
import models.UserGroups;



public class Main {

	public static void main(String[] args) throws SQLException {
		// TODO Auto-generated method stub

		String add = "add";
		String edit = "edit";
		String delete = "delete";
		String quit = "quit";

		try (Connection conn = DBUtils.createConnection()) {

			System.out.println(Arrays.toString(User.loadAllUsers(conn)));

			Scanner sc = new Scanner(System.in);
			String answer = "";

			while (!answer.equals(quit)) {
				System.out.println(
						"Wybierz jedną z opcji: " + add + " - dodanie użytkownika, " + edit + " - edycja użytkownika, "
								+ delete + " - edycja użytkownika, " + quit + " - zakończenie programu");
				answer = sc.next();

				if (answer.equals(add)) {
					System.out.println("Podaj imie:");
					String username = sc.next();
					System.out.println("Podaj email:");
					String email = sc.next();
					System.out.println("Podaj hasło: ");
					String password = sc.next();
					System.out.println("Podaj id grupy");
					System.out.println(Arrays.toString(UserGroups.loadAllUserGroups(conn)));
					int userGroupsId = sc.nextInt();
					User u1 = new User(username, email, password, userGroupsId);
					u1.save(conn);

				} else if (answer.equals(edit)) {
					System.out.println("Podaj id: ");
					int id = sc.nextInt();
					User u1 = User.loadUserById(conn, id);
					System.out.println("Podaj imie:");
					u1.setUsername(sc.next());
					System.out.println("Podaj email:");
					u1.setEmail(sc.next());
					System.out.println("Podaj hasło: ");
					String password = sc.next();
					u1.setPassword(password);
					System.out.println("Podaj id grupy");
					System.out.println(Arrays.toString(UserGroups.loadAllUserGroups(conn)));
					u1.setUserGroupId(sc.nextInt());

					u1.save(conn);

				} else if (answer.equals(delete)) {
					System.out.println("Podaj id uzytkownika");
					int id = sc.nextInt();
					User u1 = User.loadUserById(conn, id);
					u1.delete(conn);

				} else if (answer.equals(quit)) {
					System.out.println("Bye!");
				} else {
					System.out.println("Nie ma takiej opcji");
				}

			}
		}

	}

}
