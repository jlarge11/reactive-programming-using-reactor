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
    void namesFluxMap() {
        StepVerifier.create(fluxAndMonoGeneratorService.namesFluxMap())
//            .expectNext("ALEX", "CHLOE")
            .expectNext("ALEX")
            .verifyComplete();
    }

    @Test
    void namesFluxFlatMap() {
        StepVerifier.create(fluxAndMonoGeneratorService.namesFluxFlatMap())
            .expectNext("A", "L", "E", "X", "C", "H", "L", "O", "E")
            .verifyComplete();
    }

    @Test
    void namesFluxFlatMapAsync() {
        StepVerifier.create(fluxAndMonoGeneratorService.namesFluxFlatMapAsync())
            .expectNextCount(9L)
//            .expectNext("A", "L", "E", "X", "C", "H", "L", "O", "E")
            .verifyComplete();
    }

    @Test
    void namesFluxConcatMapAsync() {
        StepVerifier.create(fluxAndMonoGeneratorService.namesFluxConcatMapAsync())
            .expectNext("A", "L", "E", "X", "C", "H", "L", "O", "E")
            .verifyComplete();
    }

    @Test
    void nameMono() {
        StepVerifier.create(fluxAndMonoGeneratorService.nameMono())
            .expectNext("alex")
            .verifyComplete();
    }

    @Test
    void namesMono_map_filter_stringLengthSmallEnough() {
        StepVerifier.create(fluxAndMonoGeneratorService.namesMono_map_filter(3))
            .expectNext("ALEX")
            .verifyComplete();
    }

    @Test
    void namesMono_map_filter_stringLengthTooLarge() {
        StepVerifier.create(fluxAndMonoGeneratorService.namesMono_map_filter(4))
            .expectNext("default")
            .verifyComplete();
    }

    @Test
    void namesMono_map_filter_switchIfEmpty_stringLengthSmallEnough() {
        StepVerifier.create(fluxAndMonoGeneratorService.namesMono_map_filter_switchIfEmpty(3))
            .expectNext("ALEX")
            .verifyComplete();
    }

    @Test
    void namesMono_map_filter_switchIfEmpty_stringLengthTooLarge() {
        StepVerifier.create(fluxAndMonoGeneratorService.namesMono_map_filter_switchIfEmpty(4))
            .expectNext("DEFAULT")
            .verifyComplete();
    }

}
