package com.wristpet.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pet")
data class Pet(
    @PrimaryKey val id: Int = 0,
    val name: String = "Pixel",
    val state: PetState = PetState.HAPPY,
    val happiness: Int = 100,
    val energy: Int = 100,
    val health: Int = 100,
    val totalStepsToday: Int = 0,
    val lastInteractionEpochMs: Long = System.currentTimeMillis(),
    val lastUpdateEpochMs: Long = System.currentTimeMillis(),
    val createdEpochMs: Long = System.currentTimeMillis()
)
