package com.wristpet.data.health

import android.content.Context
import android.util.Log
import androidx.health.services.client.HealthServices
import androidx.health.services.client.PassiveListenerCallback
import androidx.health.services.client.data.DataPointContainer
import androidx.health.services.client.data.DataType
import androidx.health.services.client.data.PassiveListenerConfig
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

object StepCountReader {
    private const val TAG = "StepCountReader"

    private val _dailySteps = MutableStateFlow(0)
    val dailySteps: StateFlow<Int> = _dailySteps

    private val callback = object : PassiveListenerCallback {
        override fun onNewDataPointsReceived(dataPoints: DataPointContainer) {
            val steps = dataPoints.getData(DataType.STEPS_DAILY)
            steps.lastOrNull()?.let { dp ->
                _dailySteps.value = dp.value.toInt()
                Log.d(TAG, "Steps updated: ${dp.value}")
            }
        }
    }

    fun start(context: Context) {
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
