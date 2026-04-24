package com.wristpet

import android.app.Application
import com.wristpet.data.health.StepCountReader
import com.wristpet.data.repository.PetRepository
import com.wristpet.worker.PetUpdateScheduler

class WristPetApp : Application() {
    override fun onCreate() {
        super.onCreate()
        PetRepository.init(this)
        StepCountReader.start(this)
        PetUpdateScheduler.schedule(this)
    }
}
