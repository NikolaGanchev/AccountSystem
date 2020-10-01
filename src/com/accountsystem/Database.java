package com.accountsystem;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;

public class Database {

    final private static String DATABASEURL = "jdbc:mysql://localhost:3306/account_system?serverTimezone=UTC";
    final private static String USERNAME = readDatabaseLogInInfo("username");
    final private static String PASSWORD = readDatabaseLogInInfo("password");
    private Connection connection;


    public Database() throws SQLException {
        this.connection = this.getConnection();
        this.getDatabaseEntry("userName", "Nikola");
    }
    private static String readDatabaseLogInInfo(String key){
        try {
            return ((JSONObject)new JSONParser().parse(new FileReader("databaseinfo.json"))).get(key).toString();
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        return null;
    }
    private Connection getConnection() throws SQLException {
        Connection connection = DriverManager.getConnection(DATABASEURL, USERNAME, PASSWORD);
        return connection;
    }

    public void createDatabaseEntry(String name, String password, String UUID, String email) throws SQLException {
        PreparedStatement preparedStatement = this.connection.prepareStatement("INSERT INTO users (userName, userEmail, userPassword, userUUID) values (?, ?, ?, ?)");
        preparedStatement.setString(1, name);
        preparedStatement.setString(2, email);
        preparedStatement.setString(3, password);
        preparedStatement.setString(4, UUID);
        preparedStatement.execute();
    }

    private void getDatabaseEntry(String colName, String stringToSearchFor) throws SQLException {
        PreparedStatement preparedStatement = this.connection.prepareStatement("SELECT userName FROM users WHERE ? = ?");
        preparedStatement.setString(1, colName);
        preparedStatement.setString(2,stringToSearchFor);
        ResultSet resultSet = preparedStatement.executeQuery();
        System.out.println(resultSet.getString("userName"));
    }

    /*private String[] convertResultSetToStringArray(ResultSet resultSet){
        ArrayList<String> =
    }*/
}
