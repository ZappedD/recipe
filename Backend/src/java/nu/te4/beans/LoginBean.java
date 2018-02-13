/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nu.te4.beans;

import java.io.StringReader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Base64;
import javax.ejb.Stateless;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;

/**
 *
 * @author Jacob
 */
@Stateless
public class LoginBean {
        public int createUser(String body){
        try {
            System.out.println("Hello");
                    JsonReader jsonReader = Json.createReader(new StringReader(body));
                    JsonObject credentials = jsonReader.readObject();
                    jsonReader.close();
                    
                    String userName =  credentials.getString("username");
                    String password = credentials.getString("password");
                   
                    // OM ANÄNDARE FINNS RETURN 4XX
                    
                    //OM PASSWORD FÖR SVAGT RETURN 4XX
                    
                    Connection connection = ConnectionFactory.getConnection();

                        Statement stmt = connection.createStatement();
                        String hash = BCrypt.hashpw(password, BCrypt.gensalt());
                        System.out.println("Hellu" + userName);
                        String sql = String.format("INSERT INTO users VALUES(null, '1' ,'none','%s', '%s')", userName, hash);
                        System.out.println(sql);
                        stmt.executeUpdate(sql);
                        connection.close();
                        
                    return 201;
        } catch (Exception e) {
            
            System.out.println(e.getMessage());
            return 500;
        }
            
            
    }
    
    public boolean checkCredentials(String basic_auth) {
        basic_auth = basic_auth.substring(basic_auth.indexOf(" ")+1);
        System.out.println("The passsword ch " + basic_auth);
            byte[] decode = Base64.getDecoder().decode(basic_auth);
            String credentials = new String(decode);
            
            String username = credentials.substring(0, credentials.indexOf(":"));
            String password = credentials.substring(credentials.indexOf(":")+1);

            try {
              Connection connection = ConnectionFactory.getConnection();
              Statement stmt = connection.createStatement();
              String sql = String.format("SELECT * FROM users WHERE username='%s'",username);
                System.out.println(sql);
                ResultSet result = stmt.executeQuery(sql);
                
                if (result.next()) {
                    if (BCrypt.checkpw(password, result.getString("password"))) {
                        return true;
                    }
                    else{
                        //fel lösen
                        return false;
                    }
                }
                else{
                    //user not found 
                    System.out.println("User Not found");
                    return false;
                }
                
        } catch (Exception e) {
                System.out.println("Error : " + e.getMessage());
        }
                        return false;

    }
}
