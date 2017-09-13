package com.ePurchase.utils;

public class ConstraintViolationModel {
	private String field;
	private String message;
	private Object invalidValue;

	public ConstraintViolationModel(String field, String message, Object invalidValue) {
		this.field = field;
		this.message = message;
		this.invalidValue = invalidValue;
	}

	public String getField() {
		return field;
	}

	public String getMessage() {
		return message;
	}

	public Object getInvalidValue() {
		return invalidValue;
	}
}