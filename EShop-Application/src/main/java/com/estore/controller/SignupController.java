package com.estore.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.Connection;

import javax.naming.InitialContext;
import javax.sql.DataSource;

import com.estore.model.User;
import com.estore.model.dao.UserDAO;

/**
 * Servlet implementation class SignupController
 */
@WebServlet("/SignupController")
public class SignupController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Connection connection;
	private UserDAO userDAO;
   
    public SignupController() {
        super();
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		/// Retrieve connection from DataSource
        DataSource dataSource;
		try {
			dataSource = (DataSource) new InitialContext().lookup("java:/comp/env/jdbc/MyDB");
			connection = dataSource.getConnection();
			
            userDAO = new UserDAO(connection);  // Pass the connection to the UserDAO constructor
            
            String username = request.getParameter("username");
            String email = request.getParameter("email");
            String password = request.getParameter("password");
            
            // Check if the email already exists
            if (userDAO.checkEmailExists(email)) {
                // Email exists, forward back to signup page with error message
                request.setAttribute("errorMessage", "Email already registered. Please use a different email.");
                request.getRequestDispatcher("/signup.jsp").forward(request, response);
                return;
            }
            
            

            User user = new User(username, email, password);

            if (userDAO.register(user)) {
            	response.sendRedirect("login.jsp?message=Registration successful. Please login.");
            } else {
                request.setAttribute("errorMessage", "Failed to register. Try again.");
                request.getRequestDispatcher("/signup.jsp").forward(request, response);
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
