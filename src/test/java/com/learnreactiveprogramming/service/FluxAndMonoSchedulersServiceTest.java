package com.learnreactiveprogramming.service;

import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

class FluxAndMonoSchedulersServiceTest {
    private final FluxAndMonoSchedulersService service = new FluxAndMonoSchedulersService();

    @Test
    void explorePublishOn() {
        StepVerifier.create(service.explorePublishOn())
            .expectNextCount(6)
            .verifyComplete();
    }

    @Test
    void exploreSubscribeOn() {
        StepVerifier.create(service.exploreSubscribeOn())
            .expectNextCount(6)
            .verifyComplete();
    }

}
