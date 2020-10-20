package ru.mikhailskiy.intensiv.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [MovieDB::class], version = 1)
abstract class MovieDatabase : RoomDatabase() {
    abstract fun movieDao(): MovieDao
    companion object {
        private var instance: MovieDatabase? = null
        @Synchronized
        fun get(context: Context): MovieDatabase {
            if (instance == null) {
                instance = Room.databaseBuilder(
                    context.applicationContext,
                    MovieDatabase::class.java, "MovieDatabase"
                ).allowMainThreadQueries()
                    .build()
            }
            return instance!!
        }
    }
}