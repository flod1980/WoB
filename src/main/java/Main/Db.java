/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Main;

//import com.mysql.jdbc.Connection;
import java.sql.DriverManager;
import java.sql.Connection;

/**
 *
 * @author flod
 */
public class Db {
    
    public Connection getConnection() throws Exception{
        try{
            String driver = "com.mysql.jdbc.Driver";
            String url = "jdbc:mysql://localhost:3306/wob";
            String username = "root";
            String password = "rudolf";
            
            Class.forName(driver);
            
            Connection conn = DriverManager.getConnection(url,username,password);
            System.out.println("Connected");
            return conn;
            
        }catch(Exception e){
            
            return null;
            
        }
    }
    
}
