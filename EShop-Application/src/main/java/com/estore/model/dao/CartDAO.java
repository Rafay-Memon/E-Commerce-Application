package com.estore.model.dao;

import java.sql.*;
import java.util.*;

import com.estore.model.Cart;
import com.estore.model.Product;

public class CartDAO {
    private Connection connection;
    private ProductDAO productDAO;

    public CartDAO(Connection connection, ProductDAO productDAO) {
        this.connection = connection;
        this.productDAO = productDAO;
    }

    public CartDAO(Connection connection) {
        this.connection = connection;
    }

    // Get cart items for a user
    public List<Cart> getCartItemsByUserId(int userId) throws SQLException {
        List<Cart> cartItems = new ArrayList<>();
        String sql = "SELECT c.cart_id, c.user_id, c.product_id, c.quantity, p.category, p.name, p.price, p.image_url " +
                     "FROM cart c " +
                     "JOIN products p ON c.product_id = p.product_id " +
                     "WHERE c.user_id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                cartItems.add(new Cart(
                    rs.getInt("cart_id"),
                    rs.getInt("user_id"),
                    rs.getInt("product_id"),
                    rs.getInt("quantity"),
                    new Product(rs.getString("name"), rs.getDouble("price"), rs.getString("category"), rs.getString("image_url"))
                ));
            }
        }
        return cartItems;
    }

    // Add item to cart or update if it already exists
    public void addItemToCart(int userId, int productId) throws SQLException {
        // First, check if the product already exists in the user's cart
        String checkSql = "SELECT cart_id, quantity FROM cart WHERE user_id = ? AND product_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(checkSql)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, productId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                // Product exists, so update the quantity
                int cartId = rs.getInt("cart_id");
                int currentQuantity = rs.getInt("quantity");
                int newQuantity = currentQuantity + 1;
                updateCartQuantity(cartId, newQuantity);
            } else {
                // Product doesn't exist, so insert a new record
                String insertSql = "INSERT INTO cart (user_id, product_id, quantity, total_price) VALUES (?, ?, ?, ?)";
                try (PreparedStatement insertStmt = connection.prepareStatement(insertSql)) {
                    Product product = productDAO.getProductById(productId);
                    double totalPrice = product.getPrice(); // Set the initial price based on the quantity (1)
                    insertStmt.setInt(1, userId);
                    insertStmt.setInt(2, productId);
                    insertStmt.setInt(3, 1); // Initial quantity is 1
                    insertStmt.setDouble(4, totalPrice);
                    insertStmt.executeUpdate();
                }
            }
        }
    }

    // Calculate total price of the cart
    public double calculateTotalPrice(int userId) throws SQLException {
        double total = 0;
        String sql = "SELECT SUM(c.quantity * p.price) AS total FROM cart c " +
                     "JOIN products p ON c.product_id = p.product_id WHERE c.user_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                total = rs.getDouble("total");
            }
        }
        return total;
    }

    // Update quantity of cart item
    public void updateCartQuantity(int cartId, int quantity) throws SQLException {
        String sql = "UPDATE cart SET quantity = ? WHERE cart_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, quantity);
            stmt.setInt(2, cartId);
            stmt.executeUpdate();
        }
    }

    // Remove a cart item
    public void removeCartItem(int cartId) throws SQLException {
        String sql = "DELETE FROM cart WHERE cart_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, cartId);
            stmt.executeUpdate();
        }
    }

    // Clear the cart after the order is placed
    public void clearCart(int userId) throws SQLException {
        String sql = "DELETE FROM cart WHERE user_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.executeUpdate();
        }
    }
}
