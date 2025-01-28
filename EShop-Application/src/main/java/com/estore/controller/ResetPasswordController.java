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
 * Servlet implementation class ResetPasswordController
 */
@WebServlet("/ResetPasswordController")
public class ResetPasswordController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Connection connection;
	private UserDAO userDAO;

	public ResetPasswordController() {
		super();
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String token = request.getParameter("token");;
		String newPassword = request.getParameter("newPassword");

		/// Retrieve connection from DataSource
        DataSource dataSource;
		try {
			dataSource = (DataSource) new InitialContext().lookup("java:/comp/env/jdbc/MyDB");
			connection = dataSource.getConnection();
			 // Validate the email address
            userDAO = new UserDAO(connection);  // Pass the connection to the UserDAO constructor
			boolean isTokenValid = userDAO.validateResetToken(token);
			

			if (isTokenValid) {
				// Update the password
				boolean passwordUpdated = userDAO.updatePasswordByToken(token, newPassword);

				if (passwordUpdated) {
					response.sendRedirect("login.jsp"); // Redirect to login page
				} else {
					System.out.println("Error in Updating Password");
					request.setAttribute("errorMessage", "Error resetting password. Try again.");
					request.getRequestDispatcher("reset-password.jsp").forward(request, response);
				}
			} else {
				request.setAttribute("errorMessage", "Invalid or expired reset link.");
				request.getRequestDispatcher("reset-password.jsp").forward(request, response);
			}
		} catch (SQLException | NamingException e) {
			e.printStackTrace();
			request.setAttribute("errorMessage", "Database error occurred.");
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
