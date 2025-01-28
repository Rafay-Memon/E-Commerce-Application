<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Forgot Password Page</title>
<link rel="stylesheet" href="css/style.css">
</head>
<body>
	<script src="js/script.js"></script>
	 <!-- Header -->
    <%@include file="header.jsp" %>
	<h1>OOPS :( Don't Worry</h1>
	<div class="container">
		<h2>Forgot Password</h2>
		<form action="ForgotPasswordController" method="POST">
			<label for="email">Enter your email address:</label><br> <input
				type="email" id="email" name="email" placeholder="Enter your email" required><br>
			<br>
			<button type="submit">Submit</button>
		</form>
	</div>
	
	<%@ include file="footer.jsp" %>
</body>
</html>
