package com.estore.model.dao;

import com.estore.model.Order;
import com.estore.model.Product;
import com.estore.model.Cart;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderDAO {
    private Connection connection;

    public OrderDAO(Connection connection) {
        this.connection = connection;
    }

    public boolean createOrder(Order order) throws SQLException {
        String query = "INSERT INTO orders (user_id, name, email, address, phone, total_price, status, order_date) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            // Insert the order details
            stmt.setInt(1, order.getUserId());
            stmt.setString(2, order.getName());
            stmt.setString(3, order.getEmail());
            stmt.setString(4, order.getAddress());
            stmt.setString(5, order.getPhone());
            stmt.setDouble(6, order.getTotalPrice());
            stmt.setString(7, order.getStatus() == null ? "Pending" : order.getStatus());
            stmt.setTimestamp(8, new Timestamp(order.getOrderDate().getTime()));

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        int orderId = rs.getInt(1); // Get the generated order ID

                        // Insert order items for this order
                        createOrderItems(orderId, order.getCartItems());

                        return true; // Return true if insertion happened successfully
                    }
                }
            }
        }
        return false; // Return false if insertion fails
    }

    public void createOrderItems(int orderId, List<Cart> cartItems) throws SQLException {
        String query = "INSERT INTO order_items (order_id, product_id, quantity, total_price) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            for (Cart cartItem : cartItems) {
                stmt.setInt(1, orderId); // Order ID
                stmt.setInt(2, cartItem.getProductId()); // Product ID
                stmt.setInt(3, cartItem.getQuantity()); // Quantity
                stmt.setDouble(4, cartItem.getTotalPrice()); // Total Price
                stmt.addBatch(); // Add to batch
            }
            stmt.executeBatch(); // Execute all inserts in batch
        }
    }

    public void updateOrderStatus(int orderId, String status) throws SQLException {
        String query = "UPDATE orders SET status = ? WHERE order_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, status);
            stmt.setInt(2, orderId);
            stmt.executeUpdate();
        }
    }

    public List<Order> getOrdersByUserId(int userId) throws SQLException {
        List<Order> orders = new ArrayList<>();
        Map<Integer, Order> orderMap = new HashMap<>();

        String query = "SELECT o.order_id, o.order_date, o.status, o.total_price, " +
                       "oi.product_id, oi.quantity, oi.total_price AS item_total_price, " +
                       "p.name AS product_name, p.category, p.image_url, p.price AS product_price " +
                       "FROM orders o " +
                       "JOIN order_items oi ON o.order_id = oi.order_id " +
                       "JOIN products p ON oi.product_id = p.product_id " +
                       "WHERE o.user_id = ? " +
                       "ORDER BY o.order_id";
        
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int orderId = rs.getInt("order_id");
                    
                    // Create or retrieve existing order
                    Order order = orderMap.get(orderId);
                    if (order == null) {
                        order = new Order();
                        order.setOrderId(orderId);
                        order.setOrderDate(rs.getTimestamp("order_date"));
                        order.setStatus(rs.getString("status"));
                        orderMap.put(orderId, order);
                        order.setTotalPrice(rs.getDouble("total_price"));
                        orders.add(order);
                    }

                    // Create cart item
                    Cart cartItem = new Cart();
                    Product product = new Product();
                    product.setProductId(rs.getInt("product_id"));
                    product.setName(rs.getString("product_name"));
                    product.setCategory(rs.getString("category"));
                    product.setImageUrl(rs.getString("image_url"));
                    product.setPrice(rs.getDouble("product_price"));
                    
                    cartItem.setProduct(product);
                    cartItem.setQuantity(rs.getInt("quantity"));
                    cartItem.setTotalPrice(rs.getDouble("total_price"));

                    order.addCartItem(cartItem);
                }
            }
        }
        return orders;
    }

    public List<Cart> getOrderItemsByOrderId(int orderId) throws SQLException {
        List<Cart> cartItems = new ArrayList<>();
        String query = "SELECT oi.product_id, oi.quantity, oi.total_price, p.name, p.price, p.category, p.image_url " +
                       "FROM order_items oi " +
                       "JOIN products p ON oi.product_id = p.product_id " +
                       "WHERE oi.order_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, orderId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Product product = new Product(rs.getInt("product_id"), rs.getString("name"), rs.getDouble("price"),
                                                  rs.getString("category"), rs.getString("image_url"));

                    Cart cart = new Cart();
                    cart.setProduct(product);
                    cart.setQuantity(rs.getInt("quantity"));
                    cart.setTotalPrice(rs.getDouble("total_price"));
                    cartItems.add(cart);
                }
            }
        }
        return cartItems;
    }

    public void deleteOrderById(int orderId) throws SQLException {
        String deleteOrder = "DELETE FROM orders WHERE order_id = ?";
        String deleteItems = "DELETE FROM order_items WHERE order_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(deleteItems)) {
            stmt.setInt(1, orderId);
            stmt.executeUpdate();
        }

        try (PreparedStatement stmt = connection.prepareStatement(deleteOrder)) {
            stmt.setInt(1, orderId);
            stmt.executeUpdate();
        }
    }
}
