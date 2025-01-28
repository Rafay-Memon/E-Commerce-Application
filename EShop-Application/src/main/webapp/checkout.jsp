<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List, com.estore.model.Cart"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Checkout Page</title>
    <link rel="stylesheet" href="css/style.css">
    <script src="js/script.js"></script>
</head>
<body>

	<script src="js/script.js"></script>
	
    <!-- Header -->
    <%@include file="header.jsp" %>

    <h1>Checkout</h1>
    
    <form action="CheckoutController" method="post" onsubmit="return validateForm()">
        <div class="checkout-details">
            <h3>Customer Details</h3>
            <label for="name">Full Name:</label>
            <input type="text" id="name" name="name" required>
            <label for="email">Email Address:</label>
            <input type="email" id="email" name="email" required>
            <label for="address">Shipping Address:</label>
            <textarea id="address" name="address" rows="4" required></textarea>
            <label for="phone">Phone Number:</label>
            <input type="text" id="phone" name="phone" required>
        </div>

        <div class="order-summary">
            <h3>Order Summary</h3>
            <%
                List<Cart> cartItems = (List<Cart>) request.getAttribute("cartItems");
                double totalPrice = (double) request.getAttribute("totalPrice");
            %>
            <table>
                <thead>
                    <tr>
                        <th>Product</th>
                        <th>Price</th>
                        <th>Quantity</th>
                        <th>Total</th>
                    </tr>
                </thead>
                <tbody>
                    <%
                        for (Cart cartItem : cartItems) {
                    %>
                    <tr>
                        <td><%= cartItem.getProduct().getName() %></td>
                        <td><%= cartItem.getProduct().getPrice() %></td>
                        <td><%= cartItem.getQuantity() %></td>
                        <td><%= cartItem.getProduct().getPrice() * cartItem.getQuantity() %></td>
                    </tr>
                    <% 
                        }
                    %>
                </tbody>
            </table>
            <p><strong>Total Price: </strong><%= totalPrice %></p>
        </div>

        <div class="checkout-actions">
            <button type="submit">Place Order</button>
        </div>
    </form>

	<!-- Footer -->
    <%@include file="footer.jsp" %>
</body>
</html>
