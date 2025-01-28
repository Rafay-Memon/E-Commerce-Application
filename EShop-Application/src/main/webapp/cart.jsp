<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="java.util.List, com.estore.model.Cart"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Cart Page</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
	
	<script src="js/script.js"></script>
	
    <!-- Header -->
    <%@include file="header.jsp" %>
    
    <h1>Your Cart</h1>

    <form action="CartController" method="post">
        <table>
            <thead>
                <tr>
                    <th>Product</th>
                    <th>Category</th>
                    <th>Price</th>
                    <th>Quantity</th>
                    <th>Total</th>
                    <th>Remove Item</th>
                </tr>
            </thead>
            <tbody>
                 <%
                    List<Cart> cartItems = (List<Cart>) request.getAttribute("cartItems");
                    if (cartItems == null || cartItems.isEmpty()) {
                %>
                <tr>
                    <td colspan="6">Your cart is empty.</td>
                </tr>
                <% } else {
                	 for (Cart cartItem : cartItems) {
                 %>
                <tr>
                    <td>
                        <img src="<%= cartItem.getProduct().getImageUrl() %>" alt="<%= cartItem.getProduct().getName() %>" width="100">
                        <p><%= cartItem.getProduct().getName() %></p>
                    </td>
                    <td><%= cartItem.getProduct().getCategory() %></td>
                    <td><%= cartItem.getProduct().getPrice() %></td>
                    <td>
                        <div class="quantity-control">
                            <button type="submit" name="cartaction" value="decrease" onclick="document.getElementById('cartId').value='<%= cartItem.getCartId() %>'">-</button>
                            <input type="number" name="quantity" value="<%= cartItem.getQuantity() %>" min="1" class="quantity-input" readonly>
                            <button type="submit" name="cartaction" value="increase" onclick="document.getElementById('cartId').value='<%= cartItem.getCartId() %>'">+</button>
                            <input type="hidden" id="cartId" name="cartId" value="<%= cartItem.getCartId() %>">
                        </div>
                    </td>
                    <td><%= cartItem.getProduct().getPrice() * cartItem.getQuantity() %></td>
                    <td>
                        <button type="submit" name="cartaction" value="remove" onclick="document.getElementById('cartId').value='<%= cartItem.getCartId() %>'">Remove</button>
                    </td>
                </tr>
                <%
                    } }
                %>
            </tbody>
        </table>
    </form>

    <div class="total">
        <p id="totalPrice"><strong>Total Price: </strong><%= request.getAttribute("totalPrice") %></p>
    </div>
   
   <% if (request.getAttribute("errorMessage") != null) { %>
	            <div class="error-message"><%= request.getAttribute("errorMessage") %></div>
	        <% } 
	%>
   <div class="checkout">
    <form action="CartController" method="post">
        <input type="hidden" name="cartaction" value="checkout">
        <button type="submit" name="checkout" value="checkoutpage">Checkout</button>
    </form>
</div>

	<!-- Footer -->
    <%@include file="footer.jsp" %>
</body>
</html>
