package com.realpage.tvmaze.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class OrderTypeInvalidException extends RuntimeException {

    private static final long serialVersionUID = 6047051151650137195L;

    public OrderTypeInvalidException(String message) { super(message); }
}
