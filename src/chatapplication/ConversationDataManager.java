/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatapplication;

import chatapplication.ConversationDataObject;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.sql.*;

/**
 * Represents the management class that acts as a medium between the database
 * and the application for the table conversation
 * @author Hsen
 */
public class ConversationDataManager {
    private String url;
    private Connection connection;
    
    public ConversationDataManager() {
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
 * Parses the object from the database so it can be smoothly integrated
 * with the application
 * @return ConversationDataObject
 */  
    public ConversationDataObject parseDataObject(ResultSet rs) {
        ConversationDataObject conversation = null;
        try {
            conversation = new ConversationDataObject(
                rs.getString(2),
                rs.getString(3),
                rs.getString(4)
            );
        } catch(SQLException e) {
            System.out.println(e.getMessage());
        }
        return conversation;
    }
 /**
 * Fetches all conversations from the database and puts them into an ArrayList
 * @param username
 * @return ArrayList
 */  
    public ArrayList<ConversationDataObject> getConversations(String username) {
        ArrayList<ConversationDataObject> conversations = new ArrayList();
        String statement = "SELECT * FROM conversation WHERE username = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(statement);
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                conversations.add(parseDataObject(rs));
            }
        } catch(SQLException e) {
            System.out.println("Could not connect to the database");
        }
        return conversations;
    }
  /**
 * Creates a conversation using an already defined ConversationDataObject
 * @param conversation
 */
    public void createConversation(ConversationDataObject conversation) {
        String statement = "INSERT INTO conversation(username, recipient, conversation)"
            + "VALUES(?,?,?)";
        try {
            PreparedStatement ps = connection.prepareStatement(statement);
            ps.setString(1, conversation.getUsername());
            ps.setString(2, conversation.getRecipient());
            ps.setString(3, conversation.getConversation());
            ps.execute();
        } catch(SQLException e) {
            System.out.println("Could not connect to the database");
            System.out.println(e.getMessage());
        }
    }
 /**
 * Updates a conversation based on its data object
 * @param conversation
 */    
    public void updateConversation(ConversationDataObject conversation) {
        String statement = "UPDATE conversation SET conversation = ? "
                + "WHERE username = ? AND recipient = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(statement);
            ps.setString(1, conversation.getConversation());
            ps.setString(2, conversation.getUsername());
            ps.setString(3, conversation.getRecipient());
            ps.executeUpdate();
        } catch(SQLException e) {
            System.out.println("Could not connect to the database");
            System.out.println(e.getMessage());
        }
    }
    
}
