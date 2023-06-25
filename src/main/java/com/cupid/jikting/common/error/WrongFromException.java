package com.cupid.jikting.common.error;

public class WrongFromException extends ApplicationException {

    public WrongFromException(ApplicationError error) {
        super(error);
    }
}
