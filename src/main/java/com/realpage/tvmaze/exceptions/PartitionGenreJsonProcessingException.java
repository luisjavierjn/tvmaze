package com.realpage.tvmaze.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class PartitionGenreJsonProcessingException extends RuntimeException {

    private static final long serialVersionUID = 4738385792975684244L;

    public PartitionGenreJsonProcessingException(String message) { super(message); }
}
