package base;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.Scanner;

public class Minion_DB_Connection {
    public static final String CONNECTION_URL = "jdbc:mysql://localhost:3306/minions_db";

    public Connection getConnection() throws SQLException {
        Properties props = new Properties();
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter username default {root}: ");
        String username = sc.nextLine().trim();
        username = username.equals("") ? "root" : username;
        System.out.println();

        System.out.print("Enter password: ");
        String password = sc.nextLine().trim();
        System.out.println();

        //Set the property for username and password
        props.setProperty("user", username);
        props.setProperty("password", password);

        Connection connection = DriverManager
                .getConnection(CONNECTION_URL, props);

        return connection;
    }
}
