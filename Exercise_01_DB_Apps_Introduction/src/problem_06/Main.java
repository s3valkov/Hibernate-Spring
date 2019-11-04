package problem_06;

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

        System.out.println("Give a villain ID: ");
        int villainId = Integer.parseInt(scanner.nextLine());

        deleteVillain(connection, villainId);
    }

    private static void deleteVillain(Connection connection, int villainId) throws SQLException {
        connection.setAutoCommit(false);
        PreparedStatement selectStatement = connection.prepareStatement("SELECT  name FROM villains WHERE id = ?;");
        selectStatement.setInt(1, villainId);
        ResultSet nameSet = selectStatement.executeQuery();
        nameSet.next();
        String name = nameSet.getString("name");

        if (name.equals("")) {
            System.out.println("No such villain was found!");
        } else {
            PreparedStatement statement = connection.prepareStatement("DELETE FROM minions_villains WHERE villain_id = ?;");
            statement.setInt(1, villainId);
            int affectedRows = statement.executeUpdate();

            String query = "DELETE FROM villains WHERE id = ?;";
            PreparedStatement deleteStatement = connection.prepareStatement(query);
            deleteStatement.setInt(1, villainId);
            deleteStatement.executeUpdate();

            System.out.println(name + " was deleted");
            System.out.println(affectedRows + " minions released");
        }

    }
}
