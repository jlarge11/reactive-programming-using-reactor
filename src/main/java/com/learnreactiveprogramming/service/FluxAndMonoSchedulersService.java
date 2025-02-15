package com.learnreactiveprogramming.service;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.util.List;

import static com.learnreactiveprogramming.util.CommonUtil.delay;

@Slf4j
public class FluxAndMonoSchedulersService {

    static List<String> namesList = List.of("alex", "ben", "chloe");
    static List<String> namesList1 = List.of("adam", "jill", "jack");

    public Flux<String> explorePublishOn() {
        Flux<String> namesFlux = Flux.fromIterable(namesList)
            .publishOn(Schedulers.boundedElastic())
            .map(this::upperCase)
            .log();

        Flux<String> namesFlux1 = Flux.fromIterable(namesList1)
            .publishOn(Schedulers.boundedElastic())
            .map(this::upperCase)
            .map(s -> {
                log.info("s = {}", s);
                return s;
            })
            .log();

        return namesFlux.mergeWith(namesFlux1);
    }

    public Flux<String> exploreSubscribeOn() {
        Flux<String> namesFlux = namesToUpper(namesList)
            .subscribeOn(Schedulers.boundedElastic())
            .log();

        Flux<String> namesFlux1 = namesToUpper(namesList1)
            .subscribeOn(Schedulers.boundedElastic())
            .map(s -> {
                log.info("s = {}", s);
                return s;
            })
            .log();

        return namesFlux.mergeWith(namesFlux1);
    }

    private Flux<String> namesToUpper(List<String> names) {
        return Flux.fromIterable(names)
            .map(this::upperCase);
    }

    private String upperCase(String name) {
        delay(1000);
        return name.toUpperCase();
    }

}
