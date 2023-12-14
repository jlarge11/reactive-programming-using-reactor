package com.learnreactiveprogramming.service;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.ParallelFlux;
import reactor.core.scheduler.Schedulers;

import java.util.List;

import static com.learnreactiveprogramming.util.CommonUtil.delay;

@Slf4j
public class DataParallelismService {
    private static final List<String> NAMES_LIST = List.of("alex", "ben", "chloe");
    private static final List<String> NAMES_LIST_2 = List.of("diane", "eddie", "frank");

    private static final List<String> LONG_NAMES_LIST = List.of(
        "alex", "ben", "chloe", "diane", "eddie", "frank", "gary", "harry", "iris", "juliet", "kenny", "larry");


    public ParallelFlux<String> exploreParallel() {
        return Flux.fromIterable(LONG_NAMES_LIST)
            .parallel()
            .runOn(Schedulers.parallel())
            .map(this::upperCaseWithDelay)
            .log();
    }

    public Flux<String> exploreParallelUsingFlatMap() {
        return Flux.fromIterable(LONG_NAMES_LIST)
            .flatMap(name ->
                Mono.just(name)
                    .map(this::upperCaseWithDelay)
                    .subscribeOn(Schedulers.parallel())
            )
            .log();
    }

    public Flux<String> exploreParallelUsingFlatMapWithMerge() {
        Flux<String> flux = Flux.fromIterable(NAMES_LIST)
            .flatMapSequential(name ->
                Mono.just(name)
                    .map(this::upperCaseWithDelay)
                    .subscribeOn(Schedulers.parallel())
            )
            .log();

        Flux<String> flux1 = Flux.fromIterable(NAMES_LIST_2)
            .flatMapSequential(name ->
                Mono.just(name)
                    .map(this::upperCaseWithDelay)
                    .subscribeOn(Schedulers.parallel())
            )
            .map(s -> {
                log.info("Name is : {}", s);
                return s;
            })
            .log();

        return flux.mergeWith(flux1).sort();
    }

    private String upperCaseWithDelay(String name) {
        delay(1000);
        return name.toUpperCase();
    }
}
