package com.estore.controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import com.estore.model.Cart;
import com.estore.model.Order;
import com.estore.model.User;
import com.estore.model.dao.CartDAO;
import com.estore.model.dao.OrderDAO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/OrderController")
public class OrderController extends HttpServlet {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private OrderDAO orderDAO;
	//private CartDAO cartDAO;
	//private Order order;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
	    User user = (User) session.getAttribute("user"); // Get logged-in user

	    if (user == null) {
	        response.sendRedirect("login.jsp"); // Redirect to login page if no user is logged in
	        return;
	    }

	    try {
            	DataSource dataSource = (DataSource) new InitialContext().lookup("java:/comp/env/jdbc/MyDB");
            	Connection connection = dataSource.getConnection();
                orderDAO = new OrderDAO(connection);
                List<Order> orders = new ArrayList<>(); 
                orders = orderDAO.getOrdersByUserId(user.getUserId());
           
                
                request.setAttribute("orders", orders); // Set orders as a request attribute
                request.getRequestDispatcher("order.jsp").forward(request, response); // Forward to JSP to display orders
            
            } catch (NamingException | SQLException e) {
                request.setAttribute("error", "Error 404");
                request.getRequestDispatcher("/error.jsp").forward(request, response);
            }
        }

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    HttpSession session = request.getSession();
	    User user = (User) session.getAttribute("user");

	    if (user == null) {
	        response.sendRedirect("login.jsp");
	        return;
	    }

	    String action = request.getParameter("action");
	    Connection connection = null;

	    try {
	        DataSource dataSource = (DataSource) new InitialContext().lookup("java:/comp/env/jdbc/MyDB");
	        connection = dataSource.getConnection();
	        orderDAO = new OrderDAO(connection);

	        if ("cancelOrder".equals(action)) {
	            int orderId = Integer.parseInt(request.getParameter("orderId"));
	            orderDAO.deleteOrderById(orderId);

	            // Refresh user's orders after cancellation
	            List<Order> orders = orderDAO.getOrdersByUserId(user.getUserId());
	            request.setAttribute("orders", orders);
	            // Forward the updated order to order.jsp
	            request.getRequestDispatcher("order.jsp").forward(request, response);
	            return;
	        }
	    } catch(Exception e) {
	    	  request.setAttribute("error", "Unable to cancel order");
	          request.getRequestDispatcher("order.jsp").forward(request, response);
	    } finally {
	        if (connection != null) {
	            try {
	                connection.close();
	            } catch (SQLException e) {
	                e.printStackTrace();
	            }
	        }
	    }
	}
}