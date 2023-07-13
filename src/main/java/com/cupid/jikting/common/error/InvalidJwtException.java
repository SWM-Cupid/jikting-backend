package com.cupid.jikting.common.error;

public class InvalidJwtException extends JwtException {
    public InvalidJwtException(ApplicationError error) {
        super(error);
    }
}
