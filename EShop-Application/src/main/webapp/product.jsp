<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="java.util.List , com.estore.model.Product" %>
<!DOCTYPE html>
<html>
<head>
    <title>Product Catalog</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
    <script type="js/script.js"></script>

    <!-- Include Header -->
    <%@include file="header.jsp" %>

   <!-- Form for Category Filter and Search -->
    <form id="scrollable-form" action="ProductController" method="GET">
        <label for="category">Select Category:</label>
        <select name="category" id="category" onchange="this.form.submit()">
            <option value="">All Categories</option>
            <!-- Dynamic categories fetched from the database -->
            <%
                List<String> categories = (List<String>) request.getAttribute("categories");
                String selectedCategory = request.getParameter("category");
                for (String category : categories) {
            %>
                <option value="<%= category %>" <%= category.equals(selectedCategory) ? "selected" : "" %> >
                    <%= category %>
                </option>
            <%
                }
            %>
        </select>

        <!-- Search Bar -->
        <label for="searchQuery">Search:</label>
        <input type="text" id="searchQuery" name="searchQuery" value="<%= request.getParameter("searchQuery") != null ? request.getParameter("searchQuery") : "" %>">
        
        <!-- Search Button -->
        <button type="submit">Search</button>
    </form>
    
    <!-- Product Cards -->
    <div class="product-container">
        <%
            List<Product> products = (List<Product>) request.getAttribute("products");
            if (products != null && !products.isEmpty()) {
                for (Product product : products) {
        %>
                    <div class="product-card">
                        <img src="<%= product.getImageUrl() %>" alt="<%= product.getName() %>" class="product-image">
                        <div class="product-details">
                            <h3><%= product.getName() %></h3>
                            <p>Category: <%= product.getCategory() %></p>
                            <p>Price: $<%= product.getPrice() %></p>
                            	<!-- Add to Cart -->
       					    <a href="CartController?action=addToCart&productId=<%= product.getProductId() %>"
       					       class="button"
       					       onclick="addToCart('<%= product.getProductId() %>');"
       					    >Add to Cart</a>

					        	<!-- Buy Now -->
					        <a href="CartController?action=buyNow&productId=<%= product.getProductId() %>" class="button">Buy Now</a>
                        </div>
                    </div>
        <%
                }
            } else {
        %>
                <p>No products found for the selected category or search query.</p>
        <%
            }
        %>
    </div>

    <!-- Pagination -->
    <div class="pagination">
       <%-- Pagination Links --%>
		<% 
		    int totalPages = (int) request.getAttribute("totalPages"); 
		    int currentPage = (int) request.getAttribute("currentPage");
		    String category = (String) request.getAttribute("category");
		    String searchQuery = (String) request.getAttribute("searchQuery");
		%>
		
		<%-- Loop through totalPages and create page links --%>
		<% for (int i = 1; i <= totalPages; i++) { %>
		    <a href="ProductController?page=<%= i %>&category=<%= category %>&searchQuery=<%= searchQuery %>">
		        <%= i %>
		    </a>
		<% } %>

    </div>

    <!-- Include Footer -->
    <%@include file="footer.jsp" %>
</body>
</html>
