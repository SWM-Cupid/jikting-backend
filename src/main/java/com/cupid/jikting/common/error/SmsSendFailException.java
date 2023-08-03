package com.cupid.jikting.common.error;

public class SmsSendFailException extends RuntimeException {

    public SmsSendFailException(ApplicationError error) {
        super(error.getMessage());
    }
}
