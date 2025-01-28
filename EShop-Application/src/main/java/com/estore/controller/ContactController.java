package com.estore.controller;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/ContactServlet")
public class ContactController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // Retrieve form data
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String message = request.getParameter("message");

        // Basic validation
        if (name == null || name.isEmpty() ||
            email == null || email.isEmpty() ||
            message == null || message.isEmpty()) {
            
            // If validation fails, show an alert and redirect back to the contact page
            response.setContentType("text/html");
            response.getWriter().println(
                "<script type=\"text/javascript\">" +
                "alert('Please fill in all the fields!');" +
                "window.location.href='contact.jsp';" +
                "</script>"
            );
        } else {
            // If validation passes, show a success alert and redirect to welcome.jsp
            response.setContentType("text/html");
            response.getWriter().println(
                "<script type=\"text/javascript\">" +
                "alert('Thank you for contacting us! We will get back to you soon.');" +
                "window.location.href='welcome.jsp';" +
                "</script>"
            );
        }
    }
}
