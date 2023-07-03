package com.cupid.jikting.common.error;

public class WrongFormException extends ApplicationException {

    public WrongFormException(ApplicationError error) {
        super(error);
    }
}
