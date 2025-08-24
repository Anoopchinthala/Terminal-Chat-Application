
package com.chatapp;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Database {
    private static final String URL = "jdbc:mysql://localhost:3306/chat_app";
    private static final String USER = "root";          // change if needed
    private static final String PASSWORD = "********"; // your MySQL password
    private Connection connection;

    public Database() {
        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("✅ Connected to MySQL database.");
        } catch (SQLException e) {
            System.err.println("❌ Failed to connect to MySQL: " + e.getMessage());
        }
    }

    // Save a new message in DB
    public void saveMessage(String sender, String message) {
        String sql = "INSERT INTO messages (sender, message) VALUES (?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, sender);
            stmt.setString(2, message);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("❌ Error saving message: " + e.getMessage());
        }
    }

    // Fetch last N messages for chat history
    public List<String> getRecentMessages(int limit) {
        List<String> messages = new ArrayList<>();
        String sql = "SELECT sender, message, timestamp FROM messages ORDER BY timestamp DESC LIMIT ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, limit);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String msg = "[" + rs.getTimestamp("timestamp") + "] "
                           + rs.getString("sender") + ": "
                           + rs.getString("message");
                messages.add(msg);
            }
        } catch (SQLException e) {
            System.err.println("❌ Error fetching history: " + e.getMessage());
        }
        return messages;
    }

    // Close DB connection
    public void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            System.err.println("❌ Error closing DB connection: " + e.getMessage());
        }
    }
}
