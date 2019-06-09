/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatapplication;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import chatapplication.Server;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * This method is exclusively responsible for receiving and delivering messages
 * between users and for keeping the chats up to date
 * It takes the socket bound to a certain user and takes over its management through
 * infinite loops of checking for changes and syncing content
 * @author Hsen
 */
public class ClientHandler implements Runnable {
    private String name;
    private DataInputStream inputStream;
    private DataOutputStream outputStream;
    private DataOutputStream chatsOutputStream;
    private String recipientString;
    private static int instances = 0;
    private int oldInstances = 0;
    private boolean isLoggedIn;
    private UserDataManager dataManager = new UserDataManager();
    Socket socket;
    Socket chatsSocket;
 
 /**
 * This Runnable/Thread is responsible for continuously refreshing chats in
 * a certain user interface so they stay up to date
 */
    private volatile Runnable refreshChats =
        new Runnable() {
            @Override
            public synchronized void run() {
                try {
                    while(isLoggedIn) {
                        
                        if(Server.getHandlerThread() != null) {
                            Server.getHandlerThread().join();
                        }
                            
                        while(oldInstances != instances || Server.getRefreshChats()) {
                            Server.setRefreshChats(false);
                            oldInstances = instances;
                            StringBuilder builder = new StringBuilder();
                            ArrayList<UserDataObject> users = dataManager.getUsers();
                            for(Object userObject: users.toArray()) {
                                var user = (UserDataObject)userObject;
                                if(!user.getUsername().equals(name)) {
                                    if(true) {
                                        builder.append(user.getUsername())
                                                .append("##");
                                        boolean found = false;
                                        for(Object clientObject: Server.getClients().toArray()) {
                                            var client = (ClientHandler)clientObject;
                                            if(client.getClientName().equals(user.getUsername())) {
                                                builder.append(client.isLoggedIn);
                                                found = true;
                                                break;
                                            }
                                        }
                                        if(!found) {
                                            builder.append("false");
                                        }
                                        builder.append("###");
                                    }
                                }
                            }
                            String chats = name + ">>>" + builder.toString();
                            System.out.println(chats);
                            chatsOutputStream.writeUTF(chats);
                        }
                    }
                } catch(IOException e) {
                    System.out.println(e.getMessage());
                } catch(InterruptedException e) {
                    
                }
            }
        };
    
    public ClientHandler(
            String name, 
            Socket socket, 
            Socket chatsSocket,
            DataInputStream inputStream, 
            DataOutputStream outputStream,
            DataOutputStream chatsOutputStream
    ) {
        this.name = name;
        this.inputStream = inputStream;
        this.outputStream = outputStream;
        this.socket = socket;
        this.chatsSocket = chatsSocket;
        this.chatsOutputStream = chatsOutputStream;
        this.isLoggedIn = true;
        new Thread(refreshChats).start();
    }
  /**
 * This method handles sending and receiving messages from and to users,
 * it takes a message from a user, figure out who the recipient is, and forwards
 * it to its handler so it can take care of it
 * It also listens for incoming messages and forwards them for its corresponding user
 */
    @Override
    public void run() {
        while(isLoggedIn) {
            try {
                System.out.println("Receiving String");
                recipientString = inputStream.readUTF();
                System.out.println("Received String: " + recipientString);
                if(recipientString.equals("logout")) {
                    this.socket.close();
                    ClientHandler.instances--;
                    this.isLoggedIn = false;
                    
                    var user = dataManager.getUser(name);
                    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date now = new Date();
                    user.setLastLogin(dateFormat.format(now).toString());
                    dataManager.updateUser(name, user);
                    
                    break;
                }
                
                String recipientMessage = recipientString.split("###")[0];
                String recipientName = recipientString.split("###")[1];
                
                for(ClientHandler handler: Server.getClients()) {
                    if(handler.name.equals(recipientName)) {
                        handler.outputStream.writeUTF(this.name + "###" + recipientMessage);
                        break;
                    }
                }
                
            } catch(IOException e) {
                System.out.println("Could not connect to server");
                System.out.println(e.getMessage());
                break;
            }
        }
        
    }
    
    public synchronized String getRecipientMessage() {
        return recipientString;
    }
    
    public synchronized void setRecipientString(String recipientString) {
        this.recipientString = recipientString;
    }
    
    public String getClientName() {
        return this.name;
    }
    
    public static int getInstances() {
        return ClientHandler.instances;
    }
    
    public static void setInstances(int instances) {
        ClientHandler.instances = instances;
    }

 /**
 * This method is responsible for binding back disconnected handler upon user log out
 * so the need to create a new handler on each login is omitted
 */
    public void restartHandler (
        String name,
        Socket socket, 
        Socket chatsSocket,
        DataInputStream inputStream, 
        DataOutputStream outputStream,
        DataOutputStream chatsOutputStream
    ) {
        this.name = name;
        this.socket = socket;
        this.chatsSocket = chatsSocket;
        this.inputStream = inputStream;
        this.outputStream = outputStream;
        this.chatsOutputStream = chatsOutputStream;
        this.isLoggedIn = true;
        new Thread(refreshChats).start();
    }
            
}
