package com.learnreactiveprogramming.service;

import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

class CombiningFluxAndMonoServiceTest {

    private final CombiningFluxAndMonoService service = new CombiningFluxAndMonoService();

    @Test
    void shouldExploreConcat() {
        StepVerifier.create(service.exploreConcat())
            .expectNext("A", "B", "C", "D", "E", "F")
            .verifyComplete();
    }

    @Test
    void shouldExploreConcatWith() {
        StepVerifier.create(service.exploreConcatWith())
            .expectNext("A", "B", "C", "D", "E", "F")
            .verifyComplete();
    }

    @Test
    void shouldExploreConcatWithMono() {
        StepVerifier.create(service.exploreConcatWithMono())
            .expectNext("A", "B")
            .verifyComplete();
    }

    @Test
    void shouldExploreMerge() {
        StepVerifier.create(service.exploreMerge())
            .expectNext("A", "D", "B", "E", "C", "F")
            .verifyComplete();
    }

    @Test
    void shouldExploreMergeWith() {
        StepVerifier.create(service.exploreMergeWith())
            .expectNext("A", "D", "B", "E", "C", "F")
            .verifyComplete();
    }

    @Test
    void shouldExploreMergeWithMono() {
        StepVerifier.create(service.exploreMergeWithMono())
            .expectNext("A", "B")
            .verifyComplete();
    }

    @Test
    void shouldExploreMergeSequential() {
        StepVerifier.create(service.exploreMergeSequential())
            .expectNext("A", "B", "C", "D", "E", "F")
            .verifyComplete();
    }

    @Test
    void shouldExploreZip() {
        StepVerifier.create(service.exploreZip())
            .expectNext("AD", "BE", "CF")
            .verifyComplete();
    }

    @Test
    void shouldExploreZipWith() {
        StepVerifier.create(service.exploreZipWith())
            .expectNext("AD", "BE", "CF")
            .verifyComplete();
    }

    @Test
    void shouldExploreZipFourFluxes() {
        StepVerifier.create(service.exploreZipFourFluxes())
            .expectNext("AD14", "BE25", "CF36")
            .verifyComplete();
    }

    @Test
    void shouldExploreZipWithMono() {
        StepVerifier.create(service.exporeZipWithMono())
            .expectNext("AB")
            .verifyComplete();
    }

}
