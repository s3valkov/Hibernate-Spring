package problem_08;

import base.Minion_DB_Connection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws SQLException {
        Scanner scanner = new Scanner(System.in);

        Minion_DB_Connection minion_db_connection = new Minion_DB_Connection();
        Connection connection = minion_db_connection.getConnection();
        System.out.println("Give an array of Integers for the minionsId: ");
        int[] minionsId = Arrays.stream(scanner.nextLine().split(" ")).mapToInt(Integer::parseInt).toArray();

        PreparedStatement statement = connection
                .prepareStatement("UPDATE minions SET age = age + 1, name = LOWER (name) WHERE id = ?");

        for (int i = 0; i < minionsId.length; i++) {
            statement.setInt(1, minionsId[i]);
            statement.executeUpdate();
        }

        PreparedStatement selectStatement = connection.
                prepareStatement("SELECT  name, age FROM minions;");

        ResultSet resultSet = selectStatement.executeQuery();

        while (resultSet.next()) {
            System.out.printf("%s %s", resultSet.getString("name"), resultSet.getString("age")).println();
        }

    }
}
