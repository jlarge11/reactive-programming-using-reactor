package com.learnreactiveprogramming.service;

import com.learnreactiveprogramming.domain.Movie;
import com.learnreactiveprogramming.domain.MovieInfo;
import com.learnreactiveprogramming.domain.Revenue;
import com.learnreactiveprogramming.domain.Review;
import com.learnreactiveprogramming.exception.MovieException;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.util.List;

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

    public Flux<Movie> getAllMoviesWithRetry() {
        return movieInfoService.retrieveMoviesFlux()
            .flatMap(movieInfo -> {
                Long movieInfoId = movieInfo.getMovieInfoId();

                return reviewService.retrieveReviewsFlux(movieInfoId)
                    .collectList()
                    .map(reviews -> new Movie(movieInfo, reviews));
            })
            .retryWhen(Retry
                .backoff(3, Duration.ofMillis(500L))
                .onRetryExhaustedThrow((rbs, rs) -> rs.failure())
            )
            .onErrorMap(MovieException::new)
            .log();
    }

    public Mono<Movie> getMovie(long movieId) {

        Mono<MovieInfo> movieInfoMono = movieInfoService.retrieveMovieInfoMonoUsingId(movieId);

        Mono<List<Review>> reviewsMono = reviewService.retrieveReviewsFlux(movieId)
            .collectList();

        Mono<Revenue> revenueMono = Mono.fromCallable(() -> revenueService.getRevenue(movieId))
            .subscribeOn(Schedulers.boundedElastic());

        return movieInfoMono
            .zipWith(reviewsMono, Movie::new)
            .zipWith(revenueMono, (movie, revenue) -> {
                movie.setRevenue(revenue);
                return movie;
            });

    }

    public Mono<Movie> getMovieUsingZipWith(long movieId) {
        return movieInfoService.retrieveMovieInfoMonoUsingId(movieId)
            .zipWith(reviewService.retrieveReviewsFlux(movieId)
                .collectList(),
                Movie::new);
    }
}
