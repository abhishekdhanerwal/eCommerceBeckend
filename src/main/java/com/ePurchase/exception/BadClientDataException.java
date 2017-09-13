package com.ePurchase.exception;

public class BadClientDataException extends RuntimeException {

    public BadClientDataException(String[] fields) {
        super(BadClientDataException.convertToCommaSeperatedString(fields));
    }

    public BadClientDataException(String field) {
        super(field);
    }

    private static String convertToCommaSeperatedString(String[] fields) {
        StringBuffer string = new StringBuffer();
        for (int i = 0; i < fields.length; i++) {
            if (i == 0)
                string.append(fields[i]);
            else
                string.append("," + fields[i]);
        }
        return string.toString();
    }
}
