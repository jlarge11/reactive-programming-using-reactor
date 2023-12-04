package com.learnreactiveprogramming.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

public class CombiningFluxAndMonoService {
    public Flux<String> exploreConcat() {
        Flux<String> abc = Flux.just("A", "B", "C");
        Flux<String> def = Flux.just("D", "E", "F");

        return Flux.concat(abc, def);
    }

    public Flux<String> exploreConcatWith() {
        Flux<String> abc = Flux.just("A", "B", "C");
        Flux<String> def = Flux.just("D", "E", "F");

        return abc.concatWith(def);
    }

    public Flux<String> exploreConcatWithMono() {
        Mono<String> a = Mono.just("A");
        Mono<String> b = Mono.just("B");

        return a.concatWith(b);
    }

    public Flux<String> exploreMerge() {
        Flux<String> abc = Flux.just("A", "B", "C").delayElements(Duration.ofMillis(100L));
        Flux<String> def = Flux.just("D", "E", "F").delayElements(Duration.ofMillis(125L));

        return Flux.merge(abc, def);
    }

    public Flux<String> exploreMergeWith() {
        Flux<String> abc = Flux.just("A", "B", "C").delayElements(Duration.ofMillis(100L));
        Flux<String> def = Flux.just("D", "E", "F").delayElements(Duration.ofMillis(125L));

        return abc.mergeWith(def);
    }

    public Flux<String> exploreMergeWithMono() {
        Mono<String> a = Mono.just("A");
        Mono<String> b = Mono.just("B");

        return a.mergeWith(b);
    }

    public Flux<String> exploreMergeSequential() {
        Flux<String> abc = Flux.just("A", "B", "C").delayElements(Duration.ofMillis(100L));
        Flux<String> def = Flux.just("D", "E", "F").delayElements(Duration.ofMillis(125L));

        return Flux.mergeSequential(abc, def);
    }

    public Flux<String> exploreZip() {
        Flux<String> abc = Flux.just("A", "B", "C");
        Flux<String> def = Flux.just("D", "E", "F");

        return Flux.zip(abc, def, (first, second) -> first + second);
    }

    public Flux<String> exploreZipWith() {
        Flux<String> abc = Flux.just("A", "B", "C");
        Flux<String> def = Flux.just("D", "E", "F");

        return abc.zipWith(def, (first, second) -> first + second);
    }

    public Flux<String> exploreZipFourFluxes() {
        Flux<String> abc = Flux.just("A", "B", "C");
        Flux<String> def = Flux.just("D", "E", "F");
        Flux<String> f123 = Flux.just("1", "2", "3");
        Flux<String> f456 = Flux.just("4", "5", "6");

        return Flux.zip(abc, def, f123, f456)
            .map(t -> t.getT1() + t.getT2() + t.getT3() + t.getT4());
    }

    public Mono<String> exporeZipWithMono() {
        Mono<String> a = Mono.just("A");
        Mono<String> b = Mono.just("B");

        return a.zipWith(b)
            .map(t -> t.getT1() + t.getT2());
    }
}
