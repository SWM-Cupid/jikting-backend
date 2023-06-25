package com.cupid.jikting.common.error;

public class NotFoundException extends ApplicationException {

    public NotFoundException(ApplicationError error) {
        super(error);
    }
}
