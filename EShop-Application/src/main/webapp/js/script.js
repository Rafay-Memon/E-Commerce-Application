/**
 * 
 */
	// Check if a header exists on the page
	document.addEventListener("DOMContentLoaded", function () {
	    const header = document.querySelector(".header-container");
	    if (header) {
	        document.body.classList.add("with-header");
	    } else {
	        document.body.classList.remove("with-header");
	    }
	});
	
	function togglePassword() {
	    const passwordField = document.getElementById("password");
	    const toggleIcon = document.querySelector(".toggle-password");
	    const isPasswordVisible = passwordField.type === "text";

	    passwordField.type = isPasswordVisible ? "password" : "text";
	    toggleIcon.textContent = isPasswordVisible ? "👁️" : "🙈";
	}
	
	// This function will submit the form when the user types in the search input
	document.getElementById("searchQuery").addEventListener("input", function() {
		this.form.submit();
	});
	
	// Function to update the item-count 
	function addToCart(productId) {
	        fetch(`/ProductAccessController?action=addToCart&productId=${productId}`, {
	            method: "GET"
	        })
	        .then(response => response.text())
	        .then(data => {
	            document.getElementById("item-count").textContent = data; // Update cart count
	        })
	        .catch(error => console.error("Error adding to cart:", error));
	    }
	
		// Function to update the cart quantity and total price dynamically
		function updateQuantity(cartId, quantity) {
		    if (quantity < 1) return; // Prevent quantity from going below 1

		    $.ajax({
		        url: "CartController", // CartController will handle the quantity update
		        type: "POST",
		        data: { cartId: cartId, quantity: quantity },
		        success: function(response) {
		            // Update the total price dynamically based on the response
		            let cartItems = JSON.parse(response);
		            let totalPrice = 0;
		            cartItems.forEach(function(item) {
		                totalPrice += item.productPrice * item.quantity;
		                $('#itemTotal_' + item.cartId).text(item.productPrice * item.quantity);
		            });

		            // Update the total price displayed on the page
		            $('#totalPrice').text('Total Price: ' + totalPrice);
		        },
		        error: function(error) {
		            console.log("Error updating quantity: ", error);
		        }
		    });
		}
		
		// Checkout Validation
		function validateForm() {
		           let name = document.getElementById("name").value;
		           let email = document.getElementById("email").value;
		           let address = document.getElementById("address").value;
		           let phone = document.getElementById("phone").value;
		           let isValid = true;

		           // Clear previous error messages
		           document.getElementById("errorName").innerText = "";
		           document.getElementById("errorEmail").innerText = "";
		           document.getElementById("errorAddress").innerText = "";
		           document.getElementById("errorPhone").innerText = "";

		           // Validate name
		           if (name == "") {
		               document.getElementById("errorName").innerText = "Name is required.";
		               isValid = false;
		           }

		           // Validate email
		           let emailPattern = /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,6}$/;
		           if (email == "" || !emailPattern.test(email)) {
		               document.getElementById("errorEmail").innerText = "Please enter a valid email address.";
		               isValid = false;
		           }

		           // Validate address
		           if (address == "") {
		               document.getElementById("errorAddress").innerText = "Address is required.";
		               isValid = false;
		           }

		           // Validate phone number
		           let phonePattern = /^\d{10}$/;
		           if (phone == "" || !phonePattern.test(phone)) {
		               document.getElementById("errorPhone").innerText = "Please enter a valid 10-digit phone number.";
		               isValid = false;
		           }

		           return isValid;
		       }