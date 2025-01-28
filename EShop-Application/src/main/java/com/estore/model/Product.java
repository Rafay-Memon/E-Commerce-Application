package com.estore.model;

public class Product {
	private int productId;
	private String name;
	private double price;
	private String category;
	private String imageUrl;
	
	// Constructor
	
	public Product(int productId, String name, double price, String category, String imageUrl) {

		this.productId = productId;
		this.name = name;
		this.price = price;
		this.category = category;
		this.imageUrl = imageUrl;
	}
	
	public Product(String name, double price, String category, String imageUrl) {
		this.name = name;
		this.price = price;
		this.category = category;
		this.imageUrl = imageUrl;
	}
	
	// Getter and Setter

	public Product() {
		// TODO Auto-generated constructor stub
	}

	public int getProductId() {
		return productId;
	}

	public void setProductId(int productId) {
		this.productId = productId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}


}
