package com.learnreactiveprogramming.service;

import com.learnreactiveprogramming.domain.Movie;
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
            });
    }

    public Mono<Movie> getMovie(long movieId) {
        return movieInfoService.retrieveMovieInfoMonoUsingId(movieId)
            .zipWith(reviewService.retrieveReviewsFlux(movieId)
                .collectList(),
                Movie::new);
    }
}
