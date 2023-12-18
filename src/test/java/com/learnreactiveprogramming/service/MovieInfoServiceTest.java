package com.learnreactiveprogramming.service;

import com.learnreactiveprogramming.domain.MovieInfo;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.test.StepVerifier;

import java.time.LocalDate;
import java.util.List;

class MovieInfoServiceTest {

    private final WebClient webClient = WebClient.builder()
        .baseUrl("http://localhost:8080/movies")
        .build();

    private final MovieInfoService movieInfoService = new MovieInfoService(webClient);

    @Test
    void shouldRetrieveAllMovieInfo() {
        StepVerifier.create(movieInfoService.retrieveAllMovieInfo())
            .expectNextCount(7)
            .verifyComplete();
    }

    @Test
    void shouldRetrieveSingleMovieInfo() {
        StepVerifier.create(movieInfoService.retrieveMovieInfo(1L))
            .expectNext(new MovieInfo(
                1L,
                "Batman Begins",
                2005,
                List.of("Christian Bale", "Michael Cane"),
                LocalDate.parse("2005-06-15")
            ))
            .verifyComplete();
    }
}
