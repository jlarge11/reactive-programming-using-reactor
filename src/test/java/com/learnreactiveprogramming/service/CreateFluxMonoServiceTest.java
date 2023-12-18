package com.learnreactiveprogramming.service;

import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

class CreateFluxMonoServiceTest {

    private final CreateFluxMonoService service = new CreateFluxMonoService();

    @Test
    void exploreGenerate() {
        StepVerifier.create(service.exploreGenerate().log())
            .expectNext(2, 4, 6, 8, 10, 12, 14, 16, 18, 20)
            .verifyComplete();
    }

    @Test
    void exploreFluxCreate() {
        StepVerifier.create(service.exploreFluxCreate().log())
            .expectNextCount(9)
            .verifyComplete();
    }

    @Test
    void exploreFluxJust() {
        StepVerifier.create(service.exploreFluxJust().log())
            .expectNext("alex", "ben", "chloe")
            .verifyComplete();
    }

    @Test
    void exploreMonoCreate() {
        StepVerifier.create(service.exploreMonoCreate())
            .expectNext("alex")
            .verifyComplete();
    }

    @Test
    void exploreFluxHandle() {
        StepVerifier.create(service.exploreHandler())
            .expectNext("ALEX", "CHLOE")
            .verifyComplete();
    }
}
