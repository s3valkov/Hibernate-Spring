package problem_04;

import base.Minion_DB_Connection;

import java.sql.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws SQLException {
        Scanner scanner = new Scanner(System.in);
        Minion_DB_Connection minion_db_connection = new Minion_DB_Connection();
        Connection connection = minion_db_connection.getConnection();
        System.out.println("Information about the minion:");
        String minionInfo[] = scanner.nextLine().split("\\s+");

        System.out.println("Information about the villain:");
        String villainInfo[] = scanner.nextLine().split("\\s+");

        //add the town if is not in the towns_table
        if (!isTownInTheDatabase(minionInfo[3], connection)) {
            addTown(minionInfo[3], connection);
        }

        //add the villain if is not in the villains_table
        if (!isVillainInTheDatabase(villainInfo[1], connection)) {
            addVillain(villainInfo[1], connection);
        }

        insertMinion(connection, minionInfo[1], Integer.parseInt(minionInfo[2]), minionInfo[3], villainInfo[1]);


    }

    private static void addTown(String townName, Connection connection) throws SQLException {
        String query = "INSERT INTO towns (name) VALUES (?);";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, townName);
        statement.executeUpdate();
        System.out.printf("Town %s was added to the database.", townName).println();
    }

    private static boolean isTownInTheDatabase(String town, Connection connection) throws SQLException {
        String query = "SELECT  name FROM towns WHERE name = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, town);
        ResultSet resultSet = statement.executeQuery();
        return resultSet.isBeforeFirst();
    }

    private static void addVillain(String villainName, Connection connection) throws SQLException {
        String query = "INSERT INTO villains (name,evilness_factor) VALUES (?,?);";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, villainName);
        statement.setString(2, "evil");
        statement.executeUpdate();
        System.out.printf("Villain %s added to the database.", villainName).println();
    }

    private static boolean isVillainInTheDatabase(String villainName, Connection connection) throws SQLException {
        String query = "SELECT  name FROM villains WHERE name = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, villainName);
        ResultSet resultSet = statement.executeQuery();
        return resultSet.isBeforeFirst();
    }

    private static void insertMinion(Connection connection, String minionName, int minionAge, String minionTown, String villainName) throws SQLException {

        final String sqlMinion = "INSERT into minions(name, age, town_id) VALUES(?, ?, (SELECT t.id FROM towns t WHERE t.name = ?)); ";
        PreparedStatement preparedStatement = connection.prepareStatement(sqlMinion);


        preparedStatement.setString(1, minionName);
        preparedStatement.setInt(2, minionAge);
        preparedStatement.setString(3, minionTown);

        preparedStatement.executeUpdate();


        preparedStatement = connection.prepareStatement("INSERT INTO minions_villains(minion_id, villain_id) VALUES ((SELECT m.id FROM minions m ORDER BY m.id DESC LIMIT 1), (SELECT v.id FROM villains v WHERE v.name = ?));");

        preparedStatement.setString(1, villainName);
        preparedStatement.executeUpdate();

        System.out.println("Successfully added " + minionName + " to be minion of " + villainName + ".");

    }
}

