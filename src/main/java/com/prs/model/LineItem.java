package com.prs.model;

import jakarta.persistence.*;

@Entity
public class LineItem {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public int id;
	@ManyToOne
	@JoinColumn(name="RequestId")
	public Request request;
	@ManyToOne
	@JoinColumn(name="ProductId")
	public Product product;
	public int quantity;
	
	public LineItem() {
		super();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Request getRequest() {
		return request;
	}

	public void setRequest(Request request) {
		this.request = request;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	@Override
	public String toString() {
		return "LineItem [id=" + id + ", request=" + request + ", product=" + product + ", quantity=" + quantity + "]";
	}
	
}
