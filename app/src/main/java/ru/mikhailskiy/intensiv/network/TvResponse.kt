package ru.mikhailskiy.intensiv.network

import com.google.gson.annotations.SerializedName


data class TvResponse(
    val page: Int,
    val results: List<TVShow>,
    @SerializedName("total_pages")
    val totalPages: Int,
    @SerializedName("total_results")
    val totalResults: Int
) {

    data class TVShow(
        @SerializedName("backdrop_path")
        val backdropPath: String,
        @SerializedName("first_air_date")
        val firstAirDate: String,
        @SerializedName("genre_ids")
        val genreIds: List<Int>,
        val id: Int,
        val name: String,
        @SerializedName("origin_country")
        val originCountry: List<String>,
        @SerializedName("original_language")
        val originalLanguage: String,
        @SerializedName("original_name")
        val originalName: String,
        val overview: String,
        val popularity: Double,
        @SerializedName("poster_path")
        val posterPath: String,
        @SerializedName("vote_average")
        val voteAverage: Double,
        @SerializedName("vote_count")
        val voteCount: Int
    )
}