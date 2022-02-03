package com.nasa.image.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Nasa::class], version = 1)
abstract class NasaDatabase : RoomDatabase(){

    abstract fun nasaDao(): NasaDao

    companion object {
        private var INSTANCE: NasaDatabase? = null
        fun getDatabase(context: Context): NasaDatabase {
            if (INSTANCE == null) {
                synchronized(this) {
                    INSTANCE = Room.databaseBuilder(
                        context,
                        NasaDatabase::class.java,
                        "nasa_database"
                    )
                        .build()
                }
            }
            return INSTANCE!!
        }
    }
}