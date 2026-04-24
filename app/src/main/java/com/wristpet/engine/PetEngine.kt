package com.wristpet.engine

import com.wristpet.data.model.Pet
import com.wristpet.data.model.PetState
import java.util.Calendar

object PetEngine {

    private const val STEP_GOAL = 6000
    private const val BORED_THRESHOLD_MS = 2 * 60 * 60 * 1000L
    private const val ANGRY_THRESHOLD_MS = 6 * 60 * 60 * 1000L
    private const val SLEEP_START_HOUR = 22
    private const val SLEEP_END_HOUR = 7

    fun tick(pet: Pet, currentSteps: Int): Pet {
        val now = System.currentTimeMillis()
        val hoursSinceInteraction = now - pet.lastInteractionEpochMs

        val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        if (hour >= SLEEP_START_HOUR || hour < SLEEP_END_HOUR) {
            val energy = (pet.energy + 5).coerceAtMost(100)
            return pet.copy(
                state = PetState.SLEEPING,
                energy = energy,
                totalStepsToday = currentSteps,
                lastUpdateEpochMs = now
            )
        }

        val elapsedMinutes = ((now - pet.lastUpdateEpochMs) / 60_000).toInt()
        var happiness = (pet.happiness - elapsedMinutes / 10).coerceIn(0, 100)
        val energy = (pet.energy - elapsedMinutes / 15).coerceIn(0, 100)
        val health = pet.health

        val stepProgress = (currentSteps.toFloat() / STEP_GOAL).coerceAtMost(1f)
        happiness = (happiness + (stepProgress * 20).toInt()).coerceAtMost(100)

        val state = when {
            health < 30 -> PetState.SICK
            hoursSinceInteraction > ANGRY_THRESHOLD_MS -> PetState.ANGRY
            hoursSinceInteraction > BORED_THRESHOLD_MS || happiness < 30 -> PetState.BORED
            else -> PetState.HAPPY
        }

        return pet.copy(
            state = state,
            happiness = happiness,
            energy = energy,
            health = health,
            totalStepsToday = currentSteps,
            lastUpdateEpochMs = now
        )
    }

    fun interact(pet: Pet): Pet {
        val now = System.currentTimeMillis()
        return pet.copy(
            happiness = (pet.happiness + 15).coerceAtMost(100),
            energy = (pet.energy + 5).coerceAtMost(100),
            lastInteractionEpochMs = now,
            lastUpdateEpochMs = now
        )
    }
}
