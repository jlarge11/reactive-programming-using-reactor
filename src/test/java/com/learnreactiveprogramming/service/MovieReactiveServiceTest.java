package com.learnreactiveprogramming.service;

import com.learnreactiveprogramming.domain.Movie;
import com.learnreactiveprogramming.domain.MovieInfo;
import com.learnreactiveprogramming.domain.Review;
import com.learnreactiveprogramming.exception.MovieException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.test.StepVerifier;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MovieReactiveServiceTest {

    @InjectMocks
    private MovieReactiveService movieReactiveService;

    @Spy
    private MovieInfoService movieInfoService;

    @Spy
    private ReviewService reviewService;

    @Spy
    private RevenueService revenueService;

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
    void shouldThrowMovieExeptionWhenReviewServiceThrowsExceptionOnFirstReview() {
        when(reviewService.retrieveReviewsFlux(100L)).thenThrow(IllegalStateException.class);

        StepVerifier.create(movieReactiveService.getAllMovies())
            .verifyError(MovieException.class);
    }

    @Test
    void shouldThrowMovieExeptionWhenReviewServiceThrowsExceptionOnSecondReview() {
        when(reviewService.retrieveReviewsFlux(100L)).thenCallRealMethod();
        when(reviewService.retrieveReviewsFlux(101L)).thenThrow(IllegalStateException.class);

        StepVerifier.create(movieReactiveService.getAllMovies())
            .expectNext(new Movie(
                new MovieInfo(100L, "Batman Begins", 2005, List.of("Christian Bale", "Michael Cane"), LocalDate.parse("2005-06-15")),
                List.of(
                    new Review(1L, 100L, "Awesome Movie", 8.9),
                    new Review(2L, 100L, "Excellent Movie", 9.0)
                ))
            )
            .verifyError(MovieException.class);
    }

    @Test
    void shouldThrowMovieExceptionWhenMovieInfoServiceThrowsException() {
        when(movieInfoService.retrieveMoviesFlux()).thenThrow(IllegalStateException.class);

        StepVerifier.create(movieReactiveService.getAllMovies())
            .verifyError(MovieException.class);
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
