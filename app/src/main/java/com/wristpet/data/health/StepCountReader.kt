package com.wristpet.data.health

import android.content.ComponentName
import android.content.Context
import android.util.Log
import androidx.health.services.client.HealthServices
import androidx.health.services.client.PassiveListenerCallback
import androidx.health.services.client.data.DataPointContainer
import androidx.health.services.client.data.DataType
import androidx.health.services.client.data.PassiveListenerConfig
import androidx.wear.tiles.TileService
import androidx.wear.watchface.complications.datasource.ComplicationDataSourceUpdateRequester
import com.wristpet.complication.PetComplicationService
import com.wristpet.data.repository.PetRepository
import com.wristpet.engine.PetEngine
import com.wristpet.tile.PetTileService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

object StepCountReader {
    private const val TAG = "StepCountReader"
    private var appContext: Context? = null
    private val scope = CoroutineScope(Dispatchers.IO)

    private val _dailySteps = MutableStateFlow(0)
    val dailySteps: StateFlow<Int> = _dailySteps

    private val callback = object : PassiveListenerCallback {
        override fun onNewDataPointsReceived(dataPoints: DataPointContainer) {
            val steps = dataPoints.getData(DataType.STEPS_DAILY)
            steps.lastOrNull()?.let { dp ->
                _dailySteps.value = dp.value.toInt()
                Log.d(TAG, "Steps updated: ${dp.value}")
                refreshPetAndWidgets(dp.value.toInt())
            }
        }
    }

    private fun refreshPetAndWidgets(steps: Int) {
        val ctx = appContext ?: return
        scope.launch {
            val pet = PetRepository.get()
            val updated = PetEngine.tick(pet, steps)
            PetRepository.update(updated)
            // Refresh tile
            TileService.getUpdater(ctx)
                .requestUpdate(PetTileService::class.java)
            // Refresh complication
            ComplicationDataSourceUpdateRequester.create(
                ctx, ComponentName(ctx, PetComplicationService::class.java)
            ).requestUpdateAll()
        }
    }

    fun start(context: Context) {
        appContext = context.applicationContext
        val client = HealthServices.getClient(context).passiveMonitoringClient
        val config = PassiveListenerConfig.builder()
            .setDataTypes(setOf(DataType.STEPS_DAILY))
            .build()

        client.setPassiveListenerCallback(config, callback)
        Log.d(TAG, "Passive step listener registered")
    }

    fun stop(context: Context) {
        val client = HealthServices.getClient(context).passiveMonitoringClient
        client.clearPassiveListenerCallbackAsync()
    }
}
