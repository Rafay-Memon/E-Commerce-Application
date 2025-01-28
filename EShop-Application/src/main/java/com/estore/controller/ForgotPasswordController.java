package com.estore.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import com.estore.model.dao.UserDAO;

/**
 * Servlet implementation class ForgotPasswordController
 */
@WebServlet("/ForgotPasswordController")
public class ForgotPasswordController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private UserDAO userDAO;
	private Connection connection;

	public ForgotPasswordController() {
		super();
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email");

        // Retrieve connection from DataSource
        DataSource dataSource;
		try {
			dataSource = (DataSource) new InitialContext().lookup("java:/comp/env/jdbc/MyDB");
			connection = dataSource.getConnection();
			 // Validate the email address
            userDAO = new UserDAO(connection);  // Pass the connection to the UserDAO constructor
            boolean emailExists = userDAO.checkEmailExists(email);

            if (emailExists) {
            	 // Generate a reset token and store it in the database
                String token = userDAO.generateResetToken(email);
                
                // Redirect the user to the reset-password.jsp page with the token
                response.sendRedirect("reset-password.jsp?token=" + token);
            } else {
                request.setAttribute("errorMessage", "No account found with this email.");
                request.getRequestDispatcher("forgot-password.jsp").forward(request, response);
            }
		} catch (SQLException | NamingException e) {
			e.printStackTrace();
			 request.setAttribute("errorMessage", "Error occurred.");
	         request.getRequestDispatcher("error.jsp").forward(request, response);
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
