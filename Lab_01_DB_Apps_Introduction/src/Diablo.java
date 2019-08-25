import java.sql.*;
import java.util.Properties;
import java.util.Scanner;

public class Diablo {
    public static void main(String[] args) throws SQLException {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter username default {root}: ");
        String username = scanner.nextLine();
        username = username.equals("") ? "root" : username;
        System.out.println();

        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        System.out.println();

        Properties properties = new Properties();

        properties.setProperty("user", username.trim());
        properties.setProperty("password", password.trim());

        Connection connection =
                DriverManager.getConnection("jdbc:mysql://localhost:3306/diablo", properties);

        String query = "SELECT u.user_name, concat(u.first_name,' ',u.last_name) full_name, count(user_id) game_counts  FROM users u LEFT JOIN users_games ug  ON u.id = ug.user_id WHERE u.user_name = ? GROUP BY user_id";

        PreparedStatement stmt =
                connection.prepareStatement(query);

        String user_name = scanner.nextLine().trim();
        stmt.setString(1, user_name);
        ResultSet resultSet = stmt.executeQuery();

        if (!resultSet.isBeforeFirst()) {
            System.out.println("No such user exists!");
            System.exit(0);
        }

        while (resultSet.next()) {
            System.out.printf("Username: %s\n%s has played %s games.",
                    resultSet.getString("user_name"),
                    resultSet.getString("full_name"),
                    resultSet.getString("game_counts")
            );
        }

    }
}
