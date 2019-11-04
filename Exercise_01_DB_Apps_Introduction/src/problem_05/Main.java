package problem_05;

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
        System.out.println("Give a country name: ");
        String countryName = scanner.nextLine();
        System.out.println(updateTowns(countryName, connection));

    }

    private static String updateTowns(String countryName, Connection connection) throws SQLException {
        StringBuilder sb = new StringBuilder();
        String text = null;

        String query = "UPDATE towns SET name = UPPER(name) WHERE country = ? AND name NOT  LIKE BINARY UPPER (name);";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, countryName);
        int affectedTowns = statement.executeUpdate();

        if (affectedTowns > 0) {
            sb.append(affectedTowns).append(" town names were affected.").append(System.lineSeparator()).append("[");

            String selectQuery = "SELECT name  FROM  towns where  country = ?;";
            PreparedStatement selectStatement = connection.prepareStatement(selectQuery);
            selectStatement.setString(1, countryName);
            ResultSet set = selectStatement.executeQuery();

            while (set.next()) {
                sb.append(set.getString("name")).append(", ");
            }

            text = sb.substring(0, sb.length() - 2);
            text = text + "]";
        } else {
            text = "No town names were affected.";
        }

        return text;
    }


}
