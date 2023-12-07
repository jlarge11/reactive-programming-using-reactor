package com.learnreactiveprogramming.service;

import com.learnreactiveprogramming.exception.ServiceException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

class ExceptionHandlingServiceTest {

    private ExceptionHandlingService service = new ExceptionHandlingService();

    @Test
    void exceptionFlux() {
        StepVerifier.create(service.exceptionFlux())
            .expectNext("A", "B", "C")
            .verifyError(RuntimeException.class);
    }

    @Test
    void exceptionFluxOnErrorReturn() {
        StepVerifier.create(service.exceptionFluxOnErrorReturn())
            .expectNext("A", "B", "C", "Didn't work")
            .verifyComplete();
    }

    @Test
    void exceptionFluxOnErrorResume() {
        StepVerifier.create(service.exceptionFluxOnErrorResume())
            .expectNext("A", "B", "C", "D", "E", "F")
            .verifyComplete();
    }

    @Test
    void exceptionFluxOnErrorContinue() {
        StepVerifier.create(service.exceptionFluxOnErrorContinue())
            .expectNext("a", "b", "d")
            .verifyComplete();
    }

    @Test
    void exceptionFluxOnErrorMap() {
        StepVerifier.create(service.exceptionFluxOnErrorMap())
            .expectNext("a", "b")
            .verifyError(ServiceException.class);
    }

    @Test
    void exceptionFluxDoOnError() {
        StepVerifier.create(service.exceptionFluxDoOnError())
            .expectNext("a", "b")
            .verifyError(IllegalStateException.class);
    }

    @Test
    void exceptionMonoOnErrorContinueUnhappyPath() {
        StepVerifier.create(service.exceptionMonoOnErrorContinue("abc"))
            .expectNext()
            .verifyComplete();
    }

    @Test
    void exceptionMonoOnErrorContinueHappyPath() {
        StepVerifier.create(service.exceptionMonoOnErrorContinue("reactor"))
            .expectNext("REACTOR")
            .verifyComplete();
    }

}
