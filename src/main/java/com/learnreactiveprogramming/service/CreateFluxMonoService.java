package com.learnreactiveprogramming.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import static com.learnreactiveprogramming.util.CommonUtil.delay;

public class CreateFluxMonoService {

    public Flux<Integer> exploreGenerate() {
        return Flux.generate(() -> 1, (state, sink) -> {
            sink.next(state * 2);

            if (state == 10) {
                sink.complete();
            }

            return state + 1;
        });
    }

    public Flux<String> exploreFluxCreate() {
        return Flux.create(sink ->
            CompletableFuture.supplyAsync(this::names)
                .thenAccept(names -> names.forEach(name -> {
                    sink.next(name);
                    sink.next(name);
                }))
                .thenRun(() -> sendEvents(sink))
        );
    }

    public void sendEvents(FluxSink<String> sink) {
        CompletableFuture.supplyAsync(this::names)
            .thenAccept(names -> names.forEach(sink::next))
            .thenRun(sink::complete);
    }

    public Flux<String> exploreFluxJust() {
        return Flux.fromIterable(names());
    }

    public List<String> names() {
        delay(1000);
        return List.of("alex", "ben", "chloe");
    }

    public String name() {
        delay(1000);
        return "alex";
    }

    public Mono<String> exploreMonoCreate() {
        return Mono.create(sink -> sink.success(name()));
    }

    public Flux<String> exploreHandler() {
        return Flux.fromIterable(List.of("alex", "ben", "chloe"))
            .handle((name, sink) -> {
                if (name.length() > 3) {
                    sink.next(name.toUpperCase());
                }
            });
    }
}
