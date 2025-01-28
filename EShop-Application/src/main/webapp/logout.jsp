<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>logout</title>
</head>
<body>
	<%
	    session.invalidate(); // Clear the session
	    response.sendRedirect("welcome.jsp"); // Redirect to homepage after logging out
	%>
</body>
</html>