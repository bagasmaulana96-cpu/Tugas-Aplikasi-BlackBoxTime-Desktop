package dao;

import model.User;
import util.DatabaseConnection;

import java.sql.*;

public class UserDAO {
    
    // Register user baru
    public boolean registerUser(User user) {
        String sql = "INSERT INTO users (username, password) VALUES (?, ?)";
    
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
        
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword()); // Note: Sebaiknya di-hash dulu (bcrypt)
        
            int rows = stmt.executeUpdate();
            return rows > 0;
        
        } catch (SQLException e) {
            System.err.println("Error saat register: " + e.getMessage());
            return false;
        }
    }
    
    // Login user
    public User loginUser(String username, String password) {
    String sql = "SELECT user_id, username FROM users WHERE username = ? AND password = ?";
    
    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
        
        stmt.setString(1, username);
        stmt.setString(2, password);
        
        ResultSet rs = stmt.executeQuery();
        
        if (rs.next()) {
            return new User(
                rs.getInt("user_id"),
                rs.getString("username"),
                null  // email diset null karena tidak dipakai
            );
        }
        
        } catch (SQLException e) {
        System.err.println("Error saat login: " + e.getMessage());
    }
    
    return null;
    }
    
    // Cek apakah username sudah ada
    public boolean isUsernameExists(String username) {
        String sql = "SELECT COUNT(*) FROM users WHERE username = ?";
    
        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
        
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
        
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        
        } catch (SQLException e) {
            System.err.println("Error cek username: " + e.getMessage());
        }
    
        return false;
    }
}