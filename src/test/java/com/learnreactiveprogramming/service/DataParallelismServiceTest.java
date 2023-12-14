package com.learnreactiveprogramming.service;

import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

class DataParallelismServiceTest {

    private final DataParallelismService dataParallelismService = new DataParallelismService();

    @Test
    void exploreParallel() {
        StepVerifier.create(dataParallelismService.exploreParallel())
            .expectNextCount(12)
            .verifyComplete();
    }

    @Test
    void exploreParallelUsingFlatMap() {
        StepVerifier.create(dataParallelismService.exploreParallelUsingFlatMap())
            .expectNextCount(12)
            .verifyComplete();
    }

    @Test
    void exploreParallelUsingFlatMapWithMerge() {
        StepVerifier.create(dataParallelismService.exploreParallelUsingFlatMapWithMerge())
            .expectNextCount(6)
            .verifyComplete();
    }

}
