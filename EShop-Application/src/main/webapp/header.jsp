<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<header class="header-fixed">
    <div class="header-container">
        <h1>E-STORE</h1>
        <nav class="navbar">
            <a href="welcome.jsp">Home</a>
            <a href="ProductController">Products</a>
            <a href="about.jsp">About</a>
            <a href="contact.jsp">Contact</a>

            <% if (session.getAttribute("user") != null) { %>
                <a href="OrderController">Orders</a>
                <a href="logout.jsp">Logout</a>
                <div class="cart-container">
                    <a href="CartController">
                        <i class="fa fa-shopping-cart"></i> <!-- Cart Icon -->
                        <span id="item-count" class="item-count">
                            <%= session.getAttribute("cartCount") != null ? session.getAttribute("cartCount") : 0 %>
                        </span>
                        <span class="cart-text">Cart</span> <!-- Cart Text Below Icon -->
                    </a>
                </div>
            <% } else { %>
                <a href="login.jsp">Login</a>
            <% } %>
        </nav>
    </div>
</header>