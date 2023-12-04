package com.learnreactiveprogramming.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;
import java.util.Random;
import java.util.function.Function;
import java.util.function.UnaryOperator;

public class FluxAndMonoGeneratorService {
    public Flux<String> namesFlux() {
        return Flux.fromIterable(List.of("alex", "ben", "chloe")).log();
    }

    public Flux<String> namesFluxMap() {
        return Flux.fromIterable(List.of("alex", "ben", "chloe"))
            .map(String::toUpperCase)
            .filter(s -> s.length() > 3)
            .log();
    }

    public Flux<String> namesFluxFlatMap() {
        return Flux.fromIterable(List.of("alex", "ben", "chloe"))
            .map(String::toUpperCase)
            .filter(s -> s.length() > 3)
            .flatMap(this::splitString)
            .log();
    }

    private Flux<String> splitString(String s) {
        return Flux.fromArray(s.split(""));
    }

    public Flux<String> namesFluxFlatMapAsync() {
        return Flux.fromIterable(List.of("alex", "ben", "chloe"))
            .map(String::toUpperCase)
            .filter(s -> s.length() > 3)
            .flatMap(this::splitStringWithDelay)
            .log();
    }

    public Flux<String> namesFluxConcatMapAsync() {
        return Flux.fromIterable(List.of("alex", "ben", "chloe"))
            .map(String::toUpperCase)
            .filter(s -> s.length() > 3)
            .concatMap(this::splitStringWithDelay)
            .log();
    }

    private Flux<String> splitStringWithDelay(String s) {
        int delay = new Random().nextInt(1000);

        return Flux.fromArray(s.split("")).delayElements(Duration.ofMillis(delay));
    }

    public Mono<String> nameMono() {
        return Mono.just("alex").log();
    }

    public Mono<String> namesMono_map_filter(int stringLength) {
        return Mono.just("alex")
            .map(String::toUpperCase)
            .filter(s -> s.length() > stringLength)
            .defaultIfEmpty("default");
    }

    public Mono<String> namesMono_map_filter_switchIfEmpty(int stringLength) {
        UnaryOperator<Mono<String>> transformer = f -> f
            .map(String::toUpperCase)
            .filter(s -> s.length() > stringLength);

        Mono<String> defaultMono = Mono.just("default")
            .transform(transformer);

        return Mono.just("alex")
            .transform(transformer)
            .switchIfEmpty(defaultMono);
    }

    public static void main(String[] args) {
        FluxAndMonoGeneratorService fluxAndMonoGeneratorService = new FluxAndMonoGeneratorService();

        fluxAndMonoGeneratorService.namesFlux()
                .subscribe(name -> {
                    System.out.println("Name is: " + name);
                });

        fluxAndMonoGeneratorService.nameMono()
                .subscribe(name -> {
                    System.out.println("Mono name is: " + name);
                });
    }
}
