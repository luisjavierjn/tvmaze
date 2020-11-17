package com.realpage.tvmaze.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class PagingDataInvalidException extends RuntimeException {

    private static final long serialVersionUID = -7744982745537722051L;

    public PagingDataInvalidException(String message) { super(message); }
}
