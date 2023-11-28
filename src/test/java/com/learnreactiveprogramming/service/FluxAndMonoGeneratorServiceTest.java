package com.learnreactiveprogramming.service;

import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

class FluxAndMonoGeneratorServiceTest {

    private final FluxAndMonoGeneratorService fluxAndMonoGeneratorService = new FluxAndMonoGeneratorService();

    @Test
    void namesFlux() {
        StepVerifier.create(fluxAndMonoGeneratorService.namesFlux())
                .expectNext("alex", "ben", "chloe")
                .verifyComplete();
    }

    @Test
    void nameMono() {
        StepVerifier.create(fluxAndMonoGeneratorService.nameMono())
                .expectNext("alex")
                .verifyComplete();
    }
}
