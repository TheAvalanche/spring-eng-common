package com.symphodia.spring.common.graph

import com.symphodia.spring.common.error.ApplicationException
import com.symphodia.spring.common.error.ErrorCode
import spock.lang.Specification

class OutcomeTest extends Specification {

    def "test success outcome"() {
        when:
        Outcome successOutcome = Outcome.success();
        then:
        successOutcome.getStatus() == Outcome.Status.SUCCESS
        successOutcome.getException() == null
        successOutcome.getErrorCode() == null
    }

    def "test skipped outcome"() {
        when:
        Outcome skippedOutcome = Outcome.skipped();
        then:
        skippedOutcome.getStatus() == Outcome.Status.SKIPPED
        skippedOutcome.getException() == null
        skippedOutcome.getErrorCode() == null
    }

    def "test failed outcome"() {
        given:
        ApplicationException applicationException = new ApplicationException(ErrorCode.INTERNAL_SERVICE_ERROR, "Error")
        when:
        Outcome failedOutcome = Outcome.failure(applicationException);
        then:
        failedOutcome.getStatus() == Outcome.Status.FAILURE
        failedOutcome.getException() == applicationException
        failedOutcome.getAsApplicationException() == applicationException
        failedOutcome.getExceptionMessage() == "Error"
    }

}