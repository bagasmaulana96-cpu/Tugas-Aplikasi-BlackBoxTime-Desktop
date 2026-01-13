package model;

public class User {
    // [OOP - Encapsulation]
    // Field dideklarasikan sebagai private untuk menyembunyikan data internal (Information Hiding).
    // Hanya bisa diakses melalui method getter/setter.
    private int userId;
    private String username;
    private String password;
    
    public User() {}
    
    public User(int userId, String username, String password) {
        this.userId = userId;
        this.username = username;
        this.password = password;
    }
    
    // [OOP - Encapsulation]
    // Public method untuk memberikan akses terkontrol ke properti private.
    public int getUserId() {
        return userId;
    }
    
    public void setUserId(int userId) {
        this.userId = userId;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
        
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
}