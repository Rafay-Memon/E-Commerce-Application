<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.estore.model.Order, com.estore.model.Cart, com.estore.model.Product"%>
<%@ page import="java.util.List, java.util.Map, java.util.HashMap"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Order Details</title>
    <link rel="stylesheet" href="css/style.css">

</head>
<body>

	<script src="js/script.js"></script>
	<!-- Header -->>
	<%@include file="header.jsp" %>
	
    <div class="order-container">
        <h1>Order Details</h1>

        <table>
            <thead>
                <tr>
                    <th>Order Date</th>
                    <th>Status</th>
                    <th>Total Items</th>
                    <th>Total Order Value</th>
                    <th>Order Details</th>
                    <th>Actions</th>
                </tr>
            </thead>
            <tbody>
                <%
                List<Order> orders = (List<Order>) request.getAttribute("orders");
                if (orders != null && !orders.isEmpty()) {
                    // Group orders by OrderId
                    Map<Integer, Order> uniqueOrders = new HashMap<>();
                    for (Order order : orders) {
                        uniqueOrders.put(order.getOrderId(), order);
                    }

                    for (Order order : uniqueOrders.values()) {
                        double totalOrderValue = 0;
                        int totalItems = 0;
                        
                     // Determine status class
                        String statusClass = "";
                        switch(order.getStatus().toLowerCase()) {
                            case "pending":
                                statusClass = "status-pending";
                                break;
                            case "confirmed":
                                statusClass = "status-confirmed";
                                break;
                            case "delivered":
                                statusClass = "status-delivered";
                                break;
                            default:
                                statusClass = "";
                        }
                %>
                <tr>
                    <td><%= order.getOrderDate() %></td>
                    <td> <span class="<%= statusClass %>"> <%= order.getStatus() %> </span>
                    </td>
                    <td>
                        <%
                        // Calculate total items and total order value
                        for (Cart item : order.getCartItems()) {
                            totalItems += item.getQuantity();
                            totalOrderValue += item.getTotalPrice();
                        }
                        out.print(totalItems);
                        %>
                    </td>
                    <td>$<%= String.format("%.2f", totalOrderValue) %></td>
                    <td>
                        <details>
                            <summary>View Order Items</summary>
                            <table>
                                <thead>
                                    <tr>
                                        <th>Product</th>
                                        <th>Quantity</th>
                                        <th>Price</th>
                                        <th>Total</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <% 
                                    for (Cart item : order.getCartItems()) { 
                                        Product product = item.getProduct();
                                    %>
                                    <tr>
                                        <td>
                                            <div class="product-name-container">
                                                <img src="<%= product.getImageUrl() %>" 
                                                     alt="<%= product.getName() %>" 
                                                     class="product-thumbnail">
                                                <%= product.getName() %>
                                            </div>
                                        </td>
                                        <td><%= item.getQuantity() %></td>
                                        <td>$<%= String.format("%.2f", product.getPrice()) %></td>
                                        <td>$<%= String.format("%.2f", item.getTotalPrice()) %></td>
                                    </tr>
                                    <% } %>
                                </tbody>
                            </table>
                        </details>
                    </td>
                    <td>
                        <div class="order-actions">
                            <form method="post" action="OrderController">
                                <input type="hidden" name="action" value="cancelOrder">
                                <input type="hidden" name="orderId" value="<%= order.getOrderId() %>">
                                <button type="submit" class="cancel-button">Cancel Order</button>
                            </form>
                        </div>
                    </td>
                </tr>
                <% 
                    } 
                } else { 
                %>
                <tr>
                    <td colspan="7" class="no-orders">No orders found.</td>
                </tr>
                <% } %>
            </tbody>
        </table>
    </div>
    <!-- Footer -->>
    <%@include file="footer.jsp" %>
</body>
</html>