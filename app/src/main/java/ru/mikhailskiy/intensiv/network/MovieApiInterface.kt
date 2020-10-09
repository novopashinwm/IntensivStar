package ru.mikhailskiy.intensiv.network

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query


interface MovieApiInterface {
    @GET("movie/top_rated")
    fun getTopRatedMovies(@Query("api_key") apiKey: String, @Query("language") language: String): Call<MoviesResponse>

    @GET("search/movie")
    fun searchByQuery(@Query("language") language: String, @Query("query") query: String) : Call<MoviesResponse>

    @GET("movie/now_playing")
    fun getNowPlaying(@Query("api_key") apiKey: String, @Query("language") language: String) : Call<MoviesResponse>

    @GET("movie/upcoming")
    fun getUpcoming(@Query("api_key") apiKey: String, @Query("language") language: String) : Call<MoviesResponse>

    @GET("movie/popular")
    fun getPopular(@Query("api_key") apiKey: String, @Query("language") language: String) : Call<MoviesResponse>

    @GET("tv/popular")
    fun getTVPopular(@Query("api_key") apiKey: String, @Query("language") language: String) : Call<TvResponse>

}
