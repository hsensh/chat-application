/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatapplication;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;
import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;
import javax.swing.JOptionPane;

/**
 * This is the central server, it is responsible for integrating all the clients
 * and client handlers as well as all the requests and responses with each other
 * It is a hub for receiving requests marked with certain flags so it knows what
 * sort of data it is going to handle, and handling them using string parsing and
 * the data objects and managers connected to the databases as well as sockets and
 * handlers so it ensures a good management center for the app
 * @author Hsen
 */
public class Server {
    public static final int port = 4545;
    private static ServerSocket serverSocket;
    private static Vector<ClientHandler> clients = new Vector();
    private static UserDataManager userDataManager = new UserDataManager();
    private static ConversationDataManager convDataManager = new ConversationDataManager();
    private static Runnable initiateHandler;
    private static Thread initiateHandlerThread;
    private static boolean refreshChats;
    
    public static void main(String[] args) {
        try {
            serverSocket = new ServerSocket(port);
            Socket socket;
            Socket chatsSocket;
            String fetchedUser = "";
            while(true) {
                socket = serverSocket.accept();
                System.out.println("New client connection request");
                DataInputStream inputStream = new DataInputStream(socket.getInputStream());
                DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
                System.out.println("Attempting to read request");
                String requestStream = inputStream.readUTF();
                System.out.println("inputStream: " + requestStream);
                if(requestStream.contains("loginStream:::")) {
                    System.out.println("Attempting to connect client");
                    System.out.println("Creating handler");
                    System.out.println("Fetching Credentials");
                    String loginStream = requestStream.replace("loginStream:::", "");
                    String credentials[] = loginStream.split("###");
                    var user = userDataManager.getUser(credentials[0]);
                    if(user != null) {
                        if(user.getPassword().equals(credentials[1])) {
                            outputStream.writeUTF("success");
                            chatsSocket = serverSocket.accept();
                            DataOutputStream chatsOutputStream = new DataOutputStream(chatsSocket.getOutputStream());
                            boolean found = false;
                            for(ClientHandler client: getClients()) {
                                
                                if(client.getClientName().equals(credentials[0])) {
                                    System.out.println("Found existing connection");
                                    found = true;
                                    System.out.println("Binding connection");
                                    client.restartHandler(
                                            credentials[0],
                                            socket, 
                                            chatsSocket, 
                                            inputStream, 
                                            outputStream, 
                                            chatsOutputStream
                                    );
                                    initiateHandler = new Thread() {
                                        @Override
                                        public void run() {
                                            ClientHandler.setInstances(
                                            ClientHandler.getInstances() + 1
                                            );
                                            System.out.println("Logging client in");
                                            new Thread(client).start();
                                        }
                                    };
                                    System.out.println("Binding successful");
                                    break;
                                }
                                
                            }
                            if(!found) {
                                System.out.println("No existing connection found");
                                System.out.println("Creating new connection");
                                var handler = new ClientHandler(
                                    credentials[0], 
                                    socket, 
                                    chatsSocket, 
                                    inputStream, 
                                    outputStream, 
                                    chatsOutputStream
                                );
                                initiateHandler = new Runnable() {
                                    @Override
                                    public void run() {
                                        ClientHandler.setInstances(
                                                ClientHandler.getInstances() + 1
                                        );
                                        System.out.println("Logging client in");
                                        clients.add(handler);
                                        System.out.println("Client " + handler.getClientName() + " has been created");
                                        new Thread(handler).start();
                                    }
                                };
                                System.out.println("Connection successfully created");
                            }
                            System.out.println("Starting connection...");
                            initiateHandlerThread = new Thread(initiateHandler);
                            initiateHandlerThread.start();
                            initiateHandlerThread.join();
                        } else {
                            outputStream.writeUTF("failed");
                        }
                    } else {
                        outputStream.writeUTF("failed");
                    }
                } else if(requestStream.contains("registerCreateStream:::")) {
                    
                    System.out.println("Registration connection request");
                    System.out.println("Attempting to register client");
                    Socket imageSocket = serverSocket.accept();
                    var bufferedImage = ImageIO.read(imageSocket.getInputStream());
                    System.out.println(bufferedImage);
                    imageSocket.close();
                    // convert buffered image into an input stream
                    ByteArrayOutputStream os = new ByteArrayOutputStream();
                    ImageIO.write(bufferedImage, "jpg", os);
                    InputStream image = new ByteArrayInputStream(os.toByteArray());
                    
                    
                    String registerStream = requestStream.replace("registerCreateStream:::", "");
                    String userInfo[] = registerStream.split("###");
                    if(!userDataManager.userExists(userInfo[0])) {
                        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        Date now = new Date();

                        var user = new UserDataObject(
                                userInfo[0],
                                userInfo[1],
                                userInfo[2],
                                image,
                                dateFormat.format(now).toString()
                        );
                        userDataManager.createUser(user);
                        System.out.println("User " + userInfo[0] + " has been successfully registered");
                        outputStream.writeUTF("success");
                    } else {
                        outputStream.writeUTF("usernamefailed");
                    }
                } else if(requestStream.contains("registerUpdateStream:::")) {
                    System.out.println("Update connection request");
                    System.out.println("Attempting to update client");
                    Socket imageSocket = serverSocket.accept();
                    var bufferedImage = ImageIO.read(imageSocket.getInputStream());
                    imageSocket.close();
                    System.out.println(bufferedImage);
                    
                    // convert buffered image into an input stream
                    ByteArrayOutputStream os = new ByteArrayOutputStream();
                    ImageIO.write(bufferedImage, "jpg", os);
                    InputStream image = new ByteArrayInputStream(os.toByteArray());
                    
                    String registerStream = requestStream.replace("registerUpdateStream:::", "");
                    String userInfo[] = registerStream.split("###");
                    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date now = new Date();
                    System.out.println(userInfo[0]);
                    var user = new UserDataObject(
                            userInfo[0],
                            userInfo[1],
                            userInfo[2],
                            image,
                            dateFormat.format(now).toString()
                    );
                    userDataManager.updateUser(fetchedUser, user);
                    System.out.println("User " + userInfo[0] + " has been successfully updated");
                    outputStream.writeUTF("success");
                } else if(requestStream.contains("conversationStream:::")) {
                    fetchedUser = requestStream.replace("conversationStream:::", "");
                    var convs = convDataManager.getConversations(fetchedUser);
                    var builder = new StringBuilder();
                    for(ConversationDataObject conv: convs) {
                        builder.append(conv.getRecipient()).append("##")
                                .append(conv.getConversation()).append("###");
                    }
                    System.out.println("Sending stream to client");
                    outputStream.writeUTF(builder.toString());
                    System.out.println("Stream successfully sent");
                } else if(requestStream.contains("updateConversationStream:::")) {
                    String updateConvStream[] = requestStream
                            .replace("updateConversationStream:::", "")
                            .split(">>>");
                    var username = updateConvStream[0];
                    String convStream[] = updateConvStream[1].split("###");
                    var conv = new ConversationDataObject();
                    var reverseConv = new ConversationDataObject();
                    conv.setUsername(username);
                    reverseConv.setRecipient(username);
                    for(String convInfo: convStream) {
                        var convRecipient = convInfo.split("##")[0];
                        var convConversation = convInfo.split("##")[1];
                        var convReverseConversation = convInfo.split("##")[1]
                                .replace("Me:", "temp:")
                                .replace(convRecipient + ":", "Me:")
                                .replace("temp:", username + ":");
                        conv.setRecipient(convRecipient);
                        conv.setConversation(convConversation);
                        reverseConv.setUsername(convRecipient);
                        reverseConv.setConversation(convReverseConversation);
                        boolean found = false;
                        boolean reverseFound = false;
                        for(ConversationDataObject oldConv: convDataManager.getConversations(username)) {
                            if(oldConv.getRecipient().equals(convRecipient)) {
                                found = true;
                                convDataManager.updateConversation(conv);
                                break;  
                            }
                        }
                        for(ConversationDataObject oldConv: convDataManager.getConversations(convRecipient)) {
                            if(oldConv.getRecipient().equals(username)) {
                                reverseFound = true;
                                convDataManager.updateConversation(reverseConv);
                                break;
                            }
                        }
                        if(!found) {
                            convDataManager.createConversation(conv);
                            refreshChats = true;
                        }
                        
                        if(!reverseFound) {
                            convDataManager.createConversation(reverseConv);
                            refreshChats = true;
                        }
                            
                            
                    }
                } else if(requestStream.contains("userInfoStream:::")) {
                    fetchedUser = requestStream.replace("userInfoStream:::", "");
                    var imageSocket = serverSocket.accept();
                    var imageOutputStream = new DataOutputStream(imageSocket.getOutputStream());
                    var user = userDataManager.getUser(fetchedUser);
                    var builder = new StringBuilder();
                    builder
                            .append(user.getUsername()).append("###")
                            .append(user.getPassword()).append("###")
                            .append(user.getAge());
                    System.out.println("Initiating update for client");
                    System.out.println(builder.toString());
                    outputStream.writeUTF(builder.toString());
                    System.out.println("user info sent");
                    System.out.println(user.getPhoto().available());
                    imageOutputStream.write(user.getPhoto().readAllBytes());
                    imageSocket.close();
                    System.out.println("user image sent");
                }
            }
        } catch(IOException e) {
            System.out.println("Could not communicate with server");
            System.out.println(e.getMessage());
            System.out.println(e.getStackTrace());
            System.out.println(e.getCause());
        } catch(InterruptedException e) {
            
        }
    }
    
    public static Vector<ClientHandler> getClients() {
        return clients;
    }
    
    public static Thread getHandlerThread() {
        return Server.initiateHandlerThread;
    }
    
    public static boolean getRefreshChats() {
        return Server.refreshChats;
    }
    
    public static void setRefreshChats(boolean refreshChats) {
        Server.refreshChats = refreshChats;
    }
    
}
