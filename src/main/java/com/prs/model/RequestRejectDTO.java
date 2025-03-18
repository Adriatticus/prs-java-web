package com.prs.model;

public class RequestRejectDTO {

	public String reasonForRejection;

	public RequestRejectDTO() {
		super();
	}

	public String getReasonForRejection() {
		return reasonForRejection;
	}

	public void setReasonForRejection(String reasonForRejection) {
		this.reasonForRejection = reasonForRejection;
	}

	@Override
	public String toString() {
		return "RequestRejectDTO [reasonForRejection=" + reasonForRejection + "]";
	}
}
