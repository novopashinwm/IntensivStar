package ru.mikhailskiy.intensiv.dto

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [MovieDto::class], version = 1)
abstract class MovieDatabase: RoomDatabase() {
    abstract fun movies() : MovieStore
}