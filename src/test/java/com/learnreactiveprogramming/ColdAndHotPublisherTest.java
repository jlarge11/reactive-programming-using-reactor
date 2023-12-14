package com.learnreactiveprogramming;

import org.junit.jupiter.api.Test;
import reactor.core.Disposable;
import reactor.core.publisher.ConnectableFlux;
import reactor.core.publisher.Flux;

import java.time.Duration;

import static com.learnreactiveprogramming.util.CommonUtil.delay;

class ColdAndHotPublisherTest {

    @Test
    void coldPublisherTest() {
        Flux<Integer> flux = Flux.range(1, 10);

        flux.subscribe(i -> System.out.println("Subscriber 1 : " + i));
        flux.subscribe(i -> System.out.println("Subscriber 2 : " + i));
    }

    @Test
    void hotPublisherTest() {
        Flux<Integer> flux = Flux.range(1, 10)
            .delayElements(Duration.ofSeconds(1L));

        ConnectableFlux<Integer> connectableFlux = flux.publish();
        connectableFlux.connect();

        connectableFlux.subscribe(i -> System.out.println("Subscriber 1 : " + i));

        delay(2000);

        connectableFlux.subscribe(i -> System.out.println("Subscriber 2 : " + i));

        delay(10000);
    }

    @Test
    void hotPublisherTestAutoConnect() {
        Flux<Integer> flux = Flux.range(1, 10)
            .delayElements(Duration.ofSeconds(1L));

        Flux<Integer> hotSource = flux.publish().refCount(2);

        Disposable disposable1 = hotSource.subscribe(i -> System.out.println("Subscriber 1 : " + i));

        delay(2000);

        Disposable disposable2 = hotSource.subscribe(i -> System.out.println("Subscriber 2 : " + i));
        System.out.println("Two subscribers are connected");

        delay(2000);
        disposable1.dispose();
        disposable2.dispose();

//        hotSource.subscribe(i -> System.out.println("Subscriber 3 : " + i));


        delay(10000);
    }
}
