package com.symphodia.spring.common.error;

public enum ErrorCode {

    INTERNAL_SERVICE_ERROR(500),
    PARSE_ERROR(418),
    VALIDATION_ERROR(419),
    LISTENER_ERROR(101);

    private final int errorNumber;

    ErrorCode(int errorNumber) {
        this.errorNumber = errorNumber;
    }

    public int getErrorNumber() {
        return errorNumber;
    }

    public static ErrorCode getErrorCode(Exception e) {
        if (e instanceof ApplicationException) {
            return ((ApplicationException) e).getErrorCode();
        }
        return INTERNAL_SERVICE_ERROR;
    }
}
