# E-shop: Java EE E-commerce Application

## 🌟 Overview

**E-shop** is a feature-rich e-commerce web application developed using **Java EE**, designed for learning and demonstrating core web development concepts. The project is built with the **MVC** architecture and follows the **DAO** design pattern for modular and maintainable code.

## 🔑 Features

1.	**Dynamic Header & Footer:** 
- Personalized navigation based on user authentication state.
2.	**Product Page:**
- Search and filter functionality.
- Add to cart and buy now (restricted to logged-in users).
3.	**Cart Page:**
- View, update quantities, remove items, and checkout with order summary.
4.	**Order Management:**
- Users can view and cancel orders. (Admin functionality to update order status is planned)
5.	**User Authentication:**
- Secure login with session management.
- Signup and forgot password with password visibility toggle.

## 🛠 Tech Stack

- **Backend:** Jakarta Servlet API (6), JSP, Java
- **Frontend:** JSP (Java + HTML), CSS, JavaScript
- **Database:** MySQL with connection pooling
- **Server:** Apache Tomcat 10
- **Tools:** Eclipse IDE, MySQL Workbench

## 📈 Architecture

- **Model-View-Controller (MVC)** for clear separation of concerns.
- **DAO Design Pattern** for database interaction.

## 🛠️ Installation

### Prerequisites
- Java Development Kit (JDK 8 or above)
- Apache Tomcat 10
- MySQL Database
- Eclipse IDE

### Steps to Run Locally
1.	**Clone the Repository:**
- `git clone https://github.com/Rafay-Memon/E-Commerce-Application.git `
2.	**Import Project into Eclipse:**
- Open Eclipse IDE.
- Go to File > Import > Existing Projects into Workspace.
- Select the cloned project directory.
3.	**Setup Database:**
- Open MySQL Workbench and create a new database:
``` sql
CREATE DATABASE estoreapp;
```
- Import the **estoreapp.sql** file (located in the e-commerce-project directory) to set up the required tables and sample data.

4.	**Update Database Configuration:**
- Navigate to src/main/webapp/META-INF/context.xml
- Update the following fields:
```xml
<Context>
<Resource
... 
username = "your_mysql_username";
password = "your_mysql_password";
...
/>
</Context>
```
5.	**Deploy on Tomcat:**
- Right-click on the project in Eclipse.
- Select Run As > Run on Server.
- Choose Apache Tomcat and start the server.
6.	**Access the Application:**
- Open your browser and navigate to: `http://localhost:8080/E-Commerce-Application`

## 📸 Screenshots

### Home Page:
![Home Page](/EShop-Application/appscreenshots/homepage.png)

### Product Page:
![Product Page](/EShop-Application/appscreenshots/productpage.png)

### Cart Page:
![Cart Page](/EShop-Application/appscreenshots/cartpage.png)

### Checkout Form:
![Checkout](/EShop-Application/appscreenshots/checkoutpage.png)

### Order Page:
![Order Page](/EShop-Application/appscreenshots/orderpage.png)

## 🚀 Future Enhancements

- Admin panel for product and order management
- Enhanced security with password encryption.
- Integrate Hibernate for ORM.
- Upgrade to Spring Framework for better scalability and ease of development.
- Add payment gateway integration.

## 🤝 Contributions & Feedback

Feel free to fork, contribute, or provide suggestions for improvements. This project is a stepping stone in my learning journey with Java EE, and your feedback is highly valued!

Let me know if you'd like further customization or additional details for any section! 😊
