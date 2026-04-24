package com.wristpet.data.db

import androidx.room.TypeConverter
import com.wristpet.data.model.PetState

class Converters {
    @TypeConverter
    fun fromPetState(state: PetState): String = state.name

    @TypeConverter
    fun toPetState(value: String): PetState = PetState.valueOf(value)
}
