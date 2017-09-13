package com.ePurchase.utils;


import com.ePurchase.exception.BadClientDataException;
import com.ePurchase.exception.NotFoundException;

import org.springframework.hateoas.VndErrors;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

@ControllerAdvice
public class ExceptionControllerAdvice {

    @ResponseBody
    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public VndErrors notFoundExceptionHandler(NotFoundException ex) {
        return new VndErrors("error", ex.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(BadClientDataException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public VndErrors badClientDataExceptionHandler(BadClientDataException ex) {
        return new VndErrors("error", ex.getMessage());
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public @ResponseBody
    ConstraintViolations handleConstraintViolation(HttpServletRequest req, final ConstraintViolationException
            exception) {
        ArrayList<ConstraintViolationModel> list = new ArrayList<ConstraintViolationModel>();
        for (ConstraintViolation<?> violation : exception.getConstraintViolations()) {
            list.add(new ConstraintViolationModel(violation.getPropertyPath().toString(), violation.getMessage(),
                    violation.getInvalidValue()));
        }
        return new ConstraintViolations(list);
    }

}
