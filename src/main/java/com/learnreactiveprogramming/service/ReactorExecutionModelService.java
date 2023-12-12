package com.learnreactiveprogramming.service;

import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.List;

public class ReactorExecutionModelService {
    public Flux<String> namesFluxFlatMap(int stringLength) {
        return Flux.fromIterable(List.of("alex", "ben", "chloe"))
            .map(String::toUpperCase)
            .filter(s -> s.length() > stringLength)
            .flatMap(this::splitString)
            .log();
    }

    private Flux<String> splitString(String s) {
        return Flux.fromArray(s.split(""));
    }

    public Flux<String> namesFluxFlatMapAsync(int stringLength) {
        return Flux.fromIterable(List.of("alex", "ben", "chloe"))
            .map(String::toUpperCase)
            .filter(s -> s.length() > stringLength)
            .flatMap(this::splitStringWithDelay)
            .log();
    }

    private Flux<String> splitStringWithDelay(String s) {
        return Flux.fromArray(s.split(""))
            .delayElements(Duration.ofSeconds(1L));
    }

}
