<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login Page</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
	<script src="js/script.js"></script>
	
	 <!-- Header -->
    <%@include file="header.jsp" %>
	
	<h1>E-SHOP LOGIN</h1>
	<div class="main-content">
	    <div class="container">
	        <h2>Welcome Back!</h2>
	        <% if (request.getAttribute("errorMessage") != null) { %>
	            <div class="error-message"><%= request.getAttribute("errorMessage") %></div>
	        <% } %>
	        <form action="LoginController" method="POST">
	            <label for="email">Email Address</label>
	            <input type="email" name="email" id="email" placeholder="Enter your email" required>
	            
	            <label for="password">Password</label>
	            <input type="password" name="password" id="password" placeholder="Enter your password" required>
	            <i class="toggle-password" onclick="togglePassword()">👁️</i>
	            <button type="submit">Login</button>
	        </form>
	        <p class="additional-links">Don't have an account? <a href="signup.jsp">Sign up</a></p>
	        <p class="additional-links"><a href="forgot-password.jsp">Forgot Password?</a></p>
	    </div>
    </div>
    <%@ include file="footer.jsp" %>
</body>
</html>
