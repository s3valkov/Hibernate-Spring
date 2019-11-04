package problem_02;

import base.Minion_DB_Connection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Main {

    public static void main(String[] args) throws SQLException {
        Minion_DB_Connection minion_db_connection = new Minion_DB_Connection();

        Connection connection = minion_db_connection.getConnection();

        String query = "SELECT name , count(minion_id) `count` FROM villains JOIN minions_villains mv on villains.id = mv.villain_id GROUP BY villain_id HAVING `count` > 15 ORDER BY `count` DESC ;";

        getResult(query, connection);

    }

    private static void getResult(String query, Connection connection) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(query);
        ResultSet resultSet = statement.executeQuery();

        while (resultSet.next()) {
            System.out.printf("%s %s",
                    resultSet.getString("name"),
                    resultSet.getString("count")).println();
        }
    }

}
