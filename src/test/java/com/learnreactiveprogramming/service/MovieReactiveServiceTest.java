package com.learnreactiveprogramming.service;

import com.learnreactiveprogramming.domain.Movie;
import com.learnreactiveprogramming.domain.MovieInfo;
import com.learnreactiveprogramming.domain.Review;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

import java.time.LocalDate;
import java.util.List;

class MovieReactiveServiceTest {

    private final MovieReactiveService movieReactiveService = new MovieReactiveService(
        new MovieInfoService(),
        new ReviewService(),
        new RevenueService()
    );

    @Test
    void shouldGetAllMovies() {
        StepVerifier.create(movieReactiveService.getAllMovies())
            .expectNext(new Movie(
                new MovieInfo(100L, "Batman Begins", 2005, List.of("Christian Bale", "Michael Cane"), LocalDate.parse("2005-06-15")),
                List.of(
                    new Review(1L, 100L, "Awesome Movie", 8.9),
                    new Review(2L, 100L, "Excellent Movie", 9.0)
                ))
            )
            .expectNext(new Movie(
                new MovieInfo(101L,"The Dark Knight", 2008, List.of("Christian Bale", "HeathLedger"), LocalDate.parse("2008-07-18")),
                List.of(
                    new Review(1L, 101L, "Awesome Movie", 8.9),
                    new Review(2L, 101L, "Excellent Movie", 9.0)
                ))
            )
            .expectNext(new Movie(
                new MovieInfo(102L,"Dark Knight Rises", 2008, List.of("Christian Bale", "Tom Hardy"), LocalDate.parse("2012-07-20")),
                List.of(
                    new Review(1L, 102L, "Awesome Movie", 8.9),
                    new Review(2L, 102L, "Excellent Movie", 9.0)
                ))
            )
            .verifyComplete();
    }

    @Test
    void shouldGetMovieById() {
        StepVerifier.create(movieReactiveService.getMovie(100L))
            .expectNext(new Movie(
                new MovieInfo(100L, "Batman Begins", 2005, List.of("Christian Bale", "Michael Cane"), LocalDate.parse("2005-06-15")),
                List.of(
                    new Review(1L, 100L, "Awesome Movie", 8.9),
                    new Review(2L, 100L, "Excellent Movie", 9.0)
                ))
            )
            .verifyComplete();
    }
}
