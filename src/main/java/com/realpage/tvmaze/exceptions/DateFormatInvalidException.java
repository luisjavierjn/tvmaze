package com.realpage.tvmaze.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class DateFormatInvalidException extends RuntimeException {

    private static final long serialVersionUID = 8419880276359439713L;

    public DateFormatInvalidException(String message) { super(message); }
}
