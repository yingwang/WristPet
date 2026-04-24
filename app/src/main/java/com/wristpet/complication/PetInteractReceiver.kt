package com.wristpet.complication

import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import androidx.wear.tiles.TileService
import androidx.wear.watchface.complications.datasource.ComplicationDataSourceUpdateRequester
import com.wristpet.data.repository.PetRepository
import com.wristpet.engine.PetEngine
import com.wristpet.tile.PetTileService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PetInteractReceiver : BroadcastReceiver() {

    companion object {
        const val ACTION_INTERACT = "com.wristpet.ACTION_INTERACT"
    }

    override fun onReceive(context: Context, intent: Intent?) {
        if (intent?.action != ACTION_INTERACT) return
        val pending = goAsync()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val pet = PetRepository.get()
                val updated = PetEngine.interact(pet)
                PetRepository.update(updated)
                TileService.getUpdater(context)
                    .requestUpdate(PetTileService::class.java)
                ComplicationDataSourceUpdateRequester.create(
                    context, ComponentName(context, PetComplicationService::class.java)
                ).requestUpdateAll()
            } finally {
                pending.finish()
            }
        }
    }
}
