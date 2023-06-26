package com.cupid.jikting.common.error;

public class BadRequestException extends ApplicationException {

    public BadRequestException(ApplicationError error) {
        super(error);
    }
}
