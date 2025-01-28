package com.estore.model.dao;
import com.estore.model.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO {
    private Connection connection;

    public ProductDAO(Connection connection) {
        this.connection = connection;
    }
    
    public List<Product> getAllProducts() throws SQLException {
        List<Product> products = new ArrayList<>();
        String query = "SELECT * FROM products";
        PreparedStatement statement = connection.prepareStatement(query);
        ResultSet resultSet = statement.executeQuery(); 
        
        	while (resultSet.next()) {
                products.add(new Product(
                    resultSet.getInt("product_id"),
                    resultSet.getString("name"),
                    resultSet.getDouble("price"),
                    resultSet.getString("category"),
                    resultSet.getString("image_url")
                ));
            } 
        return products;
    }
    
    // Method to fetch all products with pagination
    public List<Product> getAllProducts(int startIndex, int pageSize) throws SQLException {
        List<Product> products = new ArrayList<>();
        String query = "SELECT * FROM products LIMIT ?, ?";
        
        PreparedStatement ps = connection.prepareStatement(query);
        ps.setInt(1, startIndex);
        ps.setInt(2, pageSize);
        
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            products.add(new Product(
                rs.getInt("product_id"),
                rs.getString("name"),
                rs.getDouble("price"),
                rs.getString("category"),
                rs.getString("image_url")
            ));
        }

        return products;
    }

    
    public Product getProductById(int productId) throws SQLException {
    	
    	String query = "SELECT * FROM products where product_id=?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, productId);
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
        	return new Product(
        			resultSet.getInt("product_id"),
                    resultSet.getString("name"),
                    resultSet.getDouble("price"),
                    resultSet.getString("category"),
                    resultSet.getString("image_url")
        		);
        }
        return null;
        
    }
    
    
    // Method to fetch products based on search and category filter
    public List<Product> getProducts(String category, String searchQuery, int startIndex, int pageSize) throws SQLException {
        List<Product> products = new ArrayList<>();
        StringBuilder query = new StringBuilder("SELECT * FROM products WHERE 1=1");

        // Dynamically add filters
        if (category != null && !category.isEmpty()) {
            query.append(" AND category LIKE ?");
        }
        if (searchQuery != null && !searchQuery.isEmpty()) {
            query.append(" AND name LIKE ?");
        }

        query.append(" LIMIT ?, ?");
        PreparedStatement ps = connection.prepareStatement(query.toString());

        int paramIndex = 1;
        if (category != null && !category.isEmpty()) {
            ps.setString(paramIndex++, "%" + category + "%");
        }
        if (searchQuery != null && !searchQuery.isEmpty()) {
            ps.setString(paramIndex++, "%" + searchQuery + "%");
        }

        ps.setInt(paramIndex++, startIndex);
        ps.setInt(paramIndex, pageSize);

        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            products.add(new Product(
                rs.getInt("product_id"),
                rs.getString("name"),
                rs.getDouble("price"),
                rs.getString("category"),
                rs.getString("image_url")
            ));
        }

        return products;
    }

    

    // Method to get the total number of products for pagination based on category and search query
    public int getProductCount(String category, String searchQuery) throws SQLException {
        StringBuilder query = new StringBuilder("SELECT COUNT(*) FROM products WHERE 1=1");

        if (category != null && !category.isEmpty()) {
            query.append(" AND category LIKE ?");
        }
        if (searchQuery != null && !searchQuery.isEmpty()) {
            query.append(" AND name LIKE ?");
        }

        PreparedStatement ps = connection.prepareStatement(query.toString());

        int paramIndex = 1;
        if (category != null && !category.isEmpty()) {
            ps.setString(paramIndex++, "%" + category + "%");
        }
        if (searchQuery != null && !searchQuery.isEmpty()) {
            ps.setString(paramIndex++, "%" + searchQuery + "%");
        }

        ResultSet rs = ps.executeQuery();
        rs.next();
        return rs.getInt(1);
    }

    
    public int getProductCount() throws SQLException {
    	
    	String query = "SELECT COUNT(*) FROM products";
    	
    	PreparedStatement ps = connection.prepareStatement(query);
    	
    	 ResultSet rs = ps.executeQuery();
    	 rs.next();
         return rs.getInt(1);
    	 
    }

    // Method to fetch all unique categories from the products table
    public List<String> getCategories() throws SQLException {
        List<String> categories = new ArrayList<>();
        String query = "SELECT DISTINCT category FROM products";
        
        PreparedStatement ps = connection.prepareStatement(query);
        ResultSet rs = ps.executeQuery();
        
        while (rs.next()) {
            categories.add(rs.getString("category"));
        }
        
        return categories;
    }
}
