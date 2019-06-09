/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatapplication;

/**
 * Represents the object mirror between the table conversation in the database
 * and the application
 * @author Hsen
 */
public class ConversationDataObject {
    private String username;
    private String recipient;
    private String conversation;
    
    public ConversationDataObject() {
        
    }
    
    public ConversationDataObject(
            String username,
            String recipient,
            String conversation
    )
    {
        this.username = username;
        this.recipient = recipient;
        this.conversation = conversation;
    }
    
    public String getUsername() {
        return this.username;
    }
    
    public String getRecipient() {
        return this.recipient;
    }
    
    public String getConversation() {
        return this.conversation;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }
    
    public void setConversation(String conversation) {
        this.conversation = conversation;
    }
    
}
