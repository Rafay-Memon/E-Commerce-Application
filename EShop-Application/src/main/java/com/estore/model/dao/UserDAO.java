package com.estore.model.dao;

import com.estore.model.User;
import java.sql.*;
import jakarta.mail.*;
import jakarta.mail.internet.*;
import java.util.*;

public class UserDAO {
    private Connection connection;

    public UserDAO(Connection connection) {
        this.connection = connection;
    }

    // Login method...
    public User login(String email, String password) throws SQLException {
        String query = "SELECT * FROM users WHERE email = ? AND password = BINARY ?";
        PreparedStatement ps = connection.prepareStatement(query);
        ps.setString(1, email);
        ps.setString(2, password);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            User user = new User(
                rs.getInt("user_id"),
                rs.getString("username"),
                rs.getString("email"),
                rs.getString("password")
            );
            return user;
        }
        return null;
    }

    // Register method...
    public boolean register(User user) throws SQLException {
        String query = "INSERT INTO users (username, email, password) VALUES (?, ?, ?)";
        PreparedStatement ps = connection.prepareStatement(query);
        ps.setString(1, user.getUsername());
        ps.setString(2, user.getEmail());
        ps.setString(3, user.getPassword());
        return ps.executeUpdate() > 0;
    }

    
    //Password-Recorvery Logic
    public boolean checkEmailExists(String email) throws SQLException {
        String query = "SELECT COUNT(*) FROM users WHERE email = ?";
        PreparedStatement ps = connection.prepareStatement(query);
        ps.setString(1, email);
        ResultSet rs = ps.executeQuery();
        rs.next();
        return rs.getInt(1) > 0;
    }

    public String generateResetToken(String email) throws SQLException {
        String token = UUID.randomUUID().toString();  // Generate a unique token
        String query = "UPDATE users SET reset_token = ? WHERE email = ?";
        PreparedStatement ps = connection.prepareStatement(query);
        ps.setString(1, token);
        ps.setString(2, email);
        int rowsUpdated = ps.executeUpdate();
        
        if (rowsUpdated > 0) {
            return token;
        }
        return null;
    }

    public boolean validateResetToken(String token) throws SQLException {
        String query = "SELECT COUNT(*) FROM users WHERE reset_token = ?";
        PreparedStatement ps = connection.prepareStatement(query);
        ps.setString(1, token);
        ResultSet rs = ps.executeQuery();
        rs.next();
        return rs.getInt(1) > 0;
    }

    public boolean updatePasswordByToken(String token, String newPassword) throws SQLException {
        String query = "UPDATE users SET password = ?, reset_token = NULL WHERE reset_token = ?";
        PreparedStatement ps = connection.prepareStatement(query);
        ps.setString(1, newPassword);
        ps.setString(2, token);
        int rowsUpdated = ps.executeUpdate();
        
        return rowsUpdated > 0;
    }
}
