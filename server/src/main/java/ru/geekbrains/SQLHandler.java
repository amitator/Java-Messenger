package ru.geekbrains;

import java.sql.*;

public class SQLHandler {
    private static Connection connection;
    private static Statement stmt;

    public static void connect() throws ClassNotFoundException, SQLException {
        Class.forName("org.sqlite.JDBC");
        connection = DriverManager.getConnection("jdbc:sqlite:data.db");
    }

    public static String getNickByLoginPass(String login, String pass){
        try {
            ResultSet rs = stmt.executeQuery(String.format("SELECT nick FROM users WHERE login = %s AND password = %s;", login, pass));
            if (rs.next()){
                return rs.getString(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void disconnect(){
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
