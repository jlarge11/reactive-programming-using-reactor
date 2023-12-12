package com.learnreactiveprogramming.service;

import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

class ReactorExecutionModelServiceTest {

    private final ReactorExecutionModelService service = new ReactorExecutionModelService();

    @Test
    void namesFluxFlatMapTest() {
        StepVerifier.create(service.namesFluxFlatMap(3))
            .expectNext("A", "L", "E", "X", "C", "H", "L", "O", "E")
            .verifyComplete();
    }

    @Test
    void namesFluxFlatMapAsyncTest() {
        StepVerifier.create(service.namesFluxFlatMapAsync(3))
            .expectNextCount(9L)
            .verifyComplete();
    }

}
