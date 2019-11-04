package problem_07;

import base.Minion_DB_Connection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws SQLException {
        Scanner scanner = new Scanner(System.in);

        Minion_DB_Connection minion_db_connection = new Minion_DB_Connection();
        Connection connection = minion_db_connection.getConnection();

        PreparedStatement statement = connection.prepareStatement("SELECT  name FROM  minions");
        ResultSet resultSet = statement.executeQuery();

        List<String> names = new LinkedList<>();

        while (resultSet.next()) {
            names.add(resultSet.getString("name"));
        }

        int evenCount = 0;
        int oddCount = 1;

        for (int i = 0; i < names.size(); i++) {

            if (i % 2 == 0) {
                System.out.println(names.get(evenCount));
                evenCount++;
            } else {
                System.out.println(names.get(names.size() - oddCount));
                oddCount++;
            }
        }

    }
}