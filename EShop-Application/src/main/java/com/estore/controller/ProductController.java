package com.estore.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.InitialContext;
import javax.sql.DataSource;

import com.estore.model.Product;
import com.estore.model.dao.ProductDAO;

@WebServlet("/ProductController")
public class ProductController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public ProductController() {
        super();
    }

    private Connection connection;
    private ProductDAO productDAO;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Initialize variables
        DataSource dataSource = null;
        List<Product> products = new ArrayList<>();
        int totalProducts = 0;
        int totalPages = 0;
        Connection connection = null;

        try {
            // Retrieve connection from DataSource
            dataSource = (DataSource) new InitialContext().lookup("java:/comp/env/jdbc/MyDB");
            connection = dataSource.getConnection();
            productDAO = new ProductDAO(connection); // Pass connection to DAO

            // Get parameters and default them if necessary
            String selectedCategory = getRequestParameter(request, "category", "");
            String searchQuery = getRequestParameter(request, "searchQuery", "");
            int page = getPageNumber(request);
            int pageSize = 6;

            // Calculate start index for pagination
            int startIndex = (page - 1) * pageSize;

            // Fetch products and total count based on the filters
            if (!selectedCategory.isEmpty() || !searchQuery.isEmpty()) {
                products = productDAO.getProducts(selectedCategory, searchQuery, startIndex, pageSize);
                totalProducts = productDAO.getProductCount(selectedCategory, searchQuery);
            } else {
                products = productDAO.getAllProducts(startIndex, pageSize);
                totalProducts = productDAO.getProductCount();
            }

            // Calculate total pages
            totalPages = (int) Math.ceil((double) totalProducts / pageSize);

            // Fetch all unique categories for the dropdown menu
            List<String> categories = productDAO.getCategories();

            // Set attributes for the view
            setRequestAttributes(request, products, totalPages, page, selectedCategory, searchQuery, categories);

            // Forward to the product.jsp page
            request.getRequestDispatcher("/product.jsp").forward(request, response);
        } catch (Exception e) {
            // Log the error with a meaningful message
            log("Error in ProductController.doGet", e);
            response.sendRedirect("error.jsp");
        } finally {
            // Ensure resources are cleaned up
            closeConnection(connection);
        }
    }

    private String getRequestParameter(HttpServletRequest request, String param, String defaultValue) {
        String value = request.getParameter(param);
        return (value == null || value.isEmpty()) ? defaultValue : value;
    }

    private int getPageNumber(HttpServletRequest request) {
        try {
            String pageParam = request.getParameter("page");
            return (pageParam != null && !pageParam.isEmpty()) ? Integer.parseInt(pageParam) : 1;
        } catch (NumberFormatException e) {
            return 1; // Default to the first page on error
        }
    }

    private void setRequestAttributes(HttpServletRequest request, List<Product> products, int totalPages,
                                      int currentPage, String selectedCategory, String searchQuery, List<String> categories) {
        request.setAttribute("products", products);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("currentPage", currentPage);
        request.setAttribute("category", selectedCategory);
        request.setAttribute("searchQuery", searchQuery);
        request.setAttribute("categories", categories);
    }

    private void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                log("Error closing connection", e);
            }
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}


