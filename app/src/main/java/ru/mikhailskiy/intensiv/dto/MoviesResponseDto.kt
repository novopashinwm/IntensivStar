package ru.mikhailskiy.intensiv.dto

import com.google.gson.annotations.SerializedName

data class MoviesResponseDto (
    val dates: DatesDto,
    val page: Int,
    val results: List<MovieDto>,
    @SerializedName("total_pages")
    val totalPages: Int,
    @SerializedName("total_results")
    val totalResults: Int
)