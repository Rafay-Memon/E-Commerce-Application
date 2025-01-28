<!-- reset-password.jsp -->
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Reset Password</title>
<link rel="stylesheet" href="css/style.css">
</head>
<body>
	<script src="js/script.js"></script>
	 <!-- Header -->
    <%@include file="header.jsp" %>
    
	<h1>Password Recovery</h1>
	<div class="container">
		<h2>Reset Password</h2>
        <% if (request.getAttribute("errorMessage") != null) { %>
            <div class="error-message"><%= request.getAttribute("errorMessage") %></div>
        <% } %>
		<form action="ResetPasswordController" method="POST">
			<input type="hidden" name="token" value="${param.token}" /> <!-- Correct token fetching -->
			<!-- Hidden field for token -->
			<label for="newPassword">Enter new password:</label><br> 
            <input type="password" id="newPassword" name="newPassword" required>
            <i class="toggle-password" onclick="togglePassword()">👁️</i>
            <br><br>
			<button type="submit">Reset Password</button>
		</form>
	</div>
	<%@ include file="footer.jsp" %>
</body>
</html>
