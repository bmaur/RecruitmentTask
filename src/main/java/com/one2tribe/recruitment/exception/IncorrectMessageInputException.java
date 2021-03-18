package com.one2tribe.recruitment.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class IncorrectMessageInputException extends RuntimeException {
    public IncorrectMessageInputException() {
        super("Message content can't be blank!");
    }
}
