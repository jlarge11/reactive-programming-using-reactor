package com.learnreactiveprogramming.service;

import com.learnreactiveprogramming.exception.ReactorException;
import com.learnreactiveprogramming.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
public class ExceptionHandlingService {

    public Flux<String> exceptionFlux() {

        return Flux.just("A", "B", "C")
            .concatWith(Flux.error(new RuntimeException("OH NOES!!")))
            .concatWith(Flux.just("D"));
    }

    public Flux<String> exceptionFluxOnErrorReturn() {
        return Flux.just("A", "B", "C")
            .concatWith(Flux.error(new RuntimeException("OH NOES!!")))
            .concatWith(Flux.just("D"))
            .onErrorReturn("Didn't work");
    }

    public Flux<String> exceptionFluxOnErrorResume() {
        return Flux.just("A", "B", "C")
            .concatWith(Flux.error(new RuntimeException("OH NOES!!")))
            .onErrorResume(t -> {
                log.error(t.getMessage(), t);
                return Flux.just("D", "E", "F");
            });
    }

    public Flux<String> exceptionFluxOnErrorContinue() {
        return Flux.just("A", "B", "C", "D")
            .map(s -> {
                if ("C".equals(s)) {
                    throw new IllegalStateException("Can't process this one");
                }

                return s.toLowerCase();
            })
            .onErrorContinue((t, o) -> log.error("Unable to process " + o, t));
    }

    public Flux<String> exceptionFluxOnErrorMap() {
        return Flux.just("A", "B", "C", "D")
            .map(s -> {
                if ("C".equals(s)) {
                    throw new IllegalStateException("Can't process this one");
                }

                return s.toLowerCase();
            })
            .onErrorMap(ServiceException::new);
    }

    public Flux<String> exceptionFluxDoOnError() {
        return Flux.just("A", "B", "C", "D")
            .map(s -> {
                if ("C".equals(s)) {
                    throw new IllegalStateException("Can't process this one");
                }

                return s.toLowerCase();
            })
            .doOnError(t -> log.error("Shouldn't have happened", t));
    }

    public Mono<Object> exceptionMonoOnErrorMap(RuntimeException e) {
        return Mono.just("B")
            .map(s -> {
                throw e;
            })
            .onErrorMap(t -> new ReactorException(t, t.getMessage()));
    }

    public Mono<String> exceptionMonoOnErrorContinue(String s) {
        return Mono.just(s)
            .map(s1 -> {
                if ("abc".equals(s1)) {
                    throw new RuntimeException("Unable to process " + s1);
                }

                return s1.toUpperCase();
            })
            .onErrorContinue((t, s2) -> log.error("Problem with processing {}", s2, t));
    }
}
