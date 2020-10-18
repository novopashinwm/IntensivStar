package ru.mikhailskiy.intensiv.network

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface MovieApiInterface {
    @GET("movie/top_rated")
    fun getTopRatedMovies(@Query("api_key") apiKey: String, @Query("language") language: String): Single<MoviesResponse>

    @GET("search/movie")
    fun searchByQuery(@Query("language") language: String, @Query("query") query: String) : Single<MoviesResponse>

    @GET("movie/now_playing")
    fun getNowPlaying(@Query("api_key") apiKey: String, @Query("language") language: String) : Single<MoviesResponse>

    @GET("movie/upcoming")
    fun getUpcoming(@Query("api_key") apiKey: String, @Query("language") language: String) : Single<MoviesResponse>

    @GET("movie/popular")
    fun getPopular(@Query("api_key") apiKey: String, @Query("language") language: String) : Single<MoviesResponse>

    @GET("movie/{movie_id}")
    fun getMovie(@Path("movie_id") movie_id:Int, @Query("api_key") apiKey: String, @Query("language") language: String) : Single<MovieDetailsResponse>

    @GET("tv/popular")
    fun getTVPopular( @Query("api_key") apiKey: String, @Query("language") language: String) : Single<TvResponse>

}
