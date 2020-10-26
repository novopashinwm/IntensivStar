package ru.mikhailskiy.intensiv.db

data class MoviesResponse (
    val dates: Dates,
    val page: Int,
    val results: List<MovieEntity>,
    val totalPages: Int,
    val totalResults: Int
)