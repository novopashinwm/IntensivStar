package ru.mikhailskiy.intensiv.db

import androidx.room.*

@Dao
interface MovieDao {

    @Query("select * from movie")
    fun loadAll(): List<MovieDB>

    @Insert
    fun insert(movieDB: MovieDB)

    @Update
    fun update(movieDB: MovieDB)

    @Delete
    fun delete(vararg note: MovieDB)
}