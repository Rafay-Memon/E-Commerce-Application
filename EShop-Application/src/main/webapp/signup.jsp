<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Signup Page</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>

	<script src="js/script.js"></script>
	
    <!-- Header -->
    <%@include file="header.jsp" %>
	
	
	<h1>E-SHOP SIGNUP</h1>
	<div class="main-content">
	    <div class="container">
	        <h2>Signup</h2>
	        <% if (request.getAttribute("errorMessage") != null) { %>
	            <div class="error-message"><%= request.getAttribute("errorMessage") %></div>
	        <% } %>
	        <form action="SignupController" method="POST">
	            <label for="username">Username</label>
	            <input type="text" name="username" id="username" placeholder="Enter your username" required>
	            
	            <label for="email">Email</label>
	            <input type="email" name="email" id="email" placeholder="Enter your email" required>
	            
	            <label for="password">Password</label>
	            <input type="password" name="password" id="password" placeholder="Enter your password" pattern="(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d@$!%*?&]{8,}" title="Password must be at least 8 characters long and contain at least one letter and one number." required>
	            <i class="toggle-password" onclick="togglePassword()">👁️</i>
	            <button type="submit">Sign Up</button>
	        </form>
	        <p class="additional-links">Already have an account? <a href="login.jsp">Login</a></p>
	    </div>
    </div>
    
    <!-- Footer -->>
    <%@ include file="footer.jsp" %>
</body>
</html>
