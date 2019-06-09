/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatapplication;
import java.awt.image.DataBufferByte;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import javax.imageio.ImageIO;

/**
 * This class is the medium of the communication with the database for the user
 * table
 * @author Hsen
 */
public class UserDataManager {
    private Connection connection;
    private String url;
    
    
    public UserDataManager() {
        this.connect();
    }
 /**
 * Connects to a MySQL database called 'chatapp'
 */
    public void connect() {
        try {
            url = "jdbc:mysql://localhost:3306/chatapp";
            connection = DriverManager.getConnection(url, "root", "root");
        } catch(SQLException e) {
            System.out.println("Could not connect to database");
            System.out.println(e.getMessage());
        }
    }
 /**
 * Parses the object so it can be smoothly integrated with the application
 * Used with the Get methods
 * @param rs
 */
    public UserDataObject parseDataObject(ResultSet rs) {
        UserDataObject user = null;
        try {
            user = new UserDataObject(
                rs.getString(2),
                rs.getString(3),
                rs.getDate(4).toString(),
                rs.getBinaryStream(5),
                rs.getDate(6).toString() + " " + rs.getTime(6)
            );
        } catch(SQLException e) {
            System.out.println(e.getMessage());
        }
        return user;
    }
 /**
 * Get a user from the database and saves it into a UserDataObject
 * @param username
 */    
    public UserDataObject getUser(String username) {
        UserDataObject user = null;
        String statement = "SELECT * FROM user WHERE username = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(statement);
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if(rs.next()) {
                user = parseDataObject(rs);
            }
        } catch(SQLException e) {
            System.out.println("Could not connect to the database");
        }
        return user;
    }
 /**
 * Get all users from the database and stores them into an ArrayList of
 * UserDataObjects
 * @return ArrayList
 */
    public ArrayList<UserDataObject> getUsers() {
        ArrayList<UserDataObject> users = new ArrayList();
        String statement = "SELECT * FROM user";
        try {
            Statement req = connection.createStatement();
            ResultSet rs = req.executeQuery(statement);
            while(rs.next()) {
                UserDataObject user = parseDataObject(rs);
                users.add(user);
            }
        } catch(SQLException e) {
            System.out.println("Could not connect to the database");
            System.out.println(e.getMessage());
        }
        return users;
    }
 /**
 * Creates a user using an already defined UserDataObject
 * @param user
 */
    public void createUser(UserDataObject user) {
        String statement = "INSERT INTO user(username, password, age, photo, last_login)"
                + "VALUES(?,?,?,?,?)";
        try {
            PreparedStatement ps = connection.prepareStatement(statement);
            System.out.println(user.getLastLogin());
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getAge());
            ps.setBinaryStream(4, user.getPhoto());
            ps.setString(5, user.getLastLogin());
            ps.execute();
        } catch(SQLException e) {
            System.out.println("Could not connect to the database");
            System.out.println(e.getMessage());
        }
    }
  /**
 * Updates a user using an already defined UserDataObject
 * @param username
 * @param user
 */   
    public void updateUser(String username, UserDataObject user) {
        String statement = "UPDATE user SET username = ?, password = ?, age = ?, photo = ?, last_login = ?"
                + "WHERE username = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(statement);
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getAge());
            ps.setBinaryStream(4, user.getPhoto());
            ps.setString(5, user.getLastLogin());
            ps.setString(6, username);
            ps.executeUpdate();
        } catch(SQLException e) {
            System.out.println("Could not connect to the database");
            System.out.println(e.getMessage());
        }
    }
 /**
 * Removes a user from the database via its username
 * @param username
 */
    public void removeUser(String username) {
        String statement = "DELETE FROM user WHERE username = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(statement);
            ps.setString(1, username);
            ps.executeUpdate();
        } catch(SQLException e) {
            System.out.println("Could not connect to the database");
            System.out.println(e.getMessage());
        }
    }
 /**
 * Checks if a user exists in the database via its username
 * @param username
 */
    public boolean userExists(String username) {
        if(getUser(username) == null)
            return false;
        else
            return true;
    }
    
    
    
}
