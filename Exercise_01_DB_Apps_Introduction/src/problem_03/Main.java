package problem_03;

import base.Minion_DB_Connection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws SQLException {
        Scanner scanner = new Scanner(System.in);
        Minion_DB_Connection minion_db_connection = new Minion_DB_Connection();

        Connection connection = minion_db_connection.getConnection();

        String query = "SELECT  v.name,m.name, m.age FROM  minions m JOIN minions_villains mv on m.id = mv.minion_id JOIN villains v on mv.villain_id = v.id WHERE mv.villain_id = ?;";

        PreparedStatement statement = connection.prepareStatement(query);
        System.out.println("Villain ID:");
        int villain_id = Integer.parseInt(scanner.nextLine());
        statement.setInt(1, villain_id);

        ResultSet resultSet = statement.executeQuery();

        if (!resultSet.isBeforeFirst()) {
            System.out.println("No villain with ID " + villain_id + " exists in the database.");
            System.exit(0);
        }

        int counter = 1;

        while (resultSet.next()) {
            if (counter == 1) {
                System.out.printf("Villain: %s ", resultSet.getString("v.name")).println();
            }

            System.out.printf("%d. %s %s",
                    counter,
                    resultSet.getString("m.name"),
                    resultSet.getString("m.age")).println();

            counter++;
        }
    }
}
