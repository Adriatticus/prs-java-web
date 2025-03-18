package com.prs.model;

import java.time.LocalDate;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

public class RequestCreateDTO {

		@ManyToOne
		@JoinColumn(name="UserId")
		public User user;
		public String description;
		public String justification;
		public LocalDate dateNeeded;
		public String deliveryMode;
		//public String requestNumber;
		

		public RequestCreateDTO() {
			super();
		}

		public User getUser() {
			return user;
		}

		public void setUser(User user) {
			this.user = user;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		public String getJustification() {
			return justification;
		}

		public void setJustification(String justification) {
			this.justification = justification;
		}

		public LocalDate getDateNeeded() {
			return dateNeeded;
		}

		public void setDateNeeded(LocalDate dateNeeded) {
			this.dateNeeded = dateNeeded;
		}

		public String getDeliveryMode() {
			return deliveryMode;
		}

		public void setDeliveryMode(String deliveryMode) {
			this.deliveryMode = deliveryMode;
		}
}
