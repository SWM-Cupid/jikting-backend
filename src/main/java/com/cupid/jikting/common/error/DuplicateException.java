package com.cupid.jikting.common.error;

public class DuplicateException extends ApplicationException {

    public DuplicateException(ApplicationError error) {
        super(error);
    }
}
