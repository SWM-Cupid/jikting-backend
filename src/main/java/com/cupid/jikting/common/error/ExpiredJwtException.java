package com.cupid.jikting.common.error;

public class ExpiredJwtException extends JwtException {
    public ExpiredJwtException(ApplicationError error) {
        super(error);
    }
}
