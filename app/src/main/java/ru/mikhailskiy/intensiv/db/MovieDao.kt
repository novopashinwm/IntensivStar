package ru.mikhailskiy.intensiv.db

import androidx.room.*
import io.reactivex.Completable
import io.reactivex.Observable
import retrofit2.http.Path

@Dao
interface MovieDao {

    @Query("select * from MovieEntity")
    fun loadAll(): Observable< List<MovieEntity>>

    @Transaction
    @Query("select * from MovieEntity where id = :movie_id")
    fun get( movie_id: Int): Observable< MovieEntity>

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