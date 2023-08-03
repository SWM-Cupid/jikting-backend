package com.cupid.jikting.common.error;

public class FileUploadException extends ApplicationException {

    public FileUploadException(ApplicationError error) {
        super(error);
    }
}
