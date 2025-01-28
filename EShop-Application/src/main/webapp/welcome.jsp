<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Welcome to E-Shop</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css"> <!-- Font Awesome for icons -->
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
	<script src="js/script.js"></script>
	
    <!-- Header -->
    <%@include file="header.jsp" %>

    <!-- Carousel -->
    <div class="carousel">
        <img src="images/eshop-2.jpg" alt="Main Banner">
    </div>
 
    <!-- Main Content -->
    <main>
        <h2>Welcome to E-Shop</h2>
        <p>Find the best products at unbeatable prices. Start shopping today!</p>
        <br>
        <a href="ProductController">Shop Now</a>
    </main>

    <!-- Footer -->
    <%@include file="footer.jsp" %>
</body>
</html>
