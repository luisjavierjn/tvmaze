package com.realpage.tvmaze.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class PageIndexNotFoundException extends RuntimeException {

    private static final long serialVersionUID = -2923864554162275898L;

    public PageIndexNotFoundException(String message) { super(message); }
}
