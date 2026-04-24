package com.wristpet.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.wristpet.data.model.Pet

@Database(entities = [Pet::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class WristPetDatabase : RoomDatabase() {
    abstract fun petDao(): PetDao

    companion object {
        @Volatile
        private var INSTANCE: WristPetDatabase? = null

        fun get(context: Context): WristPetDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    WristPetDatabase::class.java,
                    "wristpet.db"
                ).build().also { INSTANCE = it }
            }
        }
    }
}
