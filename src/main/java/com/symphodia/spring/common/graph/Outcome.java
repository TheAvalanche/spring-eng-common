package com.symphodia.spring.common.graph;


import com.symphodia.spring.common.error.ApplicationException;
import com.symphodia.spring.common.error.ErrorCode;

import java.util.EnumSet;

public class Outcome {

    public enum Status {
        SUCCESS, FAILURE, RETRY, RESUMABLE_FAILURE, SKIPPED;

        public static EnumSet NON_SUCCESS_STATUSES = EnumSet.of(FAILURE, RETRY, RESUMABLE_FAILURE);
    }

    private Status status;

    private Exception exception;
    private ApplicationException applicationException;
    private ErrorCode errorCode;

    private Outcome() {
    }

    public static Outcome success() {
        Outcome outcome = new Outcome();
        outcome.status = Status.SUCCESS;
        return outcome;
    }

    public static Outcome skipped() {
        Outcome outcome = new Outcome();
        outcome.status = Status.SKIPPED;
        return outcome;
    }


    public static Outcome failure(Exception exception) {
        Outcome outcome = new Outcome();
        outcome.status = Status.FAILURE;
        outcome.exception = exception;
        outcome.errorCode = ErrorCode.INTERNAL_SERVICE_ERROR;
        if (exception instanceof ApplicationException) {
            outcome.applicationException = (ApplicationException) exception;
            outcome.errorCode = ((ApplicationException) exception).getErrorCode();
        }

        return outcome;
    }

    public Exception getException() {
        return exception;
    }

    public String getExceptionMessage() {
        return exception.getMessage();
    }

    public ApplicationException getAsApplicationException() {

        if (applicationException != null) {
            return applicationException;
        }

        return new ApplicationException(ErrorCode.INTERNAL_SERVICE_ERROR, exception.getMessage());
    }

    public boolean isApplicationException() {
        return applicationException != null;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public Status getStatus() {
        return status;
    }

}
