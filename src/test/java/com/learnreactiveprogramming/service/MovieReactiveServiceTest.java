package com.learnreactiveprogramming.service;

import com.learnreactiveprogramming.domain.Movie;
import com.learnreactiveprogramming.domain.MovieInfo;
import com.learnreactiveprogramming.domain.Revenue;
import com.learnreactiveprogramming.domain.Review;
import com.learnreactiveprogramming.exception.MovieException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;

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
        expectAllData(movieReactiveService.getAllMovies());
    }

    @Test
    void shouldThrowMovieExceptionWhenReviewServiceThrowsExceptionOnFirstReview() {
        when(reviewService.retrieveReviewsFlux(100L)).thenThrow(IllegalStateException.class);

        StepVerifier.create(movieReactiveService.getAllMovies())
            .verifyError(MovieException.class);
    }

    @Test
    void shouldThrowMovieExceptionWhenReviewServiceThrowsExceptionOnSecondReview() {
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
        when(reviewService.retrieveReviewsFlux(100L)).thenThrow(IllegalStateException.class);

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
                ),
                Revenue.builder()
                    .movieInfoId(100L)
                    .budget(1000000)
                    .boxOffice(5000000)
                    .build()
            ))
            .verifyComplete();
    }

    @Test
    void getAllMoviesWithRetryShouldThrowOriginalExceptionWhenRetriesExhausted() {
        when(reviewService.retrieveReviewsFlux(100L)).thenThrow(IllegalStateException.class);

        StepVerifier.create(movieReactiveService.getAllMoviesWithRetry())
            .verifyError(MovieException.class);

        verify(reviewService, times(4)).retrieveReviewsFlux(100L);
    }

    @Test
    void getAllMoviesWithRetryShouldSucceedAfterThirdTry() {
        when(reviewService.retrieveReviewsFlux(100L))
            .thenThrow(IllegalStateException.class)
            .thenThrow(IllegalStateException.class)
            .thenCallRealMethod();

        expectAllData(movieReactiveService.getAllMoviesWithRetry());

        verify(reviewService, times(3)).retrieveReviewsFlux(100L);
    }

    private void expectAllData(Flux<Movie> movies) {
        StepVerifier.create(movies)
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

}
