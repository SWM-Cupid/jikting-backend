package com.cupid.jikting.common.error;

public class MailSendFailException extends ApplicationException {

    public MailSendFailException(ApplicationError applicationError) {
        super(applicationError);
    }
}
