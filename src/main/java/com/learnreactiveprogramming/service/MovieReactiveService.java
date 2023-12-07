package com.learnreactiveprogramming.service;

import com.learnreactiveprogramming.domain.Movie;
import com.learnreactiveprogramming.exception.MovieException;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@AllArgsConstructor
public class MovieReactiveService {

    private final MovieInfoService movieInfoService;
    private final ReviewService reviewService;
    private final RevenueService revenueService;

    public Flux<Movie> getAllMovies() {
        return movieInfoService.retrieveMoviesFlux()
            .flatMap(movieInfo -> {
                Long movieInfoId = movieInfo.getMovieInfoId();

                return reviewService.retrieveReviewsFlux(movieInfoId)
                    .collectList()
                    .map(reviews -> new Movie(movieInfo, reviews));
            })
            .onErrorMap(MovieException::new)
            .log();
    }

    public Mono<Movie> getMovie(long movieId) {
        return movieInfoService.retrieveMovieInfoMonoUsingId(movieId)
            .flatMap(movieInfo ->
                reviewService.retrieveReviewsFlux(movieId)
                    .collectList()
                    .map(reviews -> new Movie(movieInfo, reviews))
            );
    }

    public Mono<Movie> getMovieUsingZipWith(long movieId) {
        return movieInfoService.retrieveMovieInfoMonoUsingId(movieId)
            .zipWith(reviewService.retrieveReviewsFlux(movieId)
                .collectList(),
                Movie::new);
    }
}
