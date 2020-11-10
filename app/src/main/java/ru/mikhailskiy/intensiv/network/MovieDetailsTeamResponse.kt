package ru.mikhailskiy.intensiv.network

import com.google.gson.annotations.SerializedName


data class MovieDetailsTeamResponse(
    val cast: List<Cast>,
    val crew: List<Crew>,
    val id: Int
) {

    data class Cast(
        @SerializedName("cast_id")
        val castId: Int,
        val character: String,
        @SerializedName("credit_id")
        val creditId: String,
        val gender: Int,
        val id: Int,
        val name: String,
        val order: Int,
        @SerializedName("profile_path")
        val profilePath: String?
    )

    data class Crew(
        @SerializedName("credit_id")
        val creditId: String,
        val department: String,
        val gender: Int,
        val id: Int,
        val job: String,
        val name: String,
        @SerializedName("profile_path")
        val profilePath: Any?
    )
}