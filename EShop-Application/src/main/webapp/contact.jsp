<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Contact Us - E-Shop</title>
    <link rel="stylesheet" type="text/css" href="css/style.css">
</head>
<body>
	<script src="js/script.js"></script>
    <%@ include file="header.jsp" %> <!-- Header included here -->
    <main>
        <div class="contact-container">
            <h1>Contact Us</h1>
            <p>
                If you have any questions, suggestions, or complaints, feel free to reach out to us.
                We are here to help you!
            </p>
            <br>
            <form action="ContactController" method="post" class="contact-form">
                <div class="form-group">
                    <label for="name">Name:</label>
                    <input type="text" id="name" name="name" required>
                </div>
                <div class="form-group">
                    <label for="email">Email:</label>
                    <input type="email" id="email" name="email" required>
                </div>
                <div class="form-group">
                    <label for="message">Message:</label>
                    <textarea id="message" name="message" rows="5" required></textarea>
                </div>
                <div class="form-group">
                    <button type="submit" class="btn-submit">Send Message</button>
                </div>
            </form>
        </div>
    </main>
    <%@ include file="footer.jsp" %> <!-- Footer included here -->
</body>
</html>
