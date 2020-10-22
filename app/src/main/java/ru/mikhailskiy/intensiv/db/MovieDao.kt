package ru.mikhailskiy.intensiv.db

import androidx.room.*
import io.reactivex.Completable
import io.reactivex.Observable

@Dao
interface MovieDao {

    @Query("select * from movie")
    fun loadAll(): Observable< List<MovieDB>>

    @Insert
    fun insert(movieDB: MovieDB) : Completable

    @Update
    fun update(movieDB: MovieDB): Completable

    @Delete
    fun delete(vararg note: MovieDB): Completable
}