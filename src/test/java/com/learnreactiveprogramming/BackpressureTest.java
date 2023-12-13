package com.learnreactiveprogramming;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.reactivestreams.Subscription;
import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Flux;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
class BackpressureTest {

    @Test
    void testBackpressure() throws InterruptedException {
        Flux<Integer> numbers = Flux.range(1, 100)
            .log();

        CountDownLatch latch = new CountDownLatch(1);

        numbers
            .onBackpressureDrop(item -> {
                log.info("Dropped Item: {}", item);
            })
            .subscribe(new BaseSubscriber<Integer>() {
            @Override
            protected void hookOnSubscribe(Subscription subscription) {
                request(2);
            }

            @Override
            protected void hookOnNext(Integer value) {
                log.info("hookOnNext({})", value);

                if (value % 2 == 0) {
                    request(2);
                }

                if (value >= 50) {
                    cancel();
                }
            }

            @Override
            protected void hookOnComplete() {
                log.info("hookOnComplete()");
            }

            @Override
            protected void hookOnError(Throwable throwable) {
                log.info("hookOnError()", throwable);
            }

            @Override
            protected void hookOnCancel() {
                log.info("hookOnCancel()");
                latch.countDown();
            }
        });

        assertTrue(latch.await(5L, TimeUnit.SECONDS));
    }
}
