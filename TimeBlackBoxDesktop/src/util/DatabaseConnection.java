package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnection {

    // [OOP - Encapsulation]
    private static final String DB_URL = "jdbc:sqlite:timeblackbox.db";

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("org.sqlite.JDBC");
            return DriverManager.getConnection(DB_URL);
        } catch (ClassNotFoundException e) {
            System.err.println("SQLite JDBC Driver tidak ditemukan!");
            return null;
        }
    }

    public static void createTablesIfNotExist() {
        // [OOP - Abstraction]
        String sqlUsers = "CREATE TABLE IF NOT EXISTS users ("
                + "user_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "username TEXT UNIQUE NOT NULL, "
                + "password TEXT NOT NULL)";

        String sqlTimeBlocks = "CREATE TABLE IF NOT EXISTS time_blocks ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "user_id INTEGER, "
                + "activity_name TEXT, "
                + "duration INTEGER, " // dalam menit
                + "activity_type TEXT, " // Focus / Break
                + "productivity_score DOUBLE, " // Menggunakan Double agar presisi
                + "category TEXT, " // Kolom baru: Keterangan (Sangat Fokus, dll)
                + "created_at DATETIME DEFAULT CURRENT_TIMESTAMP, " // Penting untuk query harian
                + "FOREIGN KEY(user_id) REFERENCES users(user_id))";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sqlUsers);
            stmt.execute(sqlTimeBlocks);
            System.out.println("Database siap.");
        } catch (SQLException e) {
            System.out.println("Error inisialisasi DB: " + e.getMessage());
        }
    }
}