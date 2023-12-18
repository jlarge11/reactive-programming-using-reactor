package com.learnreactiveprogramming.service;

import com.learnreactiveprogramming.domain.MovieInfo;
import com.learnreactiveprogramming.domain.Review;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.test.StepVerifier;

import java.time.LocalDate;
import java.util.List;

class ReviewServieTest {

    private final WebClient webClient = WebClient.builder()
        .baseUrl("http://localhost:8080/movies")
        .build();

    private final ReviewService service = new ReviewService(webClient);

    @Test
    void shouldRetrieveReviewsFromApi() {
        StepVerifier.create(service.retrieveReviewsFromApi(1L))
            .expectNextMatches(r -> r.getReviewId() == 1L)
            .verifyComplete();
    }
}
