/*
 * 
 */
package chatapplication;

import java.io.InputStream;

/**
 * This class represents a mirror of the user object stored in the database
 * so it can be used with this java application
 * @author Hsen
 */
public class UserDataObject {
    private String username;
    private String password;
    private String age;
    private InputStream photo;
    private String lastLogin;
    
    public UserDataObject() {
        
    }
    
    public UserDataObject(
            String username,
            String password,
            String age,
            InputStream photo,
            String lastLogin
    ){
        this.username = username;
        this.password = password;
        this.photo = photo;
        this.age = age;
        this.lastLogin = lastLogin;
    }
    
    public String getUsername() {
        return this.username;
    }
    
    public String getPassword() {
        return this.password;
    }
    
    public String getAge() {
        return this.age.toString();
    }
    
    public InputStream getPhoto() {
        return this.photo;
    }
    
    public String getLastLogin() {
        return this.lastLogin;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public void setAge(String age) {
        this.age = age;
    }
    
    public void setPhoto(InputStream photo) {
        this.photo = photo;
    }
    
    public void setLastLogin(String lastLogin) {
        this.lastLogin = lastLogin;
    }
    
    @Override
    public String toString() {
        var builder = new StringBuilder();
        builder = builder.append("Username: ").append(this.username).append("\n")
                .append("password: ").append(this.password).append("\n")
                .append("age: ").append(this.age).append("\n")
                .append("photo: ").append(this.photo).append("\n")
                .append("last login: ").append(this.lastLogin).append("\n");
        return builder.toString();
    }
 }
