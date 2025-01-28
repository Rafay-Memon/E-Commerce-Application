create database estoreapp;

use estoreapp;

CREATE TABLE users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    reset_token VARCHAR(255)
);

CREATE TABLE products (
    product_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    category VARCHAR(100),
    image_url VARCHAR(255)
);

INSERT INTO products (name, price, category, image_url)
VALUES 
		-- Footwear Category Products
	   ('Nike Shoes', 100.00, 'Footwear', 'images/nike.jpg'),
	   ('Puma Shoes', 80.00, 'Footwear', 'images/puma.jpg'),
       ('Beige Sandals', 90.00, 'Footwear', 'images/beige-sandals.jpg'),
       ('Lofers', 120.00, 'Footwear', 'images/lofers.jpg'),
       ('Addidas Shoes', 110.00, 'Footwear', 'images/addidas.jpg'),
       ('Jordan Shoes', '150.00', 'Footwear', 'images/jordan.jpg'),
       -- Clothing Products
	   ('T-Shirt', 100.00, 'Clothing', 'images/tshirt.jpg'),
	   ('Jeans', 80.00, 'Clothing', 'images/jeans.jpg'),
       ('Hoodie', 90.00, 'Clothing', 'images/hoodie.jpg'),
       ('Blazer', 120.00, 'Clothing', 'images/blazer.jpg'),
       ('Gray Suit', 110.00, 'Clothing', 'images/graysuit.jpg'),
       
       -- Accessories Products
       ('Wrist Watch', 100.00, 'Accessories', 'images/wristwatch.jpg'),
	   ('Sun Glasses', 80.00, 'Accessories', 'images/sunglasses.jpg'),
       ('Wallet', 90.00, 'Accessories', 'images/wallet.jpg'),
       ('Girls Handbag', 120.00, 'Accessories', 'images/handbag.jpg'),
       ('jewellery', 110.00, 'Accessories', 'images/jewellery.jpg'),
       ('Necklace', '150.00', 'Accessories', 'images/necklace.jpg');

CREATE TABLE cart (
    cart_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT,
    product_id INT,
    quantity INT DEFAULT 1,
    total_price DECIMAL(10, 2),
    FOREIGN KEY (user_id) REFERENCES users(user_id),
    FOREIGN KEY (product_id) REFERENCES products(product_id)
);

CREATE TABLE orders (
    order_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    address VARCHAR(255) NOT NULL,
    phone VARCHAR(20) NOT NULL,
    total_price DECIMAL(10, 2) NOT NULL,
    order_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    status ENUM('Pending', 'Confirmed', 'Delivered') DEFAULT 'Pending', -- Added status field
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);

CREATE TABLE order_items (
    order_item_id INT AUTO_INCREMENT PRIMARY KEY,
    order_id INT,
    product_id INT,
    quantity INT NOT NULL,
    total_price DECIMAL(10, 2) NOT NULL,
    FOREIGN KEY (order_id) REFERENCES orders(order_id),
    FOREIGN KEY (product_id) REFERENCES products(product_id)
);




