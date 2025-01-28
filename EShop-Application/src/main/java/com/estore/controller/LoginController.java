package com.estore.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.Connection;
import java.util.List;

import javax.naming.InitialContext;
import javax.sql.DataSource;

import com.estore.model.Cart;
import com.estore.model.User;
import com.estore.model.dao.CartDAO;
import com.estore.model.dao.UserDAO;

/**
 * Servlet implementation class LoginController
 */
@WebServlet("/LoginController")
public class LoginController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Connection connection;
	private UserDAO userDAO;
       
   
    public LoginController() {
        super();
    }

	

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		/// Retrieve connection from DataSource
        DataSource dataSource;
		try {
			dataSource = (DataSource) new InitialContext().lookup("java:/comp/env/jdbc/MyDB");
			connection = dataSource.getConnection();
			 // Validate the email address
            userDAO = new UserDAO(connection);  // Pass the connection to the UserDAO constructor
            
            String email = request.getParameter("email");
            String password = request.getParameter("password");
            
            User user = userDAO.login(email, password);
            if (user != null) {
                HttpSession session = request.getSession();
                session.setAttribute("user", user);
                // Fetch cart count for the user
                CartDAO cartDAO = new CartDAO(connection);
                List<Cart> cartItems = cartDAO.getCartItemsByUserId(user.getUserId());
                session.setAttribute("cartCount", cartItems.size()); // Set cart count in session
                
                response.sendRedirect("welcome.jsp?message=Login successful");
            } else {
                request.setAttribute("errorMessage", "Invalid email or password.");
                request.getRequestDispatcher("/login.jsp").forward(request, response);
            }
		}
		catch(Exception e) {
			e.printStackTrace();
			response.sendRedirect("error.jsp");
		}
		
		finally {
            try {
                if (connection != null) connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
	}

}
