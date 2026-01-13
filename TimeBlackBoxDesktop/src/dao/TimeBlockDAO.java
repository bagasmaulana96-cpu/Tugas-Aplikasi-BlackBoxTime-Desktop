package dao;

import model.TimeBlock;
import model.FocusSession;
import util.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TimeBlockDAO {

    public void saveTimeBlock(int userId, TimeBlock timeBlock) {
        // [OOP - Polymorphism] 
        String sql = "INSERT INTO time_blocks (user_id, activity_name, duration, activity_type, productivity_score, category) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            stmt.setString(2, timeBlock.getName());
            stmt.setInt(3, timeBlock.getDuration());
            
            // Menentukan tipe string berdasarkan class instance
            String type = (timeBlock instanceof FocusSession) ? "Focus" : "Break";
            stmt.setString(4, type);
            
            stmt.setDouble(5, timeBlock.calculateProductivityScore());
            stmt.setString(6, timeBlock.getCategoryLabel());

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Mengambil total poin HARI INI (00:00 - 23:59)
    public double getTodayTotalScore(int userId) {
        // Query SQL menggunakan DATE('now', 'localtime') untuk filter hari ini
        String sql = "SELECT SUM(productivity_score) as total FROM time_blocks " +
                     "WHERE user_id = ? AND date(created_at, 'localtime') = date('now', 'localtime')";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getDouble("total");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0.0;
    }

    public List<String[]> getTodayHistory(int userId) {
        List<String[]> history = new ArrayList<>();
        String sql = "SELECT activity_name, duration, category, productivity_score FROM time_blocks " +
                     "WHERE user_id = ? AND date(created_at, 'localtime') = date('now', 'localtime') " +
                     "ORDER BY created_at DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                String[] row = {
                    rs.getString("activity_name"),
                    rs.getString("duration") + " min",
                    rs.getString("category"),
                    String.format("%.2f", rs.getDouble("productivity_score"))
                };
                history.add(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return history;
    }
}