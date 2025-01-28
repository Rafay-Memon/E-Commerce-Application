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
import javax.naming.NamingException;
import javax.sql.DataSource;

import com.estore.model.Cart;
import com.estore.model.User;
import com.estore.model.dao.CartDAO;
import com.estore.model.dao.ProductDAO;

/**
 * Servlet implementation class CartController
 */
@WebServlet("/CartController")
public class CartController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private Connection connection;
    private CartDAO cartDAO;
    private ProductDAO productDAO;
    private User user;

    public CartController() {
        super();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		DataSource dataSource;
		try {
			// Retrieve connection from DataSource
			dataSource = (DataSource) new InitialContext().lookup("java:/comp/env/jdbc/MyDB");
			connection = dataSource.getConnection();
			productDAO = new ProductDAO(connection);
			cartDAO = new CartDAO(connection, productDAO);

			// Get the current user session
			HttpSession session = request.getSession(false);
			if (session == null || session.getAttribute("user") == null) {
				// Redirect to login page if the user is not logged in
				request.setAttribute("errorMessage", "Please log in to continue.");
				request.getRequestDispatcher("/login.jsp").forward(request, response);
				return;
			}
			user = (User) session.getAttribute("user");
			String productId = request.getParameter("productId");
			String action = request.getParameter("action");

			// If the action is "addToCart"
			if ("addToCart".equals(action)) {
			
				// Add product to cart
				cartDAO.addItemToCart(user.getUserId(), Integer.parseInt(productId));

				// Update cart and cart count in the session
				List<Cart> cartItems = cartDAO.getCartItemsByUserId(user.getUserId());
				double totalPrice = cartDAO.calculateTotalPrice(user.getUserId());
				
				// Set attributes for cart
				request.setAttribute("cartItems", cartItems);
				request.setAttribute("totalPrice", totalPrice);

				// Update the cart count in the session
				int cartCount = cartItems.size();
				session.setAttribute("cartCount", cartCount);
				
				response.sendRedirect("ProductController");

			}
			
			else if("buyNow".equals(action)) {
				
				// Add product to cart
				cartDAO.addItemToCart(user.getUserId(), Integer.parseInt(productId));

				// Update cart and cart count in the session
				List<Cart> cartItems = cartDAO.getCartItemsByUserId(user.getUserId());
				double totalPrice = cartDAO.calculateTotalPrice(user.getUserId());
				
				// Set attributes for cart
				request.setAttribute("cartItems", cartItems);
				request.setAttribute("totalPrice", totalPrice);
				
				// Forward to the checkout.jsp page
	            request.getRequestDispatcher("/checkout.jsp").forward(request, response);
				
			}

			else {

				// Fetch cart items if no action is performed
				List<Cart> cartItems = cartDAO.getCartItemsByUserId(user.getUserId());
				double totalPrice = cartDAO.calculateTotalPrice(user.getUserId());

				
				// Set attributes for cart
				request.setAttribute("cartItems", cartItems);
				request.setAttribute("totalPrice", totalPrice);

				// Forward to cart.jsp
				request.getRequestDispatcher("cart.jsp").forward(request, response);

			}

		} catch (Exception e) {
			e.printStackTrace();
			response.sendRedirect("error.jsp");
		} finally {
			try {
				if (connection != null)
					connection.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

    	
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        DataSource dataSource = null;
        String action = request.getParameter("cartaction");
        String checkoutAction = request.getParameter("checkout");

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("login.jsp");
            return;
        }
        user = (User) session.getAttribute("user");

        try {
            dataSource = (DataSource) new InitialContext().lookup("java:/comp/env/jdbc/MyDB");
            connection = dataSource.getConnection();
            productDAO = new ProductDAO(connection);
            cartDAO = new CartDAO(connection, productDAO);

            // Handle checkout page request
            if ("checkoutpage".equals(checkoutAction)) {
                List<Cart> cartItems = cartDAO.getCartItemsByUserId(user.getUserId());
                
                if (cartItems == null || cartItems.isEmpty()) {
                    request.setAttribute("cartItems", new ArrayList<Cart>());
                    request.setAttribute("errorMessage", "Cart is empty. Please add items then checkout.");
                    request.getRequestDispatcher("cart.jsp").forward(request, response);
                    return;
                }
                
                double totalPrice = cartDAO.calculateTotalPrice(user.getUserId());
                request.setAttribute("cartItems", cartItems);
                request.setAttribute("totalPrice", totalPrice);
                request.getRequestDispatcher("checkout.jsp").forward(request, response);
                return;
            }

            // Handle cart item modifications
            if ("increase".equals(action) || "decrease".equals(action) || "remove".equals(action)) {
                int cartId = Integer.parseInt(request.getParameter("cartId"));
                List<Cart> userCartItems = cartDAO.getCartItemsByUserId(user.getUserId());
                
                for (Cart item : userCartItems) {
                    if (item.getCartId() == cartId) {
                        if ("increase".equals(action)) {
                            cartDAO.updateCartQuantity(cartId, item.getQuantity() + 1);
                        } else if ("decrease".equals(action) && item.getQuantity() > 1) {
                            cartDAO.updateCartQuantity(cartId, item.getQuantity() - 1);
                        } else if ("remove".equals(action)) {
                            cartDAO.removeCartItem(cartId);
                        }
                        break;
                    }
                }
            }

            // Refresh cart items and update session
            List<Cart> updatedCartItems = cartDAO.getCartItemsByUserId(user.getUserId());
            double totalPrice = cartDAO.calculateTotalPrice(user.getUserId());
            
            session.setAttribute("cartCount", updatedCartItems.size());
            request.setAttribute("cartItems", updatedCartItems);
            request.setAttribute("totalPrice", totalPrice);

            request.getRequestDispatcher("/cart.jsp").forward(request, response);

        } catch (SQLException | NamingException e) {
            e.printStackTrace();
            response.sendRedirect("error.jsp");
        } finally {
            try {
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}

