package com.symphodia.spring.common.error

import spock.lang.Specification


class ErrorCodeTest extends Specification {

    def "test error code from exception"() {
        when:
        ErrorCode errorCode = ErrorCode.getErrorCode(new ApplicationException(ErrorCode.LISTENER_ERROR, "Listener error"));
        then:
        errorCode == ErrorCode.LISTENER_ERROR

        when:
        errorCode = ErrorCode.getErrorCode(new NullPointerException("null"));
        then:
        errorCode == ErrorCode.INTERNAL_SERVICE_ERROR
    }

}
