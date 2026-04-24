package com.wristpet.data.repository

import android.content.Context
import com.wristpet.data.db.WristPetDatabase
import com.wristpet.data.db.PetDao
import com.wristpet.data.model.Pet
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull

object PetRepository {
    private lateinit var dao: PetDao

    fun init(context: Context) {
        dao = WristPetDatabase.get(context).petDao()
    }

    fun observe(): Flow<Pet> = dao.observe().filterNotNull()

    suspend fun get(): Pet = dao.get() ?: Pet().also { dao.upsert(it) }

    suspend fun update(pet: Pet) = dao.upsert(pet)
}
