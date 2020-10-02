package com.accountsystem;

import com.sun.jdi.InvalidTypeException;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;

public class Database {

    final private static String DATABASEURL = "jdbc:mysql://localhost:3306/account_system?serverTimezone=UTC";
    final private static String USERNAME = readDatabaseLogInInfo("username");
    final private static String PASSWORD = readDatabaseLogInInfo("password");
    private Connection connection;


    public Database(){
        try {
            this.connection = this.getConnection();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
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

    public ResultSet getDatabaseProfiles(DATABASECOLUMNS databasecolumns, String stringToSearchFor) throws SQLException, InvalidTypeException {
        PreparedStatement preparedStatement = this.connection.prepareStatement("SELECT userName, userUUID, userPassword, userEmail FROM users WHERE " + getColumnStringFromEnum(databasecolumns) + " = ?");
        preparedStatement.setString(1,stringToSearchFor);
        ResultSet resultSet = preparedStatement.executeQuery();
        return resultSet;
    }

    public boolean checkIfExists(DATABASECOLUMNS databasecolumns, String stringToSearchFor) throws InvalidTypeException, SQLException {
        String column = getColumnStringFromEnum(databasecolumns);
        PreparedStatement preparedStatement = this.connection.prepareStatement("SELECT " + column + " FROM users WHERE " + column + " = ?");
        preparedStatement.setString(1, stringToSearchFor);
        ResultSet resultSet = preparedStatement.executeQuery();
        if(!resultSet.next()){
            return false;
        }
        return true;
    }
    private String getColumnStringFromEnum(DATABASECOLUMNS databasecolumns) throws InvalidTypeException {
        switch(databasecolumns){
            case userName:
                return "userName";
            case userPassword:
                return "userPassword";
            case userUUID:
                return "userUUID";
            case userEmail:
                return "userEmail";
            default:
                throw new InvalidTypeException();
        }
    }
}
