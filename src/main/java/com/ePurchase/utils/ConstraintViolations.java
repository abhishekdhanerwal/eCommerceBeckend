package com.ePurchase.utils;

import java.util.List;

public class ConstraintViolations {
	
	private List<ConstraintViolationModel> errors;

	public List<ConstraintViolationModel> getErrors() {
		return errors;
	}

	public ConstraintViolations(List<ConstraintViolationModel> errors) {
		super();
		this.errors = errors;
	}

}
