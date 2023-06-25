package com.cupid.jikting.common.error;

public class UnAuthorizedException extends ApplicationException {

    public UnAuthorizedException(ApplicationError error) {
        super(error);
    }
}
