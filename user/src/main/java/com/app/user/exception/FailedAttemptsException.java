package com.app.user.exception;

import lombok.Getter;

@Getter
public class FailedAttemptsException extends IllegalArgumentException {
    private String got;
    private String field;

    public FailedAttemptsException(String message, String got, String field) {
        super(message);
        this.got = got;
        this.field = field;
    }
}
