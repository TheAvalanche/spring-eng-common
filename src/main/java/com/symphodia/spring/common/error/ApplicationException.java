package com.symphodia.spring.common.error;


import com.google.common.base.Strings;

public class ApplicationException extends RuntimeException {

    private static final long serialVersionUID = -3545904783912505517L;

    private ErrorCode errorCode;

    public ApplicationException(ErrorCode errorCode, String message) {
        super(Strings.isNullOrEmpty(message) ? errorCode.name() : message);
        this.errorCode = errorCode;
    }

    public ApplicationException(ErrorCode errorCode, String message, Exception cause) {
        super(Strings.isNullOrEmpty(message) ? errorCode.name() : message, cause);
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
