package com.cupid.jikting.common.error;

public class WrongAccessException extends ApplicationException {

    public WrongAccessException(ApplicationError error) {
        super(error);
    }
}
