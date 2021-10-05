/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rainventario;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author rdmn5
 */
public class Dbconnection {

    private String url = "jdbc:sqlserver://localhost:1433;databaseName=RAInventario";
    private String username = "user";
    private String password = "a1234";

    public Connection getConnection() {
        Connection Connection = null;
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            System.out.println("JDBC driver loaded");
            Connection = DriverManager.getConnection(url, username, password);
            System.out.println("connect to database successfully ");
        } catch (Exception e) {
            System.out.println("Error loading JDBC driver");
            System.out.println("there is an error for loading the databse \n" + e);
            System.out.println(e);
            // System.exit(0);
        }
        return Connection;
    }

 
}
