package com.trehub.wallpapermaster.utils.network;

import com.trehub.wallpapermaster.model.pojo.info.MovieInfo;
import com.trehub.wallpapermaster.model.pojo.info.VideosResponse;
import com.trehub.wallpapermaster.model.pojo.primaryList.MoviesList;
import com.trehub.wallpapermaster.model.pojo.secondaryList.SecondaryList;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

import static com.trehub.wallpapermaster.utils.Constants.PARAMS;

public interface ApiInterface {

    @GET("movie/top_rated" + PARAMS)
    Observable<MoviesList> getTopRated(@Query("page") int page);

    @GET("movie/popular" + PARAMS)
    Observable<MoviesList> getPopular(@Query("page") int page);

    @GET("movie/upcoming" + PARAMS)
    Observable<SecondaryList> getUpcoming(@Query("page") int page);

    @GET("movie/now_playing" + PARAMS)
    Observable<SecondaryList> getNowPlaying(@Query("page") int page);

    @GET("search/movie" + PARAMS)
    Observable<MoviesList> searchMovie(@Query("query") String query);

    @GET("movie/{movie_id}" + PARAMS)
    Observable<MovieInfo> getMovieInfo(@Path("movie_id") int movieId);

    @GET("movie/{movie_id}/videos" + PARAMS)
    Observable<VideosResponse> getVideos(@Path("movie_id") int movieId);
}
