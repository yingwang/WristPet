package com.wristpet.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.wristpet.data.model.Pet
import kotlinx.coroutines.flow.Flow

@Dao
interface PetDao {
    @Query("SELECT * FROM pet WHERE id = 0")
    fun observe(): Flow<Pet?>

    @Query("SELECT * FROM pet WHERE id = 0")
    suspend fun get(): Pet?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(pet: Pet)
}
