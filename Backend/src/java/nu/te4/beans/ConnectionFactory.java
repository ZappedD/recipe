/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nu.te4.beans;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author Jacob
 */
public class ConnectionFactory {
      public static Connection getConnection() throws SQLException, ClassNotFoundException{
        String url = "jdbc:mysql://localhost/recipe-app";
        String urlHost = "jdbc:mysql://localhost/jacob-recipe-app";
        String username = "root";
        String password = "";
        String usernameU = "jacob_te4";
        String passwordU = "jacob_te4";
        Class.forName("com.mysql.jdbc.Driver");
        return DriverManager.getConnection(urlHost  , usernameU, passwordU);
        
    }
}
