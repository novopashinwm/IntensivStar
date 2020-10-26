package ru.mikhailskiy.intensiv.db

import androidx.room.*
import io.reactivex.Completable
import io.reactivex.Observable

@Dao
interface MovieDao {

    @Query("select * from MovieEntity")
    fun loadAll(): Observable< List<MovieEntity>>

    @Insert
    fun insert(movieEntity: MovieEntity) : Completable

    //А Room переварит List<MovieEntity>?
    @Insert
    fun insertAll(movieList: List<MovieEntity>):Completable

    @Update
    fun update(movieEntity:  MovieEntity): Completable

    @Delete
    fun delete(vararg movieEntity: MovieEntity): Completable
}