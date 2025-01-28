package com.estore.controller;

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

import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.naming.InitialContext;
import javax.sql.DataSource;

@WebServlet("/CheckoutController")
public class CheckoutController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Connection connection;
	private CartDAO cartDAO;
	private OrderDAO orderDAO;

	public CheckoutController() {
		super();
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		DataSource dataSource;
		try {
			// Retrieve connection from DataSource
			dataSource = (DataSource) new InitialContext().lookup("java:/comp/env/jdbc/MyDB");
			connection = dataSource.getConnection();
			// Get session user ID
			HttpSession session = request.getSession();
			User user = (User) session.getAttribute("user");

			// Validate customer details
			String name = request.getParameter("name");
			String email = request.getParameter("email");
			String address = request.getParameter("address");
			String phone = request.getParameter("phone");

			// Simple validation for non-empty fields
			if (name == null || email == null || address == null || phone == null || name.trim().isEmpty()
					|| email.trim().isEmpty() || address.trim().isEmpty() || phone.trim().isEmpty()) {
				// Redirect back to checkout page if validation fails
				request.setAttribute("errorMessage", "All fields must be filled out correctly.");
				request.getRequestDispatcher("/checkout.jsp").forward(request, response);
				return;
			}

			// Create Order object and store it
			Order order = new Order();
			order.setUserId(user.getUserId());
			order.setName(name);
			order.setEmail(email);
			order.setAddress(address);
			order.setPhone(phone);
			order.setOrderDate(new Date());

			// Get cart items for this user
			cartDAO = new CartDAO(connection);
			List<Cart> cartItems = new ArrayList<>();
			
					
			cartItems = cartDAO.getCartItemsByUserId(user.getUserId());

			// order.setCartItems(cartItems);

			System.out.println("Cart Items: " + cartItems);

			double totalPrice = 0.0;
			for (Cart item : cartItems) {
				totalPrice += item.getTotalPrice(); // Sum up the total price
				order.addCartItem(item);
			}

			order.setTotalPrice(totalPrice);

			// Save order to database
			orderDAO = new OrderDAO(connection);

			boolean orderPlaced = orderDAO.createOrder(order);


			if (orderPlaced) {
				// clears the cart
				cartDAO.clearCart(user.getUserId());
				
				// Update the cart count 0 in the session when user places the order
				session.setAttribute("cartCount", 0);
				
				// Redirect to order confirmation page (OrderController)
				response.sendRedirect("OrderController?userId=" + user.getUserId());
			}

			else {
				request.setAttribute("errorMessage", "Please enter valid data.");
				request.getRequestDispatcher("/checkout.jsp").forward(request, response);
			}

		} catch (Exception e) {
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
