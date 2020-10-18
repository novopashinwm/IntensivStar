package ru.mikhailskiy.intensiv.dto

import androidx.room.*

@Dao
interface MovieStore {

    @Query("select * from movie")
    fun loadAll(): List<MovieDto>

    @Insert
    fun insert(movie: MovieDto)

    @Update
    fun update(movie: MovieDto)

    @Delete
    fun delete(vararg note: MovieDto)
}