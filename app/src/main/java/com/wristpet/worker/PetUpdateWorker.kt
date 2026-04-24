package com.wristpet.worker

import android.content.ComponentName
import android.content.Context
import androidx.wear.tiles.TileService
import androidx.wear.watchface.complications.datasource.ComplicationDataSourceUpdateRequester
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.wristpet.complication.PetComplicationService
import com.wristpet.data.health.StepCountReader
import com.wristpet.data.repository.PetRepository
import com.wristpet.engine.PetEngine
import com.wristpet.tile.PetTileService
import java.util.concurrent.TimeUnit

class PetUpdateWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        val pet = PetRepository.get()
        val steps = StepCountReader.dailySteps.value
        val updated = PetEngine.tick(pet, steps)
        PetRepository.update(updated)
        TileService.getUpdater(applicationContext)
            .requestUpdate(PetTileService::class.java)
        ComplicationDataSourceUpdateRequester.create(
            applicationContext,
            ComponentName(applicationContext, PetComplicationService::class.java)
        ).requestUpdateAll()
        return Result.success()
    }
}

object PetUpdateScheduler {
    private const val WORK_NAME = "pet_update"

    fun schedule(context: Context) {
        val request = PeriodicWorkRequestBuilder<PetUpdateWorker>(
            15, TimeUnit.MINUTES
        )
            .setConstraints(
                Constraints.Builder()
                    .setRequiresBatteryNotLow(true)
                    .build()
            )
            .build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            request
        )
    }
}
